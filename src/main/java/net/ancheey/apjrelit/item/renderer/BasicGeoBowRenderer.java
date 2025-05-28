package net.ancheey.apjrelit.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ancheey.apjrelit.item.APJGeoItemProperties;
import net.ancheey.apjrelit.item.BasicGeoChargedProjectileWeapon;
import net.ancheey.apjrelit.item.BasicGeoWeaponItem;
import net.ancheey.apjrelit.util.APJModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@OnlyIn(Dist.CLIENT)
public class BasicGeoBowRenderer<T extends BasicGeoChargedProjectileWeapon> extends GeoItemRenderer<T> {
	APJGeoItemProperties properties;
	public BasicGeoBowRenderer(APJGeoItemProperties properties) {
		super(new APJModel<>(properties.modelIdentifier,properties.textureFile));
		this.properties = properties;
	}
	@Override
	public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,properties.red* red, properties.green*green, properties.blue*blue, properties.alpha*alpha);
	}
}
