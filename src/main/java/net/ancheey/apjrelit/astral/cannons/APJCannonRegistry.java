package net.ancheey.apjrelit.astral.cannons;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class APJCannonRegistry {
	public static final DeferredRegister<EntityType<?>> CANNONS = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, APJRelitCore.MODID);
	public static final RegistryObject<EntityType<ShipCannonEntity>> CANNON = CANNONS.register("cannon",
			()->EntityType.Builder.of(ShipCannonEntity::new, MobCategory.MISC).build(ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"cannon").toString()));
	public static void register(IEventBus e){
		CANNONS.register(e);
	}
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent e){
		EntityRenderers.register(CANNON.get(), (k) -> new GeoCannonRenderer<>(k,new GeoCannonModel<>("cannon")));
	}
}
