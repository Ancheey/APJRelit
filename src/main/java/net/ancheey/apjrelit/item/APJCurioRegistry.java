package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.item.renderer.CurioRenderer;
import net.ancheey.apjrelit.item.renderer.model.Tier1CasterGloveModel;
import net.ancheey.apjrelit.item.renderer.model.Tier1CasterShoulderModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class APJCurioRegistry {
	public static void registerCurios(){
	}
	public static void registerRenderers(final FMLClientSetupEvent e){
		CuriosRendererRegistry.register(APJItemRegistry.TIER1_CASTER_SHOULDERS.get(),
				()-> new CurioRenderer(new Tier1CasterShoulderModel<>(), ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/armor/t1_caster_set.png")));
		CuriosRendererRegistry.register(APJItemRegistry.TIER1_CASTER_GLOVES.get(),
				()-> new CurioRenderer(new Tier1CasterGloveModel<>(), ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/armor/t1_caster_set.png")));
	}
}
