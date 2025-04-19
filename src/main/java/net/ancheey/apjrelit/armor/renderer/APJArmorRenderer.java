package net.ancheey.apjrelit.armor.renderer;
import net.ancheey.apjrelit.armor.APJArmorItem;
import net.ancheey.apjrelit.util.APJSetModel;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import javax.annotation.Nullable;

public class APJArmorRenderer extends GeoArmorRenderer<APJArmorItem> {
	public APJArmorRenderer(String ArmorAssets) {
		super(new APJSetModel(ArmorAssets));
	}

	protected GeoBone halo = null;
	protected GeoBone rightShoulder = null;
	protected GeoBone leftShoulder = null;
	protected GeoBone rightGlove = null;
	protected GeoBone leftGlove = null;
	protected GeoBone cloak = null;

	@Nullable
	public GeoBone getHalo() {
		return this.model.getBone("armorHeadHalo").orElse(null);
	}
	@Nullable
	public GeoBone getRightShoulder() {
		return this.model.getBone("armorRightShoulder").orElse(null);
	}
	@Nullable
	public GeoBone getLeftShoulder() {
		return this.model.getBone("armorLeftShoulder").orElse(null);
	}
	@Nullable
	public GeoBone getRightGlove() {
		return this.model.getBone("armorRightGlove").orElse(null);
	}
	@Nullable
	public GeoBone getLeftGlove() {
		return this.model.getBone("armorLeftGlove").orElse(null);
	}
	@Nullable
	public GeoBone getCloak() {
		return this.model.getBone("armorBodyCloak").orElse(null);
	}

	@Override
	protected void grabRelevantBones(BakedGeoModel bakedModel) {
		super.grabRelevantBones(bakedModel);
		this.halo = getHalo();
		this.rightShoulder = getRightShoulder();
		this.leftShoulder = getLeftShoulder();
		this.rightGlove = getRightGlove();
		this.leftGlove = getLeftGlove();
		this.cloak = getCloak();
	}

	@Override
	public void setAllVisible(boolean pVisible) {
		super.setAllVisible(pVisible);
		setBoneVisible(halo,pVisible);
		setBoneVisible(rightShoulder,pVisible);
		setBoneVisible(leftShoulder,pVisible);
		setBoneVisible(rightGlove,pVisible);
		setBoneVisible(leftGlove,pVisible);
		setBoneVisible(cloak,pVisible);
	}
}
