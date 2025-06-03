package net.ancheey.apjrelit.mixins;

import net.ancheey.apjrelit.enmity.EnmityManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(HurtByTargetGoal.class)
public class HurtByTargetGoalMixin {

	@Redirect(
			method = "start",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;getLastHurtByMob()Lnet/minecraft/world/entity/LivingEntity;"
			)
	)
	private LivingEntity redirectGetLastHurtMob(LivingEntity mob){

		var list = EnmityManager.getEntityData(mob);
		if(list != null){
			return list.getTopEnmityEntity();
		}
		return null;
	}
	@Redirect(
			method = "canUse",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/entity/LivingEntity;getLastHurtByMob()Lnet/minecraft/world/entity/LivingEntity;"
			)
	)
	private LivingEntity redirectGetLastHurtByMobForCanUse(LivingEntity mob) {
		var list = EnmityManager.getEntityData(mob);
		if(list != null){
			return list.getTopEnmityEntity();
		}
		return null;
	}


}
