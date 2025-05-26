package net.ancheey.apjrelit.mixins;

import com.mojang.authlib.GameProfile;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.ancheey.apjrelit.dndmodule.DamageHelper;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.api.WeaponAttributesHelper;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(Player.class)
public class PlayerMixin {

	@ModifyArg(
			method = "attack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
			),
			index = 1 // index 1 = the float damage parameter
	)
	private float modifyFinalDamage(float original) {
		var player = ((Player)((Object) this));
		if(player.level().isClientSide)
			return original;

		var item = player.getMainHandItem();
		var damage = DamageHelper.getRandomisedItemDamage(player,item);
		double mod = original/ player.getAttributes().getBaseValue(Attributes.ATTACK_DAMAGE);
		var retval =  damage==0?1f:damage * mod;
		return (float)(retval);
	}
}
