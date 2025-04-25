package net.ancheey.apjrelit;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class APJAttributeRegistry {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES,APJRelitCore.MODID);

	public static final RegistryObject<Attribute> DAMAGE_DICE_1 = ATTRIBUTES.register("damage_dice_1", ()->new RangedAttribute("attribute.apjrelit.ddice1",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> DAMAGE_DICE_2 = ATTRIBUTES.register("damage_dice_2", ()-> new RangedAttribute("attribute.apjrelit.ddice2",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> DAMAGE_DICE_3 = ATTRIBUTES.register("damage_dice_3", ()->new RangedAttribute("attribute.apjrelit.ddice3",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> DAMAGE_DICE_4 = ATTRIBUTES.register("damage_dice_4", ()-> new RangedAttribute("attribute.apjrelit.ddice4",0d,0d,16777216d).setSyncable(true));
	public static final RegistryObject<Attribute> DAMAGE_DICE_5 = ATTRIBUTES.register("damage_dice_5", ()-> new RangedAttribute("attribute.apjrelit.ddice5",0d,0d,16777216d).setSyncable(true));

	public static void Register(IEventBus e){
		ATTRIBUTES.register(e);
	}
}
