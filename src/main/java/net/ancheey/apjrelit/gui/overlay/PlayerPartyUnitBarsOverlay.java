package net.ancheey.apjrelit.gui.overlay;

import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.ancheey.apjrelit.parties.LocalPlayerGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
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
		if(!(Minecraft.getInstance().getCameraEntity() instanceof Player camplayer) || !LocalPlayerGroup.isInParty())
			return;

		partialTickStack+=(partialTick/(160*AnimationSpeed));
		partialTickStack = partialTickStack%1;
		int x = screenWidth/16;
		int y = screenHeight/4;
		//implement parties

		var players = new java.util.ArrayList<>(LocalPlayerGroup.getMembers());
		players.remove(camplayer);
		for(int i = 0; i < players.size(); i++){
			var player = players.get(i);
			APJGuiHelper.renderMinorUnitFrame(gui,guiGraphics,partialTickStack, (int) (x+XOffset+(XSpacing*Math.floor(i/4d))),y+YOffset+(YSpacing*i),player);
			if(LocalPlayerGroup.getLeader() == player){
				guiGraphics.blit(APJGuiHelper.ICONS,(int) (x+XOffset+(XSpacing*Math.floor(i/4d))),y+YOffset+(YSpacing*i),0,56,5,3);
			}
		}
	}
}
