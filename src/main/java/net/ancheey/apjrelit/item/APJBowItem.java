package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.projectiles.HitscanProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class APJBowItem extends ProjectileWeaponItem {
	private int distance;
	private float damage;
	private float speed;
	public APJBowItem(float damage, int distance, float nookSpeed) {
		super(new Item.Properties());
		this.distance = distance;
		this.damage = damage;
		speed= nookSpeed;
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return ARROW_ONLY;
	}

	@Override
	public int getDefaultProjectileRange() {
		return distance;
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
			return InteractionResultHolder.consume(itemstack);
		}
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

				float f = getPowerForTime(i);
				if (!((double)f < 0.1D)) {
					boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));
					var target = raycastTarget(pEntityLiving);
					if(target != null && target.canBeHitByProjectile()){
						pLevel.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
						if (!pLevel.isClientSide) {
							target.hurt(pLevel.damageSources().playerAttack(player),damage);
						/*
						//ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
						//AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, itemstack, player);
						abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
						if (f == 1.0F) {
							abstractarrow.setCritArrow(true);
						}

						int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, pStack);
						if (j > 0) {
							abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, pStack);
						if (k > 0) {
							abstractarrow.setKnockback(k);
						}

						if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack) > 0) {
							abstractarrow.setSecondsOnFire(100);
						}

						pStack.hurtAndBreak(1, player, (p_289501_) -> {
							p_289501_.broadcastBreakEvent(player.getUsedItemHand());
						});
						if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
							abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
						}

						pLevel.addFreshEntity(abstractarrow);
						 */
						}
					}
					new HitscanProjectile(pEntityLiving, ParticleTypes.CRIT);
					pLevel.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
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
	public static float getPowerForTime(int pCharge) {
		float f = (float)pCharge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}
	public int getUseDuration(ItemStack pStack) {
		return (int)(72000 / speed);
	}
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}
	private @Nullable Entity raycastTarget(LivingEntity e){
		Vec3 eyePos = e.getEyePosition(1.0F);
		Vec3 lookVec = e.getViewVector(1.0F);
		Vec3 endVec = eyePos.add(lookVec.scale(getDefaultProjectileRange()));

		AABB box = e.getBoundingBox().expandTowards(lookVec.scale(getDefaultProjectileRange())).inflate(1.0); // widen box slightly

		EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
				e.level(), e, eyePos, endVec, box,
				(entity) -> !entity.isSpectator() && entity.isPickable() && entity instanceof LivingEntity && entity != e && !e.isAlliedTo(entity)
		);

		return hitResult != null ? hitResult.getEntity() : null;
	}
}
