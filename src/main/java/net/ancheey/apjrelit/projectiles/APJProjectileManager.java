package net.ancheey.apjrelit.projectiles;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class APJProjectileManager {
	private static final List<HitscanProjectile> projectileQueue = new CopyOnWriteArrayList<>();
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onTick(TickEvent event){
		if(projectileQueue.size()==0)
			return;
		List<HitscanProjectile> copy;
		synchronized (projectileQueue){
			copy = new ArrayList<>(projectileQueue);
			projectileQueue.clear();
		}
		for(var proj : copy){
			if(proj.advance()<1f){
				projectileQueue.add(proj);
			}
		}
	}
	public static void RegisterProjectile(HitscanProjectile projectile){
		projectileQueue.add(projectile);
	}
}
