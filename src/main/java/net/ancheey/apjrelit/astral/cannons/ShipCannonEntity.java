package net.ancheey.apjrelit.astral.cannons;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.projectiles.HitscanProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class ShipCannonEntity extends Entity implements GeoEntity {
	private static final EntityDataAccessor<Float> DATA_ID_HEALTH = SynchedEntityData.defineId(ShipCannonEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ID_VROT = SynchedEntityData.defineId(ShipCannonEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ID_HROT = SynchedEntityData.defineId(ShipCannonEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ID_WINDUP = SynchedEntityData.defineId(ShipCannonEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> DATA_ID_TAKEN = SynchedEntityData.defineId(ShipCannonEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_ID_ANIM_STATE = SynchedEntityData.defineId(ShipCannonEntity.class, EntityDataSerializers.INT); //0 still | 1 starting | 2 shooting | 3 stopping
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	public ShipCannonEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		setBoundingBox(new AABB(this.position().subtract(1, 0, 1), this.position().add(1, 1.8, 1)));
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(DATA_ID_HEALTH,1f);
		entityData.define(DATA_ID_VROT,0f);
		entityData.define(DATA_ID_HROT,0f);
		entityData.define(DATA_ID_WINDUP,0f);
		entityData.define(DATA_ID_TAKEN,false);
		entityData.define(DATA_ID_ANIM_STATE,0);
	}
	public float getMaxVerticalRotation(){
		return 1f;
	}
	private @Nullable Player interactor;
	@Override
	public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
		if(!this.level().isClientSide){
			if(isBeingUsed() && interactor != pPlayer) return InteractionResult.FAIL;
			if(interactor == null){
				interactor = pPlayer;
				this.entityData.set(DATA_ID_TAKEN, true);
			}
			else{
				interactor = null;
				this.entityData.set(DATA_ID_TAKEN, false);
			}

			this.entityData.set(DATA_ID_VROT, (float)pPlayer.getLookAngle().y);
			this.entityData.set(DATA_ID_HROT, (float)pPlayer.getLookAngle().x);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.CONSUME;
	}
	public boolean isBeingUsed(){
		return this.entityData.get(DATA_ID_TAKEN);
	}
	public float getWindup(){
		return this.entityData.get(DATA_ID_WINDUP);
	}
	public float getMaxYRot(){
		return 0.2f;
	}
	public float getMaxXRot(){
		return 0.4f;
	}
	int tickstack = 0;
	@Override
	public void tick() {
		if(interactor == null) super.tick();

		if(isBeingUsed() && interactor != null){
			this.entityData.set(DATA_ID_VROT, (float)interactor.getLookAngle().y * 0.7f);
			this.entityData.set(DATA_ID_HROT, (float)interactor.getLookAngle().x*1.2f);
			if(interactor.isDiscrete()){
				if(getWindup() < 1f){
					this.entityData.set(DATA_ID_WINDUP, getWindup() + 0.5f);
				}
				else{
					if(!this.level().isClientSide){
						if(tickstack == 2){
							tickstack = 0;
							new HitscanProjectile(
									this.level(),
									getLookAngle(),
									position().add(getLookAngle().scale(2)).add(0,1f,0),
									ParticleTypes.CRIT);
							APJRelitCore.LOGGER.info("Cannon: "+getLookAngle());
							APJRelitCore.LOGGER.info("Player: "+interactor.getLookAngle());
							this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 0.5F, 0.2f);
						}
						else
							tickstack++;

					}
				}
			}
		}
		else if(getWindup()>0)
			this.entityData.set(DATA_ID_WINDUP, getWindup() - 0.5f);


		super.tick();
	}


	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean mayInteract(Level pLevel, BlockPos pPos) {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public @NotNull Vec3 getLookAngle() {
		//var vec = getRotationVector(); //calculate based on this - also calc clamps based on this.
		return new Vec3(Math.sin(getHorizontalRotation()), getVerticalRotation(), Math.cos(getHorizontalRotation()));
		//return calculateViewVector(getHorizontalRotation(),getVerticalRotation());
	}

	public float getVerticalRotation(){
		return Mth.clamp( this.entityData.get(DATA_ID_VROT), getRotationVector().y-getMaxYRot(),getRotationVector().y+getMaxYRot());
	}
	public float getHorizontalRotation(){
		return Mth.clamp( this.entityData.get(DATA_ID_HROT), getRotationVector().x-getMaxXRot(),getRotationVector().y+getMaxXRot());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {

	}
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this,"shoot",0,(e)->e.setAndContinue(RawAnimation.begin().thenLoop("animation.model.shoot"))));
	}
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoCache;
	}

	@Override
	public boolean isPushable() {
		return false;
	}
}
