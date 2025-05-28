package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.ancheey.apjrelit.item.renderer.BasicGeoBowRenderer;
import net.ancheey.apjrelit.item.renderer.BasicGeoItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Properties;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BasicGeoChargedProjectileWeapon extends APJProjectileWeaponItem implements GeoItem {
	APJGeoItemProperties geoProperties;
	private float nookSpeed;
	private int chargeTime = 0;
	private boolean isCharging = false;

	public BasicGeoChargedProjectileWeapon(String model, int distance, float nookSpeed) {
		super(distance); //add charging percentage, max charge based on speed, tiers, attributes
		geoProperties = new APJGeoItemProperties(this,model, model);
		this.nookSpeed = nookSpeed;
	}
	private int getTierForCharge(Player player, int tiers){
		return(int)Math.ceil(getChargePercentage(player)*tiers);
	}
	public float getNookSpeed(Player player){
		return (float)player.getAttributeValue(APJAttributeRegistry.NOOK_SPEED.get()) + nookSpeed-1;
	}
	public float getNookSpeed(){
		return nookSpeed;
	}
	public float getChargePercentage(Player player){
		return Math.min(((float)chargeTime)/(20/getNookSpeed(player)),1f);
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
				float f = getChargePercentage(player);
				float d = getChargedDamage(player);
				if (!((double)f < 0.1D) && d > 0) {
					boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));
					shoot(f,d,pLevel,player);
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
		isCharging = false;
		chargeTime = 0;
	}
	@Override
	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return ARROW_ONLY;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this,(e)->{
			e.setControllerSpeed(getNookSpeed());
			if(isCharging)
				return e.setAndContinue(RawAnimation.begin().thenPlay("animation.model.charge"));

			return PlayState.STOP;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoProperties.cache;
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

	protected int getChargeTiers(){
		int ret = 1;
		if(this.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(APJAttributeRegistry.SHOOT_GOOD.get()))
			ret++;
		if(this.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(APJAttributeRegistry.SHOOT_QUICK.get()))
			ret++;
		return  ret;
	}
	protected float getChargedDamage(Player player){
		var tiers = getChargeTiers();
		var tier = getTierForCharge(player,tiers); //this should never be 0
		if(tier == 1)
			return (float)player.getAttributeValue(APJAttributeRegistry.SHOOT_STRONG.get());
		else if (tier == 3)
			return (float)player.getAttributeValue(APJAttributeRegistry.SHOOT_QUICK.get());
		if(player.getAttributeValue(APJAttributeRegistry.SHOOT_GOOD.get())>0)
			return (float)player.getAttributeValue(APJAttributeRegistry.SHOOT_GOOD.get());
		return (float)player.getAttributeValue(APJAttributeRegistry.SHOOT_QUICK.get());
		//return 1 - 2 - 3. if there are 2 tiers, check which one exists
	}
	public BasicGeoChargedProjectileWeapon SetDamage(int Strong, int Good, int Quick){
		modifiers.put(APJAttributeRegistry.SHOOT_STRONG.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Strong, AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.SHOOT_GOOD.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Good,AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.SHOOT_QUICK.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Quick,AttributeModifier.Operation.ADDITION));
		return this;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}
}
