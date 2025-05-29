package net.ancheey.apjrelit.projectiles;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;

public class TargettingUtil {
	public static @Nullable Entity raycastTarget(LivingEntity e, float range){
		Vec3 eyePos = e.getEyePosition(1.0F);
		Vec3 lookVec = e.getLookAngle();
		Vec3 endVec = eyePos.add(lookVec.scale(range));

		AABB box = e.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0); // widen box slightly

		EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
				e.level(), e, eyePos, endVec, box,
				(entity) -> !entity.isSpectator() && entity.isPickable() && entity instanceof LivingEntity && entity != e && !e.isAlliedTo(entity) && entity.canBeHitByProjectile()
		);
		if(hitResult != null) {
			APJRelitCore.LOGGER.info("Blocking: " + canSeePosition(e.level(), e, hitResult.getLocation()) + " hitting: " + hitResult.getLocation());
			var distance = hitResult.getEntity().distanceTo(e);
			var endpos = eyePos.add(lookVec.scale(distance));
			return canSeePosition(e.level(),e,endpos)? hitResult.getEntity() : null;
		}
		return null;


	}
	public static boolean canSeePosition(Level level, LivingEntity A, Vec3 hitPos){
		Vec3 start = A.getEyePosition(1.0f);
		ClipContext context = new ClipContext(
				start,
				hitPos,
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE,
				A
		);

		BlockHitResult result = level.clip(context);
		// If the ray hit something before the target, there's no clear line of sight
		return result.getType() == HitResult.Type.MISS;
	}
}
