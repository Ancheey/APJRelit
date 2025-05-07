package net.ancheey.apjrelit.dndmodule;

import io.redspace.ironsspellbooks.api.magic.IMagicManager;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.ancheey.apjrelit.APJRelitCore;
import net.bettercombat.api.EntityPlayer_BetterCombat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Field;

public class APJDamageEvent {
	public static final ResourceKey<DamageType> OFFHAND_ATTACK = ResourceKey.create(Registries.DAMAGE_TYPE,ResourceLocation.fromNamespaceAndPath("bettercombat", "offhand_attack"));

	//@SubscribeEvent(priority =  EventPriority.LOWEST)
	public void OnHurtEvent(LivingAttackEvent e){
		if(e.getSource() instanceof SpellDamageSource sds){
			APJRelitCore.LOGGER.info("Spell damage");
		}

		else if(e.getSource().getEntity() instanceof EntityPlayer_BetterCombat bcplayer && bcplayer instanceof Player player){
			APJRelitCore.LOGGER.info(String.valueOf(bcplayer.getCurrentAttack().isOffHand()));
			//e.setCanceled(true);
		}
	}
	public void TestForCriticalEvent(CriticalHitEvent e){
	}
}
