package net.ancheey.apjrelit.gui.overlay;

import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.ancheey.apjrelit.parties.LocalPartyManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class PlayerPartyUnitBarsOverlay implements IGuiOverlay {
	public int XOffset = 0;
	public int YOffset = 0;
	public int YSpacing = 18;
	public int XSpacing = 66;
	public float AnimationSpeed = 1f;
	private float partialTickStack = 0f;
	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		partialTickStack+=(partialTick/(160*AnimationSpeed));
		partialTickStack = partialTickStack%1;
		int x = screenWidth/16;
		int y = screenHeight/4;
		//implement parties
		var players =  LocalPartyManager.getGroup().GetPlayers().asList();
		for(int i = 0; i < players.size(); i++){
			APJGuiHelper.renderMinorUnitFrame(gui,guiGraphics,partialTickStack, (int) (x+XOffset+(XSpacing*Math.floor(i/4d))),y+YOffset+(YSpacing*i),players.get(i));
		}
	}
}
