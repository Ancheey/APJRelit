package net.ancheey.apjrelit.gui;

import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.overlay.PlayerUnitBarOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class APJVanillaGuiHandler {
	private static final Minecraft minecraft = Minecraft.getInstance();

	private static final ResourceLocation vanillaHealthBar = ResourceLocation.fromNamespaceAndPath("minecraft", "player_health");
	private static final ResourceLocation vanillaArmorBar = VanillaGuiOverlay.ARMOR_LEVEL.id();
	@SubscribeEvent
	public static void disableDefaultHealthBar(RenderGuiOverlayEvent.Pre event){
		final ForgeGui gui = ((ForgeGui) minecraft.gui);
		if (!event.isCanceled()
				&& (event.getOverlay().id().equals(vanillaHealthBar) ||event.getOverlay().id().equals(vanillaArmorBar))
				&& !minecraft.options.hideGui
				&& gui.shouldDrawSurvivalElements()
				&& minecraft.cameraEntity instanceof Player player) {
			event.setCanceled(true);
		}
	}
}
