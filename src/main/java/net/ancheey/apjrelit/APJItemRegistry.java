package net.ancheey.apjrelit;

import net.ancheey.apjrelit.armor.APJArmorMaterials;
import net.ancheey.apjrelit.armor.APJArmorItem;
import net.ancheey.apjrelit.item.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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
            ()->new APJArmorItem("t1_caster_full",APJArmorMaterials.CLOTH, ArmorItem.Type.HELMET,Rarity.EPIC).AddModifier(Attributes.ARMOR,55));
    public static final RegistryObject<Item> TIER1_CASTER_ROBE = ITEMS.register("obliteration_robe",
            ()->new APJArmorItem("t1_caster_full",APJArmorMaterials.CLOTH, ArmorItem.Type.CHESTPLATE,Rarity.EPIC).AddModifier(Attributes.ARMOR_TOUGHNESS,30));
    public static final RegistryObject<Item> TIER1_CASTER_SKIRT = ITEMS.register("obliteration_skirt",
            ()->new APJArmorItem("t1_caster_full",APJArmorMaterials.CLOTH, ArmorItem.Type.LEGGINGS,Rarity.EPIC));
    public static final RegistryObject<Item> TIER1_CASTER_KICKERS = ITEMS.register("obliteration_kickers",
            ()->new APJArmorItem("t1_caster_full",APJArmorMaterials.CLOTH, ArmorItem.Type.BOOTS,Rarity.EPIC));
    public static final RegistryObject<Item> TIER1_CASTER_CAPE = ITEMS.register("obliteration_cape",()->new  BasicGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));


    public static final RegistryObject<Item> TIER1_WARRIOR_HELMET = ITEMS.register("decimation_helmet",
            ()->new APJArmorItem("t1_warrior_full",APJArmorMaterials.PLATE, ArmorItem.Type.HELMET,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_WARRIOR_CHEST = ITEMS.register("decimation_chestplate",
            ()->new APJArmorItem("t1_warrior_full",APJArmorMaterials.PLATE, ArmorItem.Type.CHESTPLATE,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_WARRIOR_LEGS = ITEMS.register("decimation_legplates",
            ()->new APJArmorItem("t1_warrior_full",APJArmorMaterials.PLATE, ArmorItem.Type.LEGGINGS,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_WARRIOR_BOOTS = ITEMS.register("decimation_sabatons",
            ()->new APJArmorItem("t1_warrior_full",APJArmorMaterials.PLATE, ArmorItem.Type.BOOTS,new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_WARRIOR_SHOULDERS = ITEMS.register("decimation_shoulderpads",()->new BasicGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_WARRIOR_GAUNTLETS = ITEMS.register("decimation_gauntlets",()->new BasicGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_WARRIOR_SIGIL = ITEMS.register("decimation_sigil",()->new SigilGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> TIER1_WARRIOR_CAPE = ITEMS.register("decimation_cape",()->new BasicGeoCurioItem(new Item.Properties().stacksTo(1).durability(0)));


    //Rings
    public static final RegistryObject<Item> SILVER_RING = ITEMS.register("silver_ring",()->new  Item(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> GOLDEN_RING = ITEMS.register("golden_ring",()->new  Item(new Item.Properties().stacksTo(1).durability(0)));


    //Weapons

    public static final RegistryObject<Item> WEP_BASE_GREATHAMMER = ITEMS.register("apj_base_greathammer",()->
            new BasicGeoWeaponItem("apj_base_greathammer", 1, Rarity.RARE)
                    .SetTexture("txt_hammer")
                    .SetDamage(7,9,3,6,68));
    public static final RegistryObject<Item> WEP_BASE_GREATSTAR = ITEMS.register("apj_base_greatstar",()->
            new BasicGeoWeaponItem("apj_base_greatstar",1,Rarity.RARE)
                    .SetTexture("txt_hammer")
                    .SetDamage(1,3,3,69,0));
    public static final RegistryObject<Item> WEP_BASE_GREATMACE = ITEMS.register("apj_base_greatmace",()->
            new BasicGeoWeaponItem("apj_base_greatmace",1.4f,Rarity.RARE)
                    .SetTexture("txt_hammer")
                    .SetDamage(16,14,3,6,37));
    public static final RegistryObject<Item> WEP_BASE_ZWEIHANDER = ITEMS.register("apj_base_zweihander",()->
            new BasicGeoWeaponItem("apj_base_zweihander",0.8f,Rarity.RARE)
                    .SetTexture("txt_zweihander")
                    .SetDamage(38,24,1,1,67));

    public static final RegistryObject<Item> bow = ITEMS.register("bow",()-> new BasicGeoChargedProjectileWeapon("bow",25,1).SetDamage(15,10,2));

    public static void register(IEventBus event){
        ITEMS.register(event);
    }
}
