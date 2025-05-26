package net.ancheey.apjrelit.projectiles;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class APJProjectileManager {
	private static List<HitscanProjectile> projectiles = new ArrayList<>();
	@SubscribeEvent
	public static void onTick(TickEvent event){
		for(int i = 0; i < projectiles.size(); i++){
			if(projectiles.get(i).advance()>1f){
				projectiles.remove(i);
				i--;
			}
		}
		projectiles.forEach(HitscanProjectile::advance);
	}
	public static void RegisterProjectile(HitscanProjectile projectile){
		projectiles.add(projectile);
	}
}
