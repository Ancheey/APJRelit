package net.ancheey.apjrelit.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.ancheey.apjrelit.projectiles.HitscanProjectile;
import net.ancheey.apjrelit.projectiles.TargettingUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class APJProjectileWeaponItem extends ProjectileWeaponItem {
	public final int BASE_TICK_USE_TIME = 70;
	private int distance;
	public APJProjectileWeaponItem(int distance) {
		super(new Item.Properties());
		this.distance = distance;
	}

	@Override
	public int getDefaultProjectileRange() {
		return distance;
	}
	public abstract InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand);
	public abstract void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft);
	public SimpleParticleType getShootParticle(){
		return ParticleTypes.CRIT;
	}
	public SoundEvent getShootSound(){
		return SoundEvents.ARROW_SHOOT;
	}
	public SoundEvent getHitSound(){
		return SoundEvents.ARROW_HIT_PLAYER;
	}
	protected boolean shoot(float power, float damage, Level level, Player player){
		var target = TargettingUtil.raycastTarget(player,getDefaultProjectileRange());
		boolean hit = target!=null;
		var rap = player.getAttributeValue(APJAttributeRegistry.RANGED_POWER_RATING.get());
		damage += (float)((rap / AttributeHelper.ratingAtLevel(player.experienceLevel)) * power);
		if(!level.isClientSide()) {
			if(hit){
				level.playSound(player,player.getX(), player.getY(), player.getZ(), getHitSound(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
				target.hurt(level.damageSources().playerAttack(player), damage);
			}

			new HitscanProjectile(player, ParticleTypes.CRIT);
			level.playSound(null, player.getX(), player.getY(), player.getZ(), getShootSound(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
			APJRelitCore.LOGGER.info("-  Hit: " + hit + (hit? ", Target: " + target.getName() : ""));
		}
		return hit;
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
		return (int)(72000);
	} //do not try it


	@Override
	public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
		if(pEquipmentSlot == EquipmentSlot.MAINHAND){
			builder.putAll(modifiers.build());
		}
		return builder.build();
	}
	ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers = ImmutableMultimap.builder();
	public APJProjectileWeaponItem AddModifier(Attribute attribute, float value, AttributeModifier.Operation operation){
		modifiers.put(attribute, new AttributeModifier(UUID.randomUUID(),"apj modifier",value,operation));
		return this;
	}
	public APJProjectileWeaponItem AddModifier(Attribute attribute, float value){
		return AddModifier(attribute, value, AttributeModifier.Operation.ADDITION);
	}
}
