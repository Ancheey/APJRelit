package net.ancheey.apjrelit.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.item.renderer.BasicGeoItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.UUID;
import java.util.function.Consumer;

public class BasicGeoShieldItem extends ShieldItem implements GeoItem {
	APJGeoItemProperties geoProperties;
	public BasicGeoShieldItem(String modelIdentifier, Rarity rarity) {
		super(new Properties().stacksTo(1).durability(-1).rarity(rarity));
		geoProperties = new APJGeoItemProperties(this, modelIdentifier, modelIdentifier);
	}
	public BasicGeoShieldItem setTexture(String filename){
		geoProperties.textureFile = filename;
		return this;
	}
	public BasicGeoShieldItem setColor(float a, float r, float g, float b){
		geoProperties.alpha = a;
		geoProperties.red = r;
		geoProperties.green = g;
		geoProperties.blue = b;
		return this;
	}
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return geoProperties.cache;
	}
	ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers = ImmutableMultimap.builder();
	public BasicGeoShieldItem AddModifier(Attribute attribute, float value, AttributeModifier.Operation operation){
		modifiers.put(attribute, new AttributeModifier(UUID.randomUUID(),"apj modifier",value,operation));
		return this;
	}
	public BasicGeoShieldItem AddModifier(Attribute attribute, float value){
		return AddModifier(attribute, value, AttributeModifier.Operation.ADDITION);
	}
	/*public BasicGeoShieldItem SetDamage(int Precise, int Great, int Good, int Fine, int Connecting){
		modifiers.put(APJAttributeRegistry.ATTACK_PRECISE_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Precise, AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.ATTACK_GREAT_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Great,AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.ATTACK_GOOD_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Good,AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.ATTACK_FINE_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Fine,AttributeModifier.Operation.ADDITION));
		modifiers.put(APJAttributeRegistry.ATTACK_CONNECTING_BLOW.get(), new AttributeModifier(UUID.randomUUID(),"apj modifier",Connecting,AttributeModifier.Operation.ADDITION));
		return this;
	}
*/
	@Override
	public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
		if(pEquipmentSlot == EquipmentSlot.MAINHAND){
			builder.putAll(modifiers.build());
		}
		return builder.build();
	}
	@OnlyIn(Dist.CLIENT)
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private BasicGeoItemRenderer<BasicGeoShieldItem> renderer;
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				if(renderer == null){
					renderer = new BasicGeoItemRenderer<>(geoProperties);
				}
				return  renderer;
			}
		});
	}

}
