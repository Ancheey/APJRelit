package net.ancheey.apjrelit.projectiles;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.swing.text.html.parser.Entity;

public class HitscanProjectile {
	private float maxDistance;
	private float speed;
	private int color;
	private Vec3 pos;
	private Vec3 target;
	private Vec3 dir;
	private Level level;
	private float lastProgress = 0f;
	private SimpleParticleType type;

	public HitscanProjectile(LivingEntity e, SimpleParticleType type){
		dir = e.getLookAngle();
		pos = e.position().add(0,1.3,0);
		speed = 1f;
		maxDistance = 30f;
		target = pos.add(dir.scale(maxDistance));
		color = 0x55FFFF00;
		APJProjectileManager.RegisterProjectile(this);
		level = e.level();
		this.type = type;
	}
	public HitscanProjectile(Level level, Vec3 dir, Vec3 pos, SimpleParticleType type){
		this.dir = dir;
		this.pos = pos;
		speed = 1f;
		maxDistance = 30f;
		target = pos.add(dir.scale(maxDistance));
		color = 0x55FFFF00;
		APJProjectileManager.RegisterProjectile(this);
		this.level = level;
		this.type = type;
	}
	public float advance(){
		for(int i = 0; i < 10 && lastProgress < 1f; i++){
			Vec3 point =pos.lerp(target,lastProgress);
			lastProgress+= 0.01f;
			if(level instanceof ServerLevel sl){
				sl.sendParticles(type,point.x,point.y,point.z,1,0,0,0,0.1f);
			}
			//else
				//level.addParticle(type,point.x,point.y,point.z,0,0,0);
		}
		return lastProgress;
	}
}
