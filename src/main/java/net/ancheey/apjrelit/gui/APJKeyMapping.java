package net.ancheey.apjrelit.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.screen.TalentMenuScreen;
import net.ancheey.apjrelit.parties.LocalPlayerParty;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, value = Dist.CLIENT)
public class APJKeyMapping {
	public static final KeyMapping PARTY_ACCEPT = new KeyMapping(
			"key.apjrelit.accept_invite",
			KeyConflictContext.IN_GAME,
			KeyModifier.CONTROL,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_Y,
			"key.categories.apjrelit"
	);
	public static final KeyMapping PARTY_DECLINE = new KeyMapping(
			"key.apjrelit.decline_invite",
			KeyConflictContext.IN_GAME,
			KeyModifier.CONTROL,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_N,
			"key.categories.apjrelit"
	);
	public static final KeyMapping TALENT_MENU = new KeyMapping(
			"key.apjrelit.talent_menu",
			KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_N,
			"key.categories.apjrelit"
	);
	@SubscribeEvent
	public static void registerKeybinds(RegisterKeyMappingsEvent e) {
		e.register(PARTY_ACCEPT);
		e.register(PARTY_DECLINE);
		e.register(TALENT_MENU);
	}
	@SubscribeEvent
	public static void onKeybind(TickEvent.ClientTickEvent event){
		if(event.phase == TickEvent.Phase.END){
			if(PARTY_ACCEPT.consumeClick()){
				LocalPlayerParty.acceptCurrentInvite();
			}
			if(PARTY_DECLINE.consumeClick()){
				LocalPlayerParty.declineInvite();
			}
			else if(TALENT_MENU.consumeClick()){
				Minecraft.getInstance().setScreen(new TalentMenuScreen());
			}
		}
	}
}
