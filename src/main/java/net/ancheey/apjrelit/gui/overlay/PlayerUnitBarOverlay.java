package net.ancheey.apjrelit.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class PlayerUnitBarOverlay implements IGuiOverlay {
	public int PlayerUnitBarXOffset = 0;
	public int PlayerUnitBarYOffset = 0;

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {

		if(!gui.shouldDrawSurvivalElements())
			return;
		var mc = Minecraft.getInstance();
		var x= screenWidth/4+PlayerUnitBarXOffset;
		var y = screenHeight-85+PlayerUnitBarYOffset;
		guiGraphics.drawString(mc.font,"HPbar",x,y, 0xFF000,false);//draw the healthbar
	}
}
