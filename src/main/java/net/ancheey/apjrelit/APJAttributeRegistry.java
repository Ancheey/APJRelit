package net.ancheey.apjrelit;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class APJAttributeRegistry {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES,APJRelitCore.MODID);

	//Max damage = DAMAGE_FLAT + DAMAGE_DICE + DAMAGE_FLAT_BONUS  + DAMAGE_MAX_BONUS
	//Min damage = DAMAGE_FLAT + FLAT_BONUS + DAMAGE_FLAT_BONUS
	//public static final RegistryObject<Attribute> ATTACK_DAMAGE_DICE = ATTRIBUTES.register("attack_damage_dice", ()->new RangedAttribute("attribute.apjrelit.ddice",0d,0d,16777216d).setSyncable(true));
	//public static final RegistryObject<Attribute> ATTACK_DICES = ATTRIBUTES.register("attack_dice", ()->new RangedAttribute("attribute.apjrelit.adice",0d,0d,5d).setSyncable(true));
	public static final RegistryObject<Attribute> ATTACK_PRECISE_BLOW = ATTRIBUTES.register("attack_precise", ()->new RangedAttribute("attribute.apjrelit.a1",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> ATTACK_GREAT_BLOW = ATTRIBUTES.register("attack_great", ()->new RangedAttribute("attribute.apjrelit.a2",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> ATTACK_GOOD_BLOW = ATTRIBUTES.register("attack_good", ()->new RangedAttribute("attribute.apjrelit.a3",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> ATTACK_FINE_BLOW = ATTRIBUTES.register("attack_fine", ()->new RangedAttribute("attribute.apjrelit.a4",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> ATTACK_CONNECTING_BLOW = ATTRIBUTES.register("attack_connecting", ()->new RangedAttribute("attribute.apjrelit.a5",0d,0d,16777216d).setSyncable(true));

	public static final RegistryObject<Attribute> ENMITY_MODIFIER = ATTRIBUTES.register("enmity_modifier", ()->new RangedAttribute("attribute.apjrelit.enmity",1d,0d,16777216d).setSyncable(true));

	public static final RegistryObject<Attribute> SPELL_DAMAGE = ATTRIBUTES.register("spell_damage", ()->new RangedAttribute("attribute.apjrelit.sd",0d,-16777216d,16777216d).setSyncable(true));

	public static final RegistryObject<Attribute> SHOOT_STRONG = ATTRIBUTES.register("shoot_strong", ()->new RangedAttribute("attribute.apjrelit.s1",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> SHOOT_GOOD = ATTRIBUTES.register("shoot_good", ()->new RangedAttribute("attribute.apjrelit.s2",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> SHOOT_QUICK= ATTRIBUTES.register("shoot_quick", ()->new RangedAttribute("attribute.apjrelit.s3",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> NOOK_SPEED = ATTRIBUTES.register("nook_speed", ()->new RangedAttribute("attribute.apjrelit.nook_speed",1d,-16777216d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> RANGED_POWER_RATING = ATTRIBUTES.register("ranged_power_rating", ()->new RangedAttribute("attribute.apjrelit.ranged_rating",0d,-16777216d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> SPELL_RESISTANCE_RATING = ATTRIBUTES.register("spell_resistance", ()->new RangedAttribute("attribute.apjrelit.sr",0d,-16777216d,16777216d).setSyncable(true));

	//public static final RegistryObject<Attribute> SPELL_RESISTANCE_RATING = ATTRIBUTES.register("spell_resistance", ()->new RangedAttribute("attribute.apjrelit.sr",0d,-16777216d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> BLOCK_AMOUNT = ATTRIBUTES.register("block_amount", ()->new RangedAttribute("attribute.apjrelit.block",0d,-16777216d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> CRITICAL_STRIKE_RATING = ATTRIBUTES.register("critical_rating", ()->new RangedAttribute("attribute.apjrelit.crit",0d,-16777216d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> RESILIENCE = ATTRIBUTES.register("resilience", ()->new RangedAttribute("attribute.apjrelit.resilience",0d,-16777216d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> CRITICAL_DAMAGE_RATING = ATTRIBUTES.register("critical_damage_rating", ()->new RangedAttribute("attribute.apjrelit.critdamage",0d,0d,16777216d).setSyncable(true));



	//invuln frames
	public static final RegistryObject<Attribute> ADRENALINE = ATTRIBUTES.register("adrenaline", ()-> new RangedAttribute("attribute.apjrelit.adrenaline",0d,0d,16777216d).setSyncable(true));

	public static void Register(IEventBus e){
		ATTRIBUTES.register(e);
	}
	@SubscribeEvent
	public static void onEntityAttributeCreate(EntityAttributeModificationEvent event) {
		event.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> event.add(entity, attribute.get())));
	}
}
