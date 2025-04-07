package net.ancheey.apjrelit;

import com.mojang.logging.LogUtils;
import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.client.AzureLibClient;
import mod.azure.azurelib.util.AzureLibUtil;
import net.ancheey.apjrelit.item.APJCurioRegistry;
import net.ancheey.apjrelit.item.APJItemRegistry;
import net.ancheey.apjrelit.itemsets.ItemSetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(APJRelitCore.MODID)
public class APJRelitCore
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "apjrelit";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
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

        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in


        //TODO: Test curios
        //TODO: Add combat effect handling
        //TODO: Test ^
        //TODO: Maybe optimize effect handling so it doesn't run entirely on strings (move effects to set bonuses?, change providers)
        //TODO: Add T1 Set
        //TODO: Change how items are displayed (rid of those fugly attribute stats)


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        LOGGER.info("Loading set module");
        MinecraftForge.EVENT_BUS.register(new APJSetModuleEventHandler());
        ItemSetManager.loadItemSets();
    }

    private void clientSetup(final FMLClientSetupEvent event){
        APJCurioRegistry.registerRenderers(event);
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
    }
    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTab() == CREATIVE_TAB.get()){
            event.accept(APJItemRegistry.TIER1_CASTER_SHOULDERS);
        }
    }

}
