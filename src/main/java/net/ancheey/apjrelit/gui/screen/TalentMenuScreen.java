package net.ancheey.apjrelit.gui.screen;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.talents.LocalTalentManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TalentMenuScreen extends Screen {
	ResourceLocation UI = ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/gui/talentsui.png");
	public TalentMenuScreen() {
		super(Component.literal("Talents"));
	}

	public int talentIndex = 0;
	@Override
	protected void init() {
		int centerX = this.width / 2;
		int centerY = this.height / 2;

		// Add a button
		//this.addRenderableWidget(Button.builder(Component.literal("Click Me"), button -> {
		//	// Do something when clicked
		//	Minecraft.getInstance().player.sendSystemMessage(Component.literal("Button Clicked!"));
		//}).bounds(centerX - 50, centerY - 10, 100, 20).build());
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

		if(pButton == 0 && rMouseover >=0 && lMouseover >=0 && !isMouseoverFlipped && isMouseoverFlippable){
			return LocalTalentManager.setTalent(talentIndex,rMouseover,lMouseover,true);
		}
		else if(pButton == 1 && rMouseover >=0 && lMouseover >=0 && isMouseoverFlipped){ //unchecking only in tests. otherwise will require big math with islands
			return LocalTalentManager.setTalent(talentIndex,rMouseover,lMouseover,false);
		}
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}
	int rMouseover = -1;
	int lMouseover = -1;
	boolean isMouseoverFlipped = false;
	boolean isMouseoverFlippable = false;
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics); // Gray bg
		guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
		rMouseover = -1;//reset mouseover
		lMouseover = -1;

		var gridsize = 6;
		var xPoint = this.width/2 - 16;
		var yPoint = (this.height/2 - gridsize * 18)+2;
		isMouseoverFlipped = false;
		isMouseoverFlippable = false;

		//Node grid generation
		for(int r = 0; r < gridsize; r++){
			for(int l = 0; l < gridsize; l++){
				var x = xPoint +(18*r - 18*l);
				var y = yPoint+((r+l)*18);
				var isFlipped = LocalTalentManager.getTalent(talentIndex,r,l);
				var isFlippable = LocalTalentManager.isAvailable(talentIndex,r,l);
				var isMouseOver = mouseX > x+7 && mouseX < x+25 && mouseY > y +7 && mouseY <  y+25;

				if(isFlipped){
					guiGraphics.blit(UI,x ,y,32,224,32,32);
				}
				else{
					if(isFlippable && LocalTalentManager.talentPoints > 0){
						if(isMouseOver)
							guiGraphics.blit(UI,x ,y,64,224,32,32);
						else
							guiGraphics.blit(UI,x ,y,96,224,32,32);
					}
					else {
						guiGraphics.blit(UI, x, y, 0, 224, 32, 32);
					}
				}
				if(isMouseOver) {
					guiGraphics.drawCenteredString(this.font, "Talent: [" + r + "," + l + "]", this.width / 4, this.height / 2, 0xFFFFFF);
					guiGraphics.drawCenteredString(this.font, "Flipped:" +isFlipped, this.width / 4, this.height / 2 - 10, 0xFFFFFF);
					guiGraphics.drawCenteredString(this.font, "Flippable:" +isFlippable, this.width / 4, this.height / 2 - 20, 0xFFFFFF);

					rMouseover = r;
					lMouseover = l;
					isMouseoverFlippable = isFlippable;
					isMouseoverFlipped = isFlipped;
				}
			}
		}

		//Points frame
		var pointFrameX = xPoint-8;
		var pointFrameY= (this.height/2+gridsize*18)+2;
		if(LocalTalentManager.talentPoints > 0) {
			guiGraphics.blit(UI, pointFrameX, pointFrameY, 176, 208, 48, 48);
			guiGraphics.drawCenteredString(this.font,"Unspent", pointFrameX+24, pointFrameY+8, 0x20FF00);
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(1.5f,1.5f,1);
			guiGraphics.drawCenteredString(this.font,""+LocalTalentManager.talentPoints, (int)((pointFrameX+25)/1.5f), (int)((pointFrameY+20)/1.5f), 0x20FF00);
			guiGraphics.pose().popPose();
		}
		else {
			guiGraphics.blit(UI, pointFrameX, pointFrameY, 128, 208, 48, 48);
			guiGraphics.drawCenteredString(this.font,"0", pointFrameX+25, pointFrameY+20, 0x333333);
		}

		//reset button
		guiGraphics.blit(UI,pointFrameX - 52,pointFrameY+16,0,176,48,16);
		guiGraphics.drawCenteredString(this.font, "Reset", pointFrameX - 28, pointFrameY+20, 0xFFFFFF);

		//save button
		guiGraphics.blit(UI,pointFrameX + 52,pointFrameY+16,0,176,48,16);
		guiGraphics.drawCenteredString(this.font, "Save", pointFrameX+76, pointFrameY+20, 0xFFFFFF);
		super.render(guiGraphics, mouseX, mouseY, partialTick);

		//Class Button L1
		guiGraphics.blit(UI, xPoint-44, yPoint-26, 128, 208, 48, 48);
		//Class Button L2
		guiGraphics.blit(UI, xPoint-80, yPoint+10, 128, 208, 48, 48);
		//Class Button L3
		guiGraphics.blit(UI, xPoint-116, yPoint+46, 128, 208, 48, 48);
		//Class Button R1
		guiGraphics.blit(UI, xPoint+28, yPoint-26, 128, 208, 48, 48);
		//Class Button R2
		guiGraphics.blit(UI, xPoint+64, yPoint+10, 128, 208, 48, 48);
		//Class ButtonR3
		guiGraphics.blit(UI, xPoint+100, yPoint+46, 128, 208, 48, 48);
	}
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
}
