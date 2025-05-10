package net.ancheey.apjrelit.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class APJSwordItem extends SwordItem {
	public APJSwordItem(float attackSpeed) {
		super(Tiers.IRON,  - 2, attackSpeed - 4, new Item.Properties().durability(0).stacksTo(1));
	}
	ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers = ImmutableMultimap.builder();
	public APJSwordItem AddModifier(Attribute attribute, float value, AttributeModifier.Operation operation){
		modifiers.put(attribute, new AttributeModifier(UUID.randomUUID(),"apj modifier",value,operation));
		return this;
	}
	public APJSwordItem AddModifier(Attribute attribute, float value){
		return AddModifier(attribute, value, AttributeModifier.Operation.ADDITION);
	}
	public APJSwordItem SetDamage(int Precise, int Great, int Good, int Fine, int Connecting){
		modifiers.put(APJAttributeRegistry.ATTACK_PRECISE_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Precise, AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.ATTACK_GREAT_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Great,AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.ATTACK_GOOD_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Good,AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.ATTACK_FINE_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Fine,AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.ATTACK_CONNECTING_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Connecting,AttributeModifier.Operation.ADDITION));
		return this;
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
}
