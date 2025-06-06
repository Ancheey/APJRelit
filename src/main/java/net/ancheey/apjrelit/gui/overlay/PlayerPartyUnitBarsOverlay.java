package net.ancheey.apjrelit.gui.overlay;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.ancheey.apjrelit.parties.LocalPlayerParty;
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
		if(!(Minecraft.getInstance().getCameraEntity() instanceof Player camplayer) || !LocalPlayerParty.isInParty())
			return;

		partialTickStack+=(partialTick/(160*AnimationSpeed));
		partialTickStack = partialTickStack%1;
		int x = screenWidth/16;
		int y = screenHeight/4;
		//implement parties

		var players = new java.util.ArrayList<>(LocalPlayerParty.getLevelMembers());
		var away = LocalPlayerParty.AwayPlayers;
		players.remove(camplayer);
		int i = 0;
		while(i < players.size()){
			var player = players.get(i);
			APJGuiHelper.renderMinorUnitFrame(gui,guiGraphics,partialTickStack, (int) (x+XOffset+(XSpacing*Math.floor(i/4d))),y+YOffset+(YSpacing*i),player);
			if(LocalPlayerParty.getLeader() == player){
				guiGraphics.blit(APJGuiHelper.ICONS,(int) (x+XOffset+(XSpacing*Math.floor(i/4d)))+2,y+YOffset+(YSpacing*i),0,56,5,3);
			}
			i++;
		}
		for(int j = i; j < i+away;j++){
			APJGuiHelper.renderMinorUnitFrameAwayPlayer(gui,guiGraphics,partialTickStack, (int) (x+XOffset+(XSpacing*Math.floor(j/4d))),y+YOffset+(YSpacing*j));
		}
	}
}
