package net.ancheey.apjrelit.gui.overlay;

import net.ancheey.apjrelit.gui.APJGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import javax.annotation.Nullable;
@OnlyIn(Dist.CLIENT)
public class TargetUnitBarOverlay implements IGuiOverlay  {
	public int XOffset = 0;
	public int YOffset = 0;
	public float AnimationSpeed = 1f;
	private float partialTickStack = 0f;
	public float targetRange = 6f;
	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		if(!gui.shouldDrawSurvivalElements())
			return;
		var mc = Minecraft.getInstance();
		if(!(mc.cameraEntity instanceof LivingEntity looker))
			return;
		if(!(raycastTarget(looker) instanceof LivingEntity entity))
			return;
		partialTickStack+=(partialTick/(320*AnimationSpeed));
		partialTickStack = partialTickStack%1;
		APJGuiHelper.renderMajorUnitFrame(gui,guiGraphics,partialTickStack,(screenWidth/4)*3-80+XOffset,screenHeight-85-24+YOffset,entity,true);
	}
	private @Nullable Entity raycastTarget(LivingEntity e){
		Vec3 eyePos = e.getEyePosition(1.0F);
		Vec3 lookVec = e.getViewVector(1.0F);
		Vec3 endVec = eyePos.add(lookVec.scale(targetRange));

		AABB box = e.getBoundingBox().expandTowards(lookVec.scale(6f)).inflate(1.0); // widen box slightly

		EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
				e.level(), e, eyePos, endVec, box,
				(entity) -> !entity.isSpectator() && entity.isPickable() && entity instanceof LivingEntity && entity != e
		);

		return hitResult != null ? hitResult.getEntity() : null;
	}
}
