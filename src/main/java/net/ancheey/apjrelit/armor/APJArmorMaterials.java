package net.ancheey.apjrelit.armor;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.function.Supplier;

public enum APJArmorMaterials implements ArmorMaterial {
	CLOTH("cloth", SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.of(Items.AIR)),
	HIDE("hide", SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(Items.AIR)),
	MAIL("mail", SoundEvents.ARMOR_EQUIP_CHAIN, () -> Ingredient.of(Items.AIR)),
	PLATE("plate", SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(Items.AIR)),
	WOOD("wood", SoundEvents.ARMOR_EQUIP_TURTLE, () -> Ingredient.of(Items.AIR)),
	CRYSTAL("crystal", SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.AIR));

	private final String name;
	private final SoundEvent sound;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	private APJArmorMaterials(String pName, SoundEvent pSound, Supplier<Ingredient> pRepairIngredient) {
		this.name = pName;
		this.sound = pSound;
		this.repairIngredient = new LazyLoadedValue<Ingredient>(pRepairIngredient);
	}

	public int getDurabilityForType(ArmorItem.Type pType) {
		return -1;
	}

	public int getDefenseForType(ArmorItem.Type pType) {
		return 0;
	}

	public int getEnchantmentValue() {
		return 0;
	}

	public SoundEvent getEquipSound() {
		return this.sound;
	}

	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	public String getName() {
		return this.name;
	}

	public float getToughness() {
		return 0;
	}

	/**
	 * Gets the percentage of knockback resistance provided by armor of the material.
	 */
	public float getKnockbackResistance() {
		return 0;
	}

	public String getSerializedName() {
		return this.name;
	}
}
