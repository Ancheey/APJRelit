package net.ancheey.apjrelit.item.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.item.APJGeoItemProperties;
import net.ancheey.apjrelit.item.BasicGeoWeaponItem;
import net.ancheey.apjrelit.util.APJModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BasicGeoItemRenderer<T extends Item & GeoAnimatable & GeoItem> extends GeoItemRenderer<T> {
	APJGeoItemProperties properties;
	public BasicGeoItemRenderer(APJGeoItemProperties properties) {
		super(new APJModel<T>(properties.modelIdentifier,properties.textureFile));
		this.properties = properties;
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		var leftHand = transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
		poseStack.pushPose();
		if(Minecraft.getInstance().getCameraEntity() instanceof  Player player && (transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND )){
			boolean isBlocking =
					player.isUsingItem() &&
					player.getUseItem() == stack;
			if (isBlocking) {
				// Extra transform while blocking
				poseStack.translate(0.0F, 0.05, 0.0F); // Adjust as needed
				if(transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
					poseStack.mulPose(Axis.YP.rotationDegrees(-15)); // Tilt the shield
				else
					poseStack.mulPose(Axis.YP.rotationDegrees(15)); // Tilt the shield
			}
		}
		if(leftHand) {
			//poseStack.scale(-1, 1, 1f);
			poseStack.mulPose(Axis.YP.rotationDegrees(-180));
			poseStack.translate(-1f,0, -1f);
		}
		super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
		poseStack.popPose();
	}

	@Override
	public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

		super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,properties.red* red, properties.green*green, properties.blue*blue, properties.alpha*alpha);
	}
}
