package net.ancheey.apjrelit.gui.tooltip;

import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PowerClientTooltipComponent implements ClientTooltipComponent {
	private PowerTooltipComponent data;
	public PowerClientTooltipComponent(PowerTooltipComponent component) {
		data = component;
	}
	@Override
	public int getHeight() {
		return 17;
	}

	@Override
	public int getWidth(@NotNull Font pFont) {
		return 52;
	}

	@Override
	public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
		String text = ""+data.D1;
		int twd = pFont.width(text);
		int x = pX - 2;
		//int x = (pX + 17 * (values.size()-1 - i))-2;
		pGuiGraphics.setColor(0.2f, 0.2f, 0.2f, 0.1f);
		pGuiGraphics.blit(APJGuiHelper.TOOLTIP_ICONS, x, pY, 16, 0, 16, 16);
		pGuiGraphics.setColor(1, 1, 1, 1);
		pGuiGraphics.drawString(pFont, text, x + 8 - twd / 2, pY + 5, 0xFFFF5f5f, true);
		x+=17;
		if(data.D2>0) {
			text = ""+data.D2;
			twd = pFont.width(text);
			pGuiGraphics.setColor(0.2f, 0.2f, 0.2f, 0.1f);
			pGuiGraphics.blit(APJGuiHelper.TOOLTIP_ICONS, x, pY, 32, 0, 16, 16);
			pGuiGraphics.setColor(1, 1, 1, 1);
			pGuiGraphics.drawString(pFont, text, x + 8 - twd / 2, pY + 5, 0xFFFFFF5f, true);
			x += 17;
		}
		if(data.D3>0) {
			text = ""+data.D3;
			twd = pFont.width(text);
			pGuiGraphics.setColor(0.2f, 0.2f, 0.2f, 0.1f);
			pGuiGraphics.blit(APJGuiHelper.TOOLTIP_ICONS, x, pY, 48, 0, 16, 16);
			pGuiGraphics.setColor(1, 1, 1, 1);
			pGuiGraphics.drawString(pFont, text, x + 8 - twd / 2, pY + 5, 0xFFF0F0F0, true);
			x += 17;
		}
		pGuiGraphics.setColor(0.2f,0.2f,0.2f,0.1f);
		pGuiGraphics.blit(APJGuiHelper.TOOLTIP_ICONS,x,pY,0,0,16,16);
		pGuiGraphics.setColor(0.94f,0.94f,0.94f,1);
		pGuiGraphics.blit(APJGuiHelper.TOOLTIP_ICONS,(x)+4,pY+4,0,16,8,8); //offseted by (4,4)
	}
}
