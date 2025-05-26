package net.ancheey.apjrelit.astral.cannons;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GeoCannonRenderer<T extends Entity & GeoAnimatable> extends GeoEntityRenderer<T> {
	public GeoCannonRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
		super(renderManager, model);
	}
}
