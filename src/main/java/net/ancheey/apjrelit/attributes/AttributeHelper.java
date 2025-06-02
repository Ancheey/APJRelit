package net.ancheey.apjrelit.attributes;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicReference;

public class AttributeHelper {
	public static double GetValue(ItemStack item, Attribute attribute, EquipmentSlot slot){
		Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> modifiers = item.getAttributeModifiers(slot);
		var mods = modifiers.get(attribute).stream().filter(k-> k.getOperation() == AttributeModifier.Operation.ADDITION);
		AtomicReference<Double> value = new AtomicReference<>((double) 0);
		mods.forEach(mod->{
			value.updateAndGet(v -> (v + mod.getAmount()));
		});
		return value.get();
	}
	public  static double GetBaseMultiplier(ItemStack item, Attribute attribute, EquipmentSlot slot){
		Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> modifiers = item.getAttributeModifiers(slot);
		var mods = modifiers.get(attribute).stream().filter(k-> k.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE);
		AtomicReference<Double> value = new AtomicReference<>((double) 0);
		mods.forEach(mod->{
			value.updateAndGet(v -> (v + mod.getAmount()));
		});
		return value.get();
	}
	public  static double GetTotalMultiplier(ItemStack item, Attribute attribute, EquipmentSlot slot){
		Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> modifiers = item.getAttributeModifiers(slot);
		var mods = modifiers.get(attribute).stream().filter(k-> k.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL);
		AtomicReference<Double> value = new AtomicReference<>((double) 0);
		mods.forEach(mod->{
			value.updateAndGet(v -> (v *= mod.getAmount()));
		});
		return value.get();
	}
	public static int ratingAtLevel(int level){
		return  Math.min(level,1)/3;
	}
}
