package net.ancheey.apjrelit.gui;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.overlay.PlayerUnitBarOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class APJRelitUIHandler {
	public static final PlayerUnitBarOverlay HEALTH_BAR_OVERLAY = new PlayerUnitBarOverlay();

	@SubscribeEvent
	public static void registerCustomOverlays(RegisterGuiOverlaysEvent event){
		event.registerAbove(VanillaGuiOverlay.HOTBAR.id(),"health_bar",HEALTH_BAR_OVERLAY);
	}
}
