package net.ancheey.apjrelit.gui.overlay;

import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.ancheey.apjrelit.parties.LocalPlayerGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class PlayerUnitBarOverlay implements IGuiOverlay {
	public int XOffset = 0;
	public int YOffset = 0;
	public float AnimationSpeed = 1f;
	private float partialTickStack = 0f;
	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		if(!gui.shouldDrawSurvivalElements())
			return;
		var mc = Minecraft.getInstance();
		if(!(mc.cameraEntity instanceof LivingEntity entity))
			return;
		partialTickStack+=(partialTick/(320*AnimationSpeed));
		partialTickStack = partialTickStack%1;
		APJGuiHelper.renderMajorUnitFrame(gui,guiGraphics,partialTickStack,screenWidth/4-48+XOffset,screenHeight-85-24+YOffset,entity);

		if(entity instanceof Player player && LocalPlayerGroup.isInParty()) {
			if (LocalPlayerGroup.getLeader() == player) {
				guiGraphics.blit(APJGuiHelper.ICONS, screenWidth/4-48+XOffset, screenHeight-85-24+YOffset, 0, 56, 5, 3);
			}
		}
	}

}
