package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class APJItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, APJRelitCore.MODID);

    public static final RegistryObject<Item> TIER1_CASTER_SHOULDERS = ITEMS.register("obliteration_shoulderpads",()->new ObliterationShoulderpads());
    public static void register(IEventBus event){
        ITEMS.register(event);
    }
}
