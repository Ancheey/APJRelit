package net.ancheey.apjrelit.item;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class APJGeoItemProperties {
	public AnimatableInstanceCache cache;
	public String modelIdentifier;
	public String textureFile;
	public float alpha = 1;
	public float red = 1;
	public float green = 1;
	public float blue = 1;
	public APJGeoItemProperties(GeoAnimatable item, String model, String texture){
		cache = GeckoLibUtil.createInstanceCache(item);
		modelIdentifier = model;
		textureFile = texture;
	}
}
