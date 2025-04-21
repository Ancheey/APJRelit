package net.ancheey.apjrelit.gui.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class TooltipImageComponent implements ClientTooltipComponent {
	private final int value;
	private static final ResourceLocation DICE_ICON = ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID, "textures/gui/dice_icon.png");
	public TooltipImageComponent(int value) {
		this.value = value;
	}
	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth(Font pFont) {
		return 0;
	}

	@Override
	public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
		RenderSystem.setShaderTexture(0,DICE_ICON);
		//RenderSystem.colo
		ClientTooltipComponent.super.renderImage(pFont, pX, pY, pGuiGraphics);

	}
}
