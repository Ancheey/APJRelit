package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.armor.APJArmorMaterials;
import net.ancheey.apjrelit.armor.APJArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class APJItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, APJRelitCore.MODID);

    //Gear Curios
    public static final RegistryObject<Item> TIER1_CASTER_SHOULDERS = ITEMS.register("obliteration_shoulderpads",()->new BasicGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_CASTER_GLOVES = ITEMS.register("obliteration_gloves",()->new BasicGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_CASTER_SIGIL = ITEMS.register("obliteration_sigil",()->new SigilGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_CASTER_COWL = ITEMS.register("obliteration_cowl",
            ()->new APJArmorItem("t1_caster_full",APJArmorMaterials.CLOTH, ArmorItem.Type.HELMET,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_CASTER_ROBE = ITEMS.register("obliteration_robe",
            ()->new APJArmorItem("t1_caster_full",APJArmorMaterials.CLOTH, ArmorItem.Type.CHESTPLATE,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_CASTER_SKIRT = ITEMS.register("obliteration_skirt",
            ()->new APJArmorItem("t1_caster_full",APJArmorMaterials.CLOTH, ArmorItem.Type.LEGGINGS,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_CASTER_KICKERS = ITEMS.register("obliteration_kickers",
            ()->new APJArmorItem("t1_caster_full",APJArmorMaterials.CLOTH, ArmorItem.Type.BOOTS,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_CASTER_CAPE = ITEMS.register("obliteration_cape",()->new  Item(new Item.Properties().stacksTo(1).durability(0)));


    public static final RegistryObject<Item> TIER1_WARRIOR_HELMET = ITEMS.register("decimation_helmet",
            ()->new APJArmorItem("t1_warrior_full",APJArmorMaterials.PLATE, ArmorItem.Type.HELMET,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_WARRIOR_SHOULDERS = ITEMS.register("decimation_shoulderpads",()->new BasicGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));


    //Rings
    public static final RegistryObject<Item> SILVER_RING = ITEMS.register("silver_ring",()->new  Item(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> GOLDEN_RING = ITEMS.register("golden_ring",()->new  Item(new Item.Properties().stacksTo(1).durability(0)));
    public static void register(IEventBus event){
        ITEMS.register(event);
    }
}
