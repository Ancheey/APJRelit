package net.ancheey.apjrelit;

import net.ancheey.apjrelit.itemsets.ItemSet;
import net.ancheey.apjrelit.itemsets.ItemSetManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

public class APJClientEvents {
	private Item lastCheckedItem = null;
	long LastItemTimeStamp = 0;
	private List<Component> lastCheckedTooltip = new ArrayList<>();
	@SubscribeEvent(priority =  EventPriority.LOWEST)
	public void onItemTooltip(ItemTooltipEvent e){
		Item item = e.getItemStack().getItem();
		try {
			if(item == lastCheckedItem && System.currentTimeMillis() - LastItemTimeStamp < 500){ //already generated once, so no need to regenerate it (unless a second has passed)
				var tooltip = e.getToolTip();
				tooltip.addAll(lastCheckedTooltip);
			}
			else if (ItemSetManager.SetsByItems.containsKey(item)) {
				LastItemTimeStamp = System.currentTimeMillis();
				ItemSet set = ItemSetManager.SetsByItems.get(item);
				List<Item> worn = ItemSetManager.GetWornItems(e.getEntity(),set);

				var tooltip = e.getToolTip();
				lastCheckedTooltip.clear();
				lastCheckedTooltip.add(Component.empty());//Empty line

				//Set Name (0/5)
				lastCheckedTooltip.add(Component.literal(set.getName() + " (" + worn.size() + "/" + set.GetItems().size() + ")").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD));
				//Listing of items (Gray/Gold)
				for (Item i : set.GetItems()) {
					if (worn.contains(i))
						lastCheckedTooltip.add(Component.literal("  " + i.getDescription().getString()).withStyle(ChatFormatting.GOLD));
					else
						lastCheckedTooltip.add(Component.literal("  " + i.getDescription().getString()).withStyle(ChatFormatting.DARK_GRAY));
				}
				lastCheckedTooltip.add(Component.empty());//Empty line
				//Listing of set bonuses (Gray/Green)
				for (var power : set.getBonuses()) {
					if (worn.size() >= power.getRequiredItems())
						lastCheckedTooltip.add(Component.literal("(" + power.getRequiredItems() + ") Set: " + power.getTooltip()).withStyle(ChatFormatting.GREEN));
					else
						lastCheckedTooltip.add(Component.literal("(" + power.getRequiredItems() + ") Set: " + power.getTooltip()).withStyle(ChatFormatting.DARK_GRAY));
				}
				lastCheckedItem = item;
				tooltip.addAll(lastCheckedTooltip);
			}
		}
		catch (Exception ex){
			APJRelitCore.LOGGER.info(ex.toString());
		}
	}
}
