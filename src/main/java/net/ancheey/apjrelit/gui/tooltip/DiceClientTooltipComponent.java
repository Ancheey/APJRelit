package net.ancheey.apjrelit.gui.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class DiceClientTooltipComponent implements ClientTooltipComponent {
	private List<Integer> values = new ArrayList<>();
	private static final ResourceLocation DICE_ICON = ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID, "textures/gui/dice_icon.png");
	public DiceClientTooltipComponent(DiceTooltipComponent component) {
		if(component.D1 >0) values.add(component.D1+ component.D2 + component.D3 + component.D4 + component.D5);
		if(component.D2 >0) values.add(component.D2 + component.D3 + component.D4 + component.D5);
		if(component.D3 >0) values.add(component.D3 + component.D4 + component.D5);
		if(component.D4 >0) values.add(component.D4 + component.D5);
		if(component.D5 >0) values.add(component.D5);
	}
	@Override
	public int getHeight() {
		return 18;
	}

	@Override
	public int getWidth(Font pFont) {
		return values.size() * 18 - 2;
	}

	@Override
	public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
		RenderSystem.setShaderTexture(0,DICE_ICON);

		for (int i =0; i<values.size();i++) {
			String value = String.valueOf(values.get(i));
			int x = pX + 18 * i;
			pGuiGraphics.setColor(0.3f,0.3f,0.3f,0.1f);
			pGuiGraphics.blit(DICE_ICON,x,pY,0,0,16,16,16,16,16);
			pGuiGraphics.setColor(1,1,1,1);
			int twd = pFont.width(value);
			pGuiGraphics.drawString(pFont,value,x+8-twd/2,pY+5,i==0?0xFFFF0000:0xFFFFFFFF,true);
		}
	}
}
