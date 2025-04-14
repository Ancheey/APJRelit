package net.ancheey.apjrelit.item;

import net.ancheey.apjrelit.item.renderer.GeoCurioRenderer;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class APJCurioRegistry {
	public static void registerCurios(){
	}
	public static void registerRenderers(final FMLClientSetupEvent e){
		CuriosRendererRegistry.register(APJItemRegistry.TIER1_CASTER_SHOULDERS.get(), ()-> GeoCurioRenderer.forShoulders("t1_caster_full"));
		CuriosRendererRegistry.register(APJItemRegistry.TIER1_CASTER_GLOVES.get(), ()-> GeoCurioRenderer.forGloves("t1_caster_full"));
	}
}
