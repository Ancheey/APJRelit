package net.ancheey.apjrelit.mixins;

import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.api.WeaponAttributesHelper;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(Player.class)
public class PlayerMixin {
	private static final Random rand = new Random();
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
		var diceSides = GetDiceSides(item) + 1;
		var randResult = rand.nextInt(0,diceSides);
		var total = 0f;
		if(randResult <= 5 && diceSides >= 5)
			total += (float)player.getAttributeValue(APJAttributeRegistry.ATTACK_CONNECTING_BLOW.get());

		if(randResult <= 4&& diceSides <= 5)
			total += (float)player.getAttributeValue(APJAttributeRegistry.ATTACK_FINE_BLOW.get());

		if(randResult <= 3&& diceSides >= 4)
			total += (float) player.getAttributeValue(APJAttributeRegistry.ATTACK_GOOD_BLOW.get());

		if(randResult <= 2&& diceSides >= 3)
			total += (float) player.getAttributeValue(APJAttributeRegistry.ATTACK_GREAT_BLOW.get());

		if(randResult <= 1&& diceSides >= 2)
			total += (float) player.getAttributeValue(APJAttributeRegistry.ATTACK_PRECISE_BLOW.get());
		double mod = original/ player.getAttributes().getBaseValue(Attributes.ATTACK_DAMAGE);
		var retval =  total==0?1f:total * mod;
		//APJRelitCore.LOGGER.info("[1d"+(diceSides-1)+": "+randResult+ " Damage total: " + total + " * "+ mod + " = " + retval);
		// Modify base attack damage value `f` before it's used
		return (float)(retval); // Example modification
	}
	private int GetDiceSides(ItemStack item){
		if(AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_CONNECTING_BLOW.get(), EquipmentSlot.MAINHAND)>0)
			return 5;
		if(AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_FINE_BLOW.get(), EquipmentSlot.MAINHAND)>0)
			return 4;
		if(AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_GOOD_BLOW.get(), EquipmentSlot.MAINHAND)>0)
			return 3;
		if(AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_GREAT_BLOW.get(), EquipmentSlot.MAINHAND)>0)
			return 2;
		if(AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_PRECISE_BLOW.get(), EquipmentSlot.MAINHAND)>0)
			return 1;
		return 0;
	}
}
