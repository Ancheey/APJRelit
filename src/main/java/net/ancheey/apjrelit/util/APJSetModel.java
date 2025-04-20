package net.ancheey.apjrelit.util;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import javax.annotation.Nullable;
import java.util.Optional;

public class APJSetModel<T extends GeoItem> extends APJModel<T> {
	public APJSetModel(String ArmorName){
		super(ArmorName);
	}
	public APJSetModel(String ArmorName, String textureSuffix){
		super(ArmorName, textureSuffix);
	}
	@Override
	public ResourceLocation getModelResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"geo/"+modelName+".geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/armor/"+modelName+textureSuffix+".png");
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"animations/"+modelName+".animation.json");
	}
	public Optional<GeoBone> getHead(){
		return this.getBone("armorHead");
	}
	@Nullable
	public GeoBone Head(){
		return this.getBone("armorHead").orElse(null);
	}
	public Optional<GeoBone> getHalo(){
		return this.getBone("armorHeadHalo");
	}
	public Optional<GeoBone> getBody(){
		return this.getBone("armorBody");
	}
	public Optional<GeoBone> getCape(){
		return this.getBone("armorBodyCloak");
	}
	public Optional<GeoBone> getLeftArm(){
		return this.getBone("armorLeftArm");
	}
	public Optional<GeoBone> getLeftShoulder(){
		return this.getBone("armorLeftShoulder");
	}
	public Optional<GeoBone> getLeftGlove(){
		return this.getBone("armorLeftGlove");
	}
	public Optional<GeoBone> getRightArm(){
		return this.getBone("armorRightArm");
	}
	public Optional<GeoBone> getRightShoulder(){
		return this.getBone("armorRightShoulder");
	}
	public Optional<GeoBone> getRightGlove(){
		return this.getBone("armorRightGlove");
	}
	public Optional<GeoBone> getLeftLeg(){
		return this.getBone("armorLeftLeg");
	}
	public Optional<GeoBone> getLeftBoot(){
		return this.getBone("armorLeftBoot");
	}
	public Optional<GeoBone> getRightLeg(){
		return this.getBone("armorRightLeg");
	}
	public Optional<GeoBone> getRightBoot(){
		return this.getBone("armorRightBoot");
	}





}
