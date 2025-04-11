package net.ancheey.apjrelit.armor.renderer;
import net.ancheey.apjrelit.armor.APJArmorItem;
import net.ancheey.apjrelit.armor.APJArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class APJArmorRenderer extends GeoArmorRenderer<APJArmorItem> {
	public APJArmorRenderer(String ArmorAssets) {
		super(new APJArmorModel(ArmorAssets));
		addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}
}
