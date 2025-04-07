package net.ancheey.apjrelit;

import net.ancheey.apjrelit.itemsets.ItemSet;
import net.ancheey.apjrelit.itemsets.ItemSetManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class APJSetModuleEventHandler {

	@SubscribeEvent(priority =  EventPriority.HIGH)
	public void onEquipmentChange(LivingEquipmentChangeEvent event){
		EquipmentChange(event.getFrom().getItem(),event.getTo().getItem(),event.getEntity());
	}
	@SubscribeEvent(priority =  EventPriority.HIGH)
	public void onCurioChange(CurioChangeEvent event){
		EquipmentChange(event.getFrom().getItem(),event.getTo().getItem(),event.getEntity());
	}
	private void EquipmentChange(Item from, Item to, LivingEntity e){
		if(ItemSetManager.SetsByItems.containsKey(from)){
			var unequippedSet = ItemSetManager.SetsByItems.get(from);
			var worn = ItemSetManager.GetWornItems(e,unequippedSet);
			APJRelitCore.LOGGER.info("Removing bonus for "+worn.size()+1+" items");
			unequippedSet.getBonuses().stream()
					.filter(b -> b.getRequiredItems() == worn.size()+1)
					.forEach(b-> ItemSetManager.UndoSetBonus(e,b)); //remove all bonuses that require an item more than currently equipped
		}
		if(ItemSetManager.SetsByItems.containsKey(to)){
			var equippedSet = ItemSetManager.SetsByItems.get(to);
			var worn = ItemSetManager.GetWornItems(e,equippedSet);
			equippedSet.getBonuses().stream()
					.filter(b -> b.getRequiredItems() == worn.size())
					.forEach(b-> ItemSetManager.ApplySetBonus(e,b)); //apply a set bonus that matches the amount of items on
		}
	}

	private Item lastCheckedItem = null;
	long LastItemTimeStamp = 0;
	private final List<Component> lastCheckedTooltip = new ArrayList<>();
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
