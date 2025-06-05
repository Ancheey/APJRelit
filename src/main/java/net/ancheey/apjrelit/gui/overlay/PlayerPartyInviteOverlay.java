package net.ancheey.apjrelit.gui.overlay;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.ancheey.apjrelit.parties.LocalPlayerInvite;
import net.ancheey.apjrelit.parties.LocalPlayerParty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PlayerPartyInviteOverlay implements IGuiOverlay {
	private int currentAnimHeight = 16;

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		var invite = LocalPlayerParty.getInvite();
		if(invite == null && currentAnimHeight != 16){
			currentAnimHeight -= 1;
			APJGuiHelper.renderFrame(guiGraphics, 30,0,100,currentAnimHeight,false);
		}
		else if(invite != null){
			var pname = invite.getInviter() + " is inviting you to a party.";
			var pnamelen = Minecraft.getInstance().font.width(pname);
			var time = (int) (LocalPlayerInvite.VALID_TIME - (System.currentTimeMillis() - invite.getTimestamp()))/1000;
			APJGuiHelper.renderFrame(guiGraphics, 30,0, Math.max(32+pnamelen,120),currentAnimHeight,false);
			int maxAnimHeight = 43;
			if(currentAnimHeight < maxAnimHeight)
				currentAnimHeight++;
			else{
				guiGraphics.drawString(Minecraft.getInstance().font,pname,54,8,0xFFFFFFFF);
				guiGraphics.drawString(Minecraft.getInstance().font,"Ctrl+Y to accept",54,17,0xFF22FF22);
				guiGraphics.drawString(Minecraft.getInstance().font,"Ctrl+N to decline",54,26,0xFFFF2222);
				guiGraphics.drawString(Minecraft.getInstance().font,""+time,38,17,0xFFFFFFFF);
			}
		}
	}
}
