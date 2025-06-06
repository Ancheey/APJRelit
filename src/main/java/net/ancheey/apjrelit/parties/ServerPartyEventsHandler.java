package net.ancheey.apjrelit.parties;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, value = Dist.DEDICATED_SERVER)
public class ServerPartyEventsHandler {
	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event){
		var player =  (ServerPlayer)event.getEntity();
		var pt = ServerPartyManager.GetPlayerParty(player);
		if(pt != null)
			ServerPartyManager.forceRemovePlayer(player);
		ServerPartyManager.declineInvite(player);
		//make an actual way to insta rejoin -- will require entity swapping
	}
	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event){
		var player =  (ServerPlayer)event.getEntity();
		var pt = ServerPartyManager.GetPlayerParty(player);
		if(pt != null)
			pt.sync();
	}
	public static void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event){
		//swap entities for a new one based on UUID
		var entity = (ServerPlayer)event.getEntity();
		ServerPartyManager.replaceEntity(entity);
	}
	@SubscribeEvent
	public static void onEntityAttack(AttackEntityEvent e){
		if(e.getTarget() instanceof ServerPlayer player){
			var attacker = (ServerPlayer) e.getEntity();
			if(ServerPartyManager.GetPlayerParty(attacker) == ServerPartyManager.GetPlayerParty(player) && ServerPartyManager.GetPlayerParty(attacker) != null)
				e.setCanceled(true); //no party pvp
		}
	}
}
