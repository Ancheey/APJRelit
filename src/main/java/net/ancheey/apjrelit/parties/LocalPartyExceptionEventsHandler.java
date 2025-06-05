package net.ancheey.apjrelit.parties;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, value = Dist.CLIENT)
public class LocalPartyExceptionEventsHandler {
	private static @Nullable UUID lastObserver;
	@SubscribeEvent
	public static void onTick(TickEvent event){
		var newObs = Minecraft.getInstance().getCameraEntity();
		if(newObs == null && lastObserver != null)
			lastObserver = null;
		else if(newObs != null  && lastObserver != newObs.getUUID()){
			APJRelitCore.LOGGER.info("Swapping: " + lastObserver + " for " + newObs.getUUID());
			lastObserver = newObs.getUUID();
			if(newObs instanceof Player player) {
				NetworkHandler.sendToServer(new CTSPartySyncBegPacket(player.getUUID()));
				APJRelitCore.LOGGER.info("Syncing for: " + player.getUUID());
			}
		}
	}
	@SubscribeEvent
	public  static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event){
		LocalPlayerParty.clear();
		LocalPlayerParty.InvalidateInvite();
	}
}
