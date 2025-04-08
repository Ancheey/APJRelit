package net.ancheey.apjrelit.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CurioRenderer implements ICurioRenderer {
	private final  EntityModel<Entity> model;
	private final ResourceLocation texture;

	public CurioRenderer(EntityModel<Entity> model, ResourceLocation texture) {
		this.model = model;
		this.texture = texture;
	}

	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
																		  int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> parent)) {
			return;
		}
		if(model instanceof IArmsModel shoulders){
			//left arm
			matrixStack.pushPose();
			parent.leftArm.translateAndRotate(matrixStack);
			matrixStack.translate(-0.33f,-0.13f,0);
			shoulders.renderLeftToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityCutout(texture)),light, OverlayTexture.NO_OVERLAY,1,1,1,1);
			matrixStack.popPose();
			//right arm
			matrixStack.pushPose();
			parent.rightArm.translateAndRotate(matrixStack);
			matrixStack.translate(0.33f,-0.13f,0);
			shoulders.renderRightToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityCutout(texture)),light, OverlayTexture.NO_OVERLAY,1,1,1,1);
			matrixStack.popPose();
		}
		//model.setupAnim(slotContext.entity(), limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch);
		//model.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityCutout(texture)),light, OverlayTexture.NO_OVERLAY,1,1,1,1);
		//matrixStack.popPose();
	}
}
