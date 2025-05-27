package net.ancheey.apjrelit.astral.cannons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class GeoCannonRenderer<T extends ShipCannonEntity> extends GeoEntityRenderer<T> {
	public GeoCannonRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
		super(renderManager, model);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.scale(1.3f,1.3f,1.3f);
		var v = (float) (entity.getVerticalRotation() * (Math.PI/2));
		var h = (float) (entity.getHorizontalRotation() * (Math.PI/2));
		var verticalRotor = model.getBone("VerticalRotor");
		verticalRotor.ifPresent(b->{
			b.setRotX(v);
		});
		var horizontalRotor = model.getBone("HorizontalRotor");
		horizontalRotor.ifPresent(b->{
			b.setRotY(h);
		});
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
	}
}
