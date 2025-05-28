package net.ancheey.apjrelit.gui.tooltip;

import com.mojang.datafixers.util.Either;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = APJRelitCore.MODID, value = Dist.CLIENT)
public class TooltipClientEvents {
	@SubscribeEvent
	public static  void renderTooltipEventGC(RenderTooltipEvent.GatherComponents e){
		var item = e.getItemStack();
		var list = e.getTooltipElements();
		for(int i = 0; i < list.size(); i++){
			var entry  =list.get(i);
			int finalI = i;
			entry.left().ifPresent(k->{
				if(k instanceof Component c) {
					if (c.contains(Component.literal("{apj.weapon.damage.dice}"))) {
						list.remove(finalI);
						list.add(finalI, Either.right(new DiceTooltipComponent(
								(int) AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_PRECISE_BLOW.get(), EquipmentSlot.MAINHAND),
								(int) AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_GREAT_BLOW.get(), EquipmentSlot.MAINHAND),
								(int) AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_GOOD_BLOW.get(), EquipmentSlot.MAINHAND),
								(int) AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_FINE_BLOW.get(), EquipmentSlot.MAINHAND),
								(int) AttributeHelper.GetValue(item, APJAttributeRegistry.ATTACK_CONNECTING_BLOW.get(), EquipmentSlot.MAINHAND)
						)));
					}
					else if(c.contains(Component.literal("{apj.weapon.damage.power}"))){
						list.remove(finalI);
						list.add(finalI, Either.right(new PowerTooltipComponent(
								(int) AttributeHelper.GetValue(item, APJAttributeRegistry.SHOOT_STRONG.get(), EquipmentSlot.MAINHAND),
								(int) AttributeHelper.GetValue(item, APJAttributeRegistry.SHOOT_GOOD.get(), EquipmentSlot.MAINHAND),
								(int) AttributeHelper.GetValue(item, APJAttributeRegistry.SHOOT_QUICK.get(), EquipmentSlot.MAINHAND)
						)));
					}
				}
			});
		}
	}
}
