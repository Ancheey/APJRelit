package net.ancheey.apjrelit.item;

import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.item.renderer.BasicGeoItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.util.function.Consumer;

public class BasicGeoWeaponItem extends APJSwordItem implements GeoItem {
	APJGeoItemProperties geoProperties;
	public BasicGeoWeaponItem(String modelIdentifier, float swingsPerSecond, Rarity rarity) {
		super(swingsPerSecond,rarity);
		geoProperties = new APJGeoItemProperties(this, modelIdentifier, modelIdentifier);
	}

	public BasicGeoWeaponItem SetTexture(String filename){
		geoProperties.textureFile = filename;
		return this;
	}
	public BasicGeoWeaponItem SetColor(float a, float r, float g, float b){
		geoProperties.alpha = a;
		geoProperties.red = r;
		geoProperties.green = g;
		geoProperties.blue = b;
		return this;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoProperties.cache;
	}

	@Override
	public double getTick(Object itemStack) {
		return RenderUtils.getCurrentTick();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private BasicGeoItemRenderer renderer;
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				if(renderer == null){
						renderer = new BasicGeoItemRenderer(geoProperties);
				}
				return  renderer;
			}
		});
	}
}
