package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.ancheey.apjrelit.item.renderer.BasicGeoBowRenderer;
import net.ancheey.apjrelit.item.renderer.BasicGeoItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BasicGeoChargedProjectileWeapon extends APJProjectileWeaponItem implements GeoItem {
	APJGeoItemProperties geoProperties;
	private float nookSpeed;
	private int chargeTime = 0;
	private boolean isCharging = false;

	public BasicGeoChargedProjectileWeapon(String model, float damage, int distance, float nookSpeed) {
		super(damage, distance); //add charging percentage, max charge based on speed, tiers, attributes
		geoProperties = new APJGeoItemProperties(this,model, model);
		this.nookSpeed = nookSpeed;
	}
	private int getTierForCharge(Player player, int tiers){
		return (int)getChargePercentage(player)*tiers;
	}
	public float getNookSpeed(Player player){
		return (float)player.getAttributeValue(APJAttributeRegistry.NOOK_SPEED.get()) + nookSpeed;
	}
	public float getNookSpeed(){
		return nookSpeed;
	}
	public float getChargePercentage(Player player){
		return Math.min(chargeTime/(20/getNookSpeed(player)),1f);
	}
	public BasicGeoChargedProjectileWeapon SetTexture(String filename){
		geoProperties.textureFile = filename;
		return this;
	}
	public BasicGeoChargedProjectileWeapon SetColor(float a, float r, float g, float b){
		geoProperties.alpha = a;
		geoProperties.red = r;
		geoProperties.green = g;
		geoProperties.blue = b;
		return this;
	}
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		boolean flag = !pPlayer.getProjectile(itemstack).isEmpty();

		InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, flag);
		if (ret != null) return ret;

		if (!pPlayer.getAbilities().instabuild && !flag) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			pPlayer.startUsingItem(pHand);
			isCharging = true;
			return InteractionResultHolder.consume(itemstack);
		}
	}

	@Override
	public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
		if(isCharging)
			chargeTime++;
	}

	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
		isCharging = false;
		if (pEntityLiving instanceof Player player) {
			boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
			ItemStack itemstack = player.getProjectile(pStack);

			int i = this.getUseDuration(pStack) - pTimeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getPowerForTime(i);
				if (!((double)f < 0.1D)) {
					boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));
					shoot(f,pLevel,player);
					if (!flag1 && !player.getAbilities().instabuild) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							player.getInventory().removeItem(itemstack);
						}
					}

					player.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}

	@Override
	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return ARROW_ONLY;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return null;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private BasicGeoBowRenderer<BasicGeoChargedProjectileWeapon> renderer;
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				if(renderer == null){
					renderer = new BasicGeoBowRenderer<>(geoProperties);
				}
				return  renderer;
			}
		});
	}
}
