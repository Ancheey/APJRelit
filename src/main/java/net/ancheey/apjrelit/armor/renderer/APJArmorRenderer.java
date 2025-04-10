package net.ancheey.apjrelit.armor.renderer;
import net.ancheey.apjrelit.armor.APJArmorItem;
import net.ancheey.apjrelit.armor.APJArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class APJArmorRenderer extends GeoArmorRenderer<APJArmorItem> {
	public APJArmorRenderer(String ArmorAssets) {
		super(new APJArmorModel(ArmorAssets));
		addRenderLayer(new AutoGlowingGeoLayer(this));
	}
}
