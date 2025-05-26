package net.ancheey.apjrelit.dndmodule;

import io.redspace.ironsspellbooks.api.magic.IMagicManager;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.bettercombat.api.EntityPlayer_BetterCombat;
import net.bettercombat.logic.PlayerAttackHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.Random;

public class APJDamageEvent {
	private static final Random rand = new Random();
	@SubscribeEvent(priority =  EventPriority.LOWEST)
	public void OnHurtEvent(LivingHurtEvent e){
		if(e.getSource().getEntity() instanceof Projectile projectile && e.getSource().getDirectEntity() instanceof LivingEntity entity){

		}
		//if(e.getSource() instanceof SpellDamageSource sds){
		//	APJRelitCore.LOGGER.info(sds.spell().getSchoolType().toString());
		//}
		//else{
		//	APJRelitCore.LOGGER.info(e.getSource().type().toString());
			//make stuff like poison know who's done it
		//}
		/*if(e.getSource().getDirectEntity() instanceof Player player && e.getAmount() > 0){
			APJRelitCore.LOGGER.info("Dice: 1d" + player.getAttributeValue(APJAttributeRegistry.ATTACK_DAMAGE_DICE.get())+" + "+e.getAmount());
			//e.setAmount((float)(player.getAttributeValue(APJAttributeRegistry.ATTACK_DAMAGE_DICE.get()) + e.getAmount()));
			var dice = player.getAttributeValue(APJAttributeRegistry.ATTACK_DAMAGE_DICE.get());
			e.setAmount(e.getAmount() + (rand.nextInt(0,(int)dice+1)));
		}

		else if(e.getSource().getEntity() instanceof EntityPlayer_BetterCombat bcplayer && bcplayer instanceof Player player){
			APJRelitCore.LOGGER.info(String.valueOf(bcplayer.getCurrentAttack().isOffHand()));
			//e.setCanceled(true);
		}*/
	}
	@SubscribeEvent(priority =  EventPriority.LOWEST)
	public void TestForCriticalEvent(CriticalHitEvent e){
		//e.setResult(Event.Result.DENY);
	}
	public void ArrowLooseEvent(ArrowLooseEvent e){
		var damage = DamageHelper.getRandomisedItemDamage(e.getEntity(),e.getBow());
	}
}
