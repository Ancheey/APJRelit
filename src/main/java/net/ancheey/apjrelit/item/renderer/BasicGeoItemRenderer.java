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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BasicGeoItemRenderer extends GeoItemRenderer<BasicGeoWeaponItem> {
	APJGeoItemProperties properties;
	public BasicGeoItemRenderer(APJGeoItemProperties properties) {
		super(new APJModel<>(properties.modelIdentifier,properties.textureFile));
		this.properties = properties;
	}
	@Override
	public void actuallyRender(PoseStack poseStack, BasicGeoWeaponItem animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,properties.red* red, properties.green*green, properties.blue*blue, properties.alpha*alpha);
	}
}
