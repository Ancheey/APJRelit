package net.ancheey.apjrelit.util;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;

public class APJModel <T extends GeoItem> extends GeoModel<T> {
	private final String modelName;
	public APJModel(String modelName){
		this.modelName = modelName;
	}
	@Override
	public ResourceLocation getModelResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"geo/"+modelName+".geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/item/"+modelName+".png");
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"animations/"+modelName+".animation.json");
	}
}
