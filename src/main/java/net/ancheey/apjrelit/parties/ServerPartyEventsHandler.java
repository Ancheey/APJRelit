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
		var player = event.getEntity();
		var pt = ServerPartyManager.GetPlayerParty(player);
		if(pt != null)
			ServerPartyManager.forceRemovePlayer((ServerPlayer)player);
		ServerPartyManager.declineInvite((ServerPlayer) player);
		//make an actual way to insta rejoin
	}
	@SubscribeEvent
	public static void onEntityAttack(AttackEntityEvent e){
		if(e.getTarget() instanceof ServerPlayer player){
			var attacker = e.getEntity();
			if(ServerPartyManager.GetPlayerParty(attacker) == ServerPartyManager.GetPlayerParty(player) && ServerPartyManager.GetPlayerParty(attacker) != null)
				e.setCanceled(true); //no party pvp
		}
	}
}
