package net.ancheey.apjrelit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue LOAD_SETS_MODULE = BUILDER
            .comment("Turn on gear sets module")
            .define("GearSetsModule", true);
//TODO: fix config loading
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean loadSetsModule;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        loadSetsModule = LOAD_SETS_MODULE.get();
    }
}
