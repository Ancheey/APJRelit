package net.ancheey.apjrelit.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.io.Console;

@OnlyIn(Dist.CLIENT)
public class PlayerUnitBarOverlay implements IGuiOverlay {
	private final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/gui/icons.png");
	public int PlayerUnitBarXOffset = 0;
	public int PlayerUnitBarYOffset = 0;
	public float AnimationSpeed = 1f;
	private float partialTickStack = 0f;
	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		if(!gui.shouldDrawSurvivalElements())
			return;
		var mc = Minecraft.getInstance();
		if(!(mc.cameraEntity instanceof LivingEntity entity))
			return;
		partialTickStack+=(partialTick/(320*AnimationSpeed));
		partialTickStack = partialTickStack%1;
		APJGuiHelper.renderMajorUnitFrame(gui,guiGraphics,partialTickStack,screenWidth,screenHeight,PlayerUnitBarXOffset,PlayerUnitBarYOffset,entity);
	}

}
