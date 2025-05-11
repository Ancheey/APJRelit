package net.ancheey.apjrelit.mixins;
import net.minecraft.world.damagesource.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CombatRules.class)
public class CombatRulesMixin {
	private static final float TOUGHNESS_BASE = 0f;

	@Overwrite
	public static float getDamageAfterAbsorb(float pDamage, float pTotalArmor, float pToughnessAttribute){

		float toughness = TOUGHNESS_BASE + pToughnessAttribute;
		float mitigatedDamage = Math.min(pDamage,toughness);
		float halfMitDamage = Math.min(pDamage - mitigatedDamage, toughness);
		float noMitDamage = Math.max(pDamage - mitigatedDamage - halfMitDamage,0 );
		float armorPercentage = (pTotalArmor + 1)/Math.max(1,120+2*pTotalArmor);
		float damageAnnulment = Math.max(0, toughness- pDamage);
		//APJRelitCore.LOGGER.info("Hit for: "+pDamage+" | A"+pTotalArmor+" ("+(armorPercentage)*100+"%) |T"+toughness+" | Damage: "+mitigatedDamage+"/"+halfMitDamage+"/"+noMitDamage + " ("+finalDamage+" damage taken)");
		return Math.max((mitigatedDamage * (1-armorPercentage)) + (halfMitDamage * (1-(armorPercentage/2))) + noMitDamage - (damageAnnulment*0.3f),0);
	}
}
