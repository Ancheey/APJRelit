package net.ancheey.apjrelit;

import com.mojang.logging.LogUtils;

import net.ancheey.apjrelit.gui.APJKeyMapping;
import net.ancheey.apjrelit.item.APJCurioRegistry;
import net.ancheey.apjrelit.item.APJItemRegistry;
import net.ancheey.apjrelit.itemsets.APJSetModuleEventHandler;
import net.ancheey.apjrelit.itemsets.ItemSetManager;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;

import net.minecraft.world.item.*;

import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(APJRelitCore.MODID)
public class APJRelitCore
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "apjrelit";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("apjitems",()->CreativeModeTab.builder().title(Component.literal("Apj Items")).build());
    public APJRelitCore(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        APJItemRegistry.register(modEventBus);
        APJCurioRegistry.registerCurios();

        CREATIVE_MODE_TABS.register(modEventBus);

        //TODO: Item Sets: Add and test combat effect handling


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(APJKeyMapping::registerKeybinds);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        LOGGER.info("Loading set module");
        MinecraftForge.EVENT_BUS.register(new APJSetModuleEventHandler());
    }

    private void clientSetup(final FMLClientSetupEvent event){
        APJCurioRegistry.registerRenderers(event);
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        ItemSetManager.loadItemSets();
    }
    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTab() == CREATIVE_TAB.get()){
            for(var a : APJItemRegistry.ITEMS.getEntries()){
                event.accept(a.get());
            }
        }
    }

}
