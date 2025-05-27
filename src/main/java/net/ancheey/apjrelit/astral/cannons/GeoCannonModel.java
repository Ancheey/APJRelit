package net.ancheey.apjrelit.astral.cannons;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class GeoCannonModel<T extends GeoAnimatable> extends GeoModel<T> {

	private final ResourceLocation cannonModel;
	private final ResourceLocation cannonTexture;
	private final ResourceLocation cannonAnimation;

	public GeoCannonModel(String cannonModel, String cannonTexture, String cannonAnimation) {
		this.cannonModel =  ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"geo/cannons/"+cannonModel+".geo.json");
		this.cannonTexture  =  ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/cannons/"+cannonTexture+".png");
		this.cannonAnimation =  ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"animations/cannons/"+cannonAnimation+".animation.json");
	}
	public GeoCannonModel(String model){
		this.cannonModel =  ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"geo/cannons/"+model+".geo.json");
		this.cannonTexture  =  ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/cannons/"+model+".png");
		this.cannonAnimation =  ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"animations/cannons/"+model+".animation.json");
	}



	@Override
	public ResourceLocation getModelResource(GeoAnimatable animatable) {
		return cannonModel;
	}

	@Override
	public ResourceLocation getTextureResource(GeoAnimatable animatable) {
		return cannonTexture;
	}

	@Override
	public ResourceLocation getAnimationResource(GeoAnimatable animatable) {
		return cannonAnimation;
	}
}
