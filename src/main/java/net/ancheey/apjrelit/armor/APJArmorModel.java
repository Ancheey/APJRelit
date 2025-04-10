package net.ancheey.apjrelit.armor;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class APJArmorModel extends GeoModel<APJArmorItem> {
	private final String armorName;
	public APJArmorModel(String ArmorName){
		armorName = ArmorName;
	}
	@Override
	public ResourceLocation getModelResource(APJArmorItem animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"geo/"+armorName+".geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(APJArmorItem animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/armor/"+armorName+".png");
	}

	@Override
	public ResourceLocation getAnimationResource(APJArmorItem animatable) {
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"animations/"+armorName+".animation.json");
	}
}
