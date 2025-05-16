package net.ancheey.apjrelit.item;

import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BasicGeoCurioItem extends Item implements GeoItem, ICurioItem {
	AnimatableInstanceCache cache;
	public BasicGeoCurioItem(Properties pProperties) {
		super(pProperties);
		cache = GeckoLibUtil.createInstanceCache(this);
	}
	public BasicGeoCurioItem() {
		super(new Item.Properties().durability(-1).stacksTo(1));
		cache = GeckoLibUtil.createInstanceCache(this);
	}


	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}
}
