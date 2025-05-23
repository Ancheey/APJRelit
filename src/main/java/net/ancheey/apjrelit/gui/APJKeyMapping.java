package net.ancheey.apjrelit.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class APJKeyMapping {
	public static final KeyMapping PARTY_MENU_KEY = new KeyMapping(
			"key.apjrelit.accept_invite",
			KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_Y,
			"key.categories.apjrelit"
	);
	@SubscribeEvent
	public static void registerKeybinds(RegisterKeyMappingsEvent e) {
		e.register(PARTY_MENU_KEY);
	}
}
