package net.ancheey.apjrelit.item.renderer;

import net.ancheey.apjrelit.item.BasicGeoWeaponItem;
import net.ancheey.apjrelit.util.APJModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BasicGeoItemRenderer extends GeoItemRenderer<BasicGeoWeaponItem> {
	public BasicGeoItemRenderer(String modelName) {
		super(new APJModel<>(modelName));
	}
}
