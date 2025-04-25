package net.ancheey.apjrelit.item;

import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.item.renderer.BasicGeoItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.util.function.Consumer;

public class BasicGeoWeaponItem extends SwordItem implements GeoItem {
	AnimatableInstanceCache cache;
	String modelIdentifier;
	public BasicGeoWeaponItem(String modelIdentifier, Properties pProperties) {
		super(Tiers.NETHERITE,1,-3f,pProperties);
		cache = GeckoLibUtil.createInstanceCache(this);
		this.modelIdentifier = modelIdentifier;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
		return super.getDefaultAttributeModifiers(pEquipmentSlot);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}

	@Override
	public double getTick(Object itemStack) {
		return RenderUtils.getCurrentTick();
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private BasicGeoItemRenderer renderer;
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				if(renderer == null)
					renderer = new BasicGeoItemRenderer(modelIdentifier);
				return  renderer;
			}
		});
	}
}
