package net.ancheey.apjrelit.mixins;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.apache.http.util.Args;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(
			method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/entity/LivingEntity;invulnerableTime:I",
					opcode = Opcodes.PUTFIELD,
					ordinal = 0
			),
			cancellable = true
	)
	private void injectSetCustomInvulnerableTime(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		((LivingEntity)(Object)this).invulnerableTime = 15;//MyModHooks.getCustomInvulnerableTime((LivingEntity)(Object)this, source, amount);
	}
}
