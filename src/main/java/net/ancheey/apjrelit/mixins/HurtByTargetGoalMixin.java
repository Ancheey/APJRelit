package net.ancheey.apjrelit.mixins;

import net.ancheey.apjrelit.enmity.EnmityManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(HurtByTargetGoal.class)
public abstract class HurtByTargetGoalMixin extends HurtByTargetGoal {



	@Redirect(
			method = "start",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;setTarget(Lnet/minecraft/world/entity/LivingEntity;)V"
			)
	)
	public LivingEntity redirectGetLastHurtMob(LivingEntity mob, LivingEntity original){
		var list = EnmityManager.getEntityData(mob);
		if(list != null){
			return list.getTopEnmityEntity();
		}
		return null;
	}
	/*
	@ModifyVariable(
			method = "canUse",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;getLastHurtByMob()Lnet/minecraft/world/entity/LivingEntity;"
			),
			index = 1, // This might need adjusting depending on local variable ordering
			ordinal = 0
	)
	public LivingEntity redirectGetLastHurtByMobForCanUse(LivingEntity original) {
		var list = EnmityManager.getEntityData(mob);
		if(list != null){
			return list.getTopEnmityEntity();
		}
		return original;
	}

*/
	@Shadow
	private int timestamp;
	@Shadow
	private final Class<?>[] toIgnoreDamage;
	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	private void injectCustomCanUse(CallbackInfoReturnable<Boolean> cir) {
		int i = this.mob.getLastHurtByMobTimestamp();
		LivingEntity livingentity = this.mob.getLastHurtByMob();
		if (i != this.timestamp && livingentity != null) {
			if (livingentity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
				cir.setReturnValue(false);
			} else {
				var enmity = EnmityManager.getEntityData(mob);
				if(enmity == null || enmity.size() <2){ //No enmity list (probably hit by a mob) or a single player is fighting it
					cir.setReturnValue(isNotIgnored(livingentity.getClass()) && canAttack(livingentity, HURT_BY_TARGETING));
				}
				else {
					var iterator = enmity.getIterator();
					cir.setReturnValue(false);
					while(iterator.hasNext()){
						var o = iterator.next();
						if(canAttack(o.entity, HURT_BY_TARGETING) && isNotIgnored(o.entity.getClass())){
							cir.setReturnValue(true);
							return;
						}
					}
				}
			}
		}
		else
			cir.setReturnValue(false); // override result
	}
	private boolean isNotIgnored(Class<?> type){
		for(Class<?> oclass : this.toIgnoreDamage) {
			if (oclass.isAssignableFrom(type)) {
				return false;
			}
		}
		return true;
	}
	public HurtByTargetGoalMixin(PathfinderMob pMob, Class<?>... pToIgnoreDamage) {
		super(pMob, pToIgnoreDamage);
		this.toIgnoreDamage = pToIgnoreDamage;
	}

}
