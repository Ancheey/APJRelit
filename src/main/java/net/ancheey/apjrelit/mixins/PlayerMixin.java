package net.ancheey.apjrelit.mixins;

import com.mojang.authlib.GameProfile;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.ancheey.apjrelit.dndmodule.DamageHelper;
import net.ancheey.apjrelit.util.APJFormulas;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.api.WeaponAttributesHelper;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(Player.class)
public class PlayerMixin {

	@ModifyArg(
			method = "attack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
			),
			index = 1 // index 1 = the float damage parameter
	)
	private float modifyFinalDamage(float original) {
		var player = ((Player)((Object) this));
		if(player.level().isClientSide)
			return original;

		var item = player.getMainHandItem();
		var damage = DamageHelper.getRandomisedItemDamage(player,item);
		double mod = original/ player.getAttributes().getBaseValue(Attributes.ATTACK_DAMAGE);
		var retval =  damage==0?1f:damage * mod;
		return (float)(retval);
	}
@Shadow
private int experienceLevel;
	@Inject(method = "getXpNeededForNextLevel", at = @At("HEAD"), cancellable = true)
	private void getNewExpByFormula(CallbackInfoReturnable<Integer> cir){
		cir.cancel();
		cir.setReturnValue(APJFormulas.getExpForLevel(experienceLevel));
	}
	@Inject(method = "giveExperiencePoints", at = @At("HEAD"), cancellable = true)
	public void preventXpGainAbove30(int xp, CallbackInfo ci) {

		if (experienceLevel >= APJFormulas.MAX_PLAYER_LEVEL) {
			ci.cancel(); // Prevent further XP gain
		}

	}
}

