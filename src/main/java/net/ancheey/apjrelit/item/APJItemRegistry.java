package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class APJItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, APJRelitCore.MODID);

    public static void Register(IEventBus event){
        ITEMS.register(event);
    }
}
