package net.ancheey.apjrelit.util;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;

public class APJModel <T extends GeoItem> extends GeoModel<T> {
	protected final String modelName;
	protected final String textureFile;
	protected final float alpha;
	protected final float red;
	protected final float green;
	protected final float blue;
	public APJModel(String modelName, String textureFile, float alpha, float red, float green, float blue){
		this.modelName = modelName;
		this.textureFile = textureFile;
		this.alpha = alpha;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	public APJModel(String modelName, String textureFile){
		this.modelName = modelName;
		this.textureFile = textureFile;
		this.alpha = 1;
		this.red = 1;
		this.green = 1;
		this.blue = 1;
	}
	public APJModel(String modelName){
		this.modelName = modelName;
		this.textureFile = modelName;
		this.alpha = 1;
		this.red = 1;
		this.green = 1;
		this.blue = 1;
	}
	@Override
	public ResourceLocation getModelResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"geo/"+modelName+".geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/item/"+textureFile+".png");
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"animations/"+modelName+".animation.json");
	}

	public float getAlpha() {
		return alpha;
	}

	public float getRed() {
		return red;
	}

	public float getGreen() {
		return green;
	}

	public float getBlue() {
		return blue;
	}
}
