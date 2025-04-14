package net.ancheey.apjrelit.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class APJKeyMapping {
	public static final KeyMapping PARTY_MENU_KEY = new KeyMapping(
			"key.apjrelit.party_menu",
			KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_P,
			"key.categories.apjrelit"
	);
	public static void registerKeybinds(RegisterKeyMappingsEvent e) {
		e.register(PARTY_MENU_KEY);
	}
}
