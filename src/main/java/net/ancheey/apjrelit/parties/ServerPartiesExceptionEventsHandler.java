package net.ancheey.apjrelit.parties;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ServerPartiesExceptionEventsHandler {
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event){
		var player = event.getEntity();
		var pt = ServerPartyManager.GetPlayerParty(player);
		if(pt != null)
			ServerPartyManager.forceRemovePlayer((ServerPlayer)player);
		//make an actual way to insta rejoin
	}
}
