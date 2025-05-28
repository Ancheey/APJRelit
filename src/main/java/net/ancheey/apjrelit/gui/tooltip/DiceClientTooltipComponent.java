package net.ancheey.apjrelit.gui.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiceClientTooltipComponent implements ClientTooltipComponent {
	private List<Integer> values = new ArrayList<>();
	public DiceClientTooltipComponent(DiceTooltipComponent component) {
		if(component.D5 >0) values.add(component.D5);
		if(component.D4 >0) values.add(component.D4 + component.D5);
		if(component.D3 >0) values.add(component.D3 + component.D4 + component.D5);
		if(component.D2 >0) values.add(component.D2 + component.D3 + component.D4 + component.D5);
		if(component.D1 >0) values.add(component.D1+ component.D2 + component.D3 + component.D4 + component.D5);
		else if(values.size() == 0) values.add(1);
	}
	@Override
	public int getHeight() {
		return 30;
	}

	@Override
	public int getWidth(Font pFont) {
		return Math.max((values.size()+1) * 18 - 2,52);
	}

	@Override
	public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {

		for (int i =values.size()-1; i>=0;i--) {
			String value = String.valueOf(values.get(i));
			int x = (pX + 17 * (values.size()-1 - i))-2;
			pGuiGraphics.setColor(0.2f,0.2f,0.2f,0.1f);
			pGuiGraphics.blit(APJGuiHelper.TOOLTIP_ICONS,x,pY,0,0,16,16);
			pGuiGraphics.setColor(1,1,1,1);
			int twd = pFont.width(value);
			pGuiGraphics.drawString(pFont,value,x+8-twd/2,pY+5,i==values.size()-1?0xFFFF5f5f:(0xFFF0F0F0),true);
		}
		AtomicInteger avg = new AtomicInteger();
		values.forEach(avg::addAndGet);
		pGuiGraphics.setColor(0.2f,0.2f,0.2f,0.1f);
		pGuiGraphics.blit(APJGuiHelper.TOOLTIP_ICONS,(pX+17*values.size())-2,pY,0,0,16,16);
		pGuiGraphics.setColor(0.94f,0.94f,0.94f,1);
		pGuiGraphics.blit(APJGuiHelper.TOOLTIP_ICONS,(pX+17*values.size())+2,pY+4,0,16,8,8); //offseted by (4,4)
		pGuiGraphics.setColor(1,1,1,1);
		pGuiGraphics.drawString(pFont, String.format("%.2f",(((double)avg.get())/values.size()))+ " avg",pX,pY+18,0xFFFFFFFF,true);
	}
}
