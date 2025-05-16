package net.ancheey.apjrelit.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.armor.renderer.APJArmorRenderer;
import net.ancheey.apjrelit.item.APJSwordItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class APJArmorItem extends ArmorItem implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private final String identifier;
	public APJArmorItem(String setIdentifier, ArmorMaterial pMaterial, Type pType, Properties pProperties) {
		super(pMaterial, pType, pProperties);
		identifier = setIdentifier;
	}
	public APJArmorItem(String setIdentifier, ArmorMaterial pMaterial, Type pType, Rarity rarity) {
		super(pMaterial, pType, new Item.Properties().stacksTo(1).durability(-1).rarity(rarity));
		identifier = setIdentifier;
	}
	ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers = ImmutableMultimap.builder();
	public APJArmorItem AddModifier(Attribute attribute, float value, AttributeModifier.Operation operation){
		modifiers.put(attribute, new AttributeModifier(UUID.randomUUID(),"apj modifier",value,operation));
		return this;
	}
	public APJArmorItem AddModifier(Attribute attribute, float value){
		return AddModifier(attribute, value, AttributeModifier.Operation.ADDITION);
	}
	@Override
	public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return APJRelitCore.MODID+":textures/armor/"+identifier+".png";
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private GeoArmorRenderer<?> renderer;

			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				if (this.renderer == null)
					this.renderer = new APJArmorRenderer(identifier);

				// This prepares our GeoArmorRenderer for the current render frame.
				// These parameters may be null however, so we don't do anything further with them
				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

				return this.renderer;
			}
		});
	}
	@Override
	public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
		if(pEquipmentSlot == this.getEquipmentSlot()){
			builder.putAll(modifiers.build());
		}
		return builder.build();
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
