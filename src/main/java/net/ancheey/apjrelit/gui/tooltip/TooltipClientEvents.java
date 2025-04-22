package net.ancheey.apjrelit.gui.tooltip;

import com.mojang.datafixers.util.Either;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = APJRelitCore.MODID, value = Dist.CLIENT)
public class TooltipClientEvents {
	@SubscribeEvent
	public static  void renderTooltipEventGC(RenderTooltipEvent.GatherComponents e){
		var list = e.getTooltipElements();
		for(int i = 0; i < list.size(); i++){
			var entry  =list.get(i);
			int finalI = i;
			entry.left().ifPresent(k->{
				if(k instanceof Component c && c.contains(Component.literal("{apj.dice}"))) {
					list.remove(finalI);
					list.add(finalI, Either.right(new DiceTooltipComponent(5, 8, 12, 24,0)));
				}
			});
		}
	}
}
