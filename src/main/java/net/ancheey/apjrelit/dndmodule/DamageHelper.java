package net.ancheey.apjrelit.dndmodule;

import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class DamageHelper {
	private static final Random rand = new Random();
	public static float getRandomisedItemDamage(LivingEntity player, ItemStack item) {
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
		var retval =  total==0?1f:total;
		//APJRelitCore.LOGGER.info("[1d"+(diceSides-1)+": "+randResult+ " Damage total: " + total + " * "+ mod + " = " + retval);
		// Modify base attack damage value `f` before it's used
		return (retval); // Example modification
	}
	private static int GetDiceSides(ItemStack item){
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
