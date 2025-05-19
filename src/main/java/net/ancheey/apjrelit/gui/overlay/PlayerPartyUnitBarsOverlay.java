package net.ancheey.apjrelit.gui.overlay;

import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PlayerPartyUnitBarsOverlay implements IGuiOverlay {
	public int XOffset = 0;
	public int YOffset = 0;
	public float AnimationSpeed = 1f;
	private float partialTickStack = 0f;
	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		partialTickStack+=(partialTick/(160*AnimationSpeed));
		partialTickStack = partialTickStack%1;
		var entity = Minecraft.getInstance().getCameraEntity();
		int x = screenWidth/16;
		int y = screenHeight/4;
		int yMul = 18;
		//implement parties
		for(int i = 0; i < 4; i++){
			APJGuiHelper.renderMinorUnitFrame(gui,guiGraphics,partialTickStack,x+XOffset,y+YOffset+yMul*i,(LivingEntity) entity);
		}
	}
}
