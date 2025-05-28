package net.ancheey.apjrelit.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.projectiles.HitscanProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class APJProjectileWeaponItem extends ProjectileWeaponItem {
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
		var target = raycastTarget(player);
		boolean ret = false;
		if(target != null){
			if(!level.isClientSide)
				target.hurt(level.damageSources().playerAttack(player),damage);
			level.playSound(player, player.getX(), player.getY(), player.getZ(), getHitSound(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
			ret = true;
		}
		new HitscanProjectile(player, ParticleTypes.CRIT);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), getShootSound(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
		return ret;
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
	protected @Nullable Entity raycastTarget(LivingEntity e){
		Vec3 eyePos = e.getEyePosition(1.0F);
		Vec3 lookVec = e.getViewVector(1.0F);
		Vec3 endVec = eyePos.add(lookVec.scale(getDefaultProjectileRange()));

		AABB box = e.getBoundingBox().expandTowards(lookVec.scale(getDefaultProjectileRange())).inflate(1.0); // widen box slightly

		EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
				e.level(), e, eyePos, endVec, box,
				(entity) -> !entity.isSpectator() && entity.isPickable() && entity instanceof LivingEntity && entity != e && !e.isAlliedTo(entity) && entity.canBeHitByProjectile()
		);

		return hitResult != null ? hitResult.getEntity() : null;
	}
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
