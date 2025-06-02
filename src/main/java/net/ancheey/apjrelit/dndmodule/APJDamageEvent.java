package net.ancheey.apjrelit.dndmodule;

import io.redspace.ironsspellbooks.api.magic.IMagicManager;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.Random;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class APJDamageEvent {
	private static final Random rand = new Random();
	@SubscribeEvent(priority =  EventPriority.LOWEST)
	public void OnHurtEvent(LivingHurtEvent e){
	}
	@SubscribeEvent(priority =  EventPriority.HIGHEST)
	public void OnShieldBlock(ShieldBlockEvent ev){
		var e = ev.getEntity();
		ev.setShieldTakesDamage(false);
		ev.setBlockedDamage((float)e.getAttributeValue(APJAttributeRegistry.BLOCK_AMOUNT.get()));
	}
	@SubscribeEvent(priority =  EventPriority.LOWEST)
	public static void TestForCriticalEvent(CriticalHitEvent ev){
		var p =ev.getEntity();
		var t = ev.getTarget();
		var pcrit = AttributeHelper.ratingAtLevel(p.experienceLevel) * 5f + (float) p.getAttributeValue(APJAttributeRegistry.CRITICAL_STRIKE_RATING.get());
		var pcritmod = AttributeHelper.ratingAtLevel(p.experienceLevel) * 5f +(float)p.getAttributeValue(APJAttributeRegistry.CRITICAL_DAMAGE_RATING.get());
		if(t instanceof LivingEntity ent && ent.isBlocking()){
			if(ent.isBlocking()){
				ev.setResult(Event.Result.DENY);
				ev.setDamageModifier(1);
				return;
			}
			pcrit -= (float)p.getAttributeValue(APJAttributeRegistry.RESILIENCE.get());
		}
		var chance = Math.max(pcrit/AttributeHelper.ratingAtLevel(p.experienceLevel),0);
		if(rand.nextFloat()*100f < chance){
			ev.setResult(Event.Result.ALLOW);
			ev.setDamageModifier(Math.max(1f+(pcritmod/AttributeHelper.ratingAtLevel(p.experienceLevel)),1));
		}
		//e.setResult(Event.Result.DENY);
	}
}
