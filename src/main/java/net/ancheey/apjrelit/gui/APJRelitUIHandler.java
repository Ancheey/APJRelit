package net.ancheey.apjrelit.gui;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.overlay.PlayerPartyUnitBarsOverlay;
import net.ancheey.apjrelit.gui.overlay.PlayerUnitBarOverlay;
import net.ancheey.apjrelit.gui.overlay.TargetUnitBarOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class APJRelitUIHandler {
	public static final PlayerUnitBarOverlay OBSERVER_UNIT_FRAME = new PlayerUnitBarOverlay();
	public static final TargetUnitBarOverlay TARGET_UNIT_FRAME = new TargetUnitBarOverlay();
	public static final PlayerPartyUnitBarsOverlay PARTY_UNIT_FRAMES = new PlayerPartyUnitBarsOverlay();

	@SubscribeEvent
	public static void registerCustomOverlays(RegisterGuiOverlaysEvent event){
		event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(),"observer_unit_frame",OBSERVER_UNIT_FRAME);
		event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(),"target_unit_frame",TARGET_UNIT_FRAME);
		event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(),"party_unit_frames",PARTY_UNIT_FRAMES);
	}
}
