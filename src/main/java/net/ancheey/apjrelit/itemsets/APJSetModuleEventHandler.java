package net.ancheey.apjrelit.itemsets;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.gui.tooltip.DiceTooltipComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
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
		if(!(e instanceof Player))
			return;

		if(ItemSetManager.SetsByItems.containsKey(from)){
			var unequippedSet = ItemSetManager.SetsByItems.get(from);
			var worn = ItemSetManager.GetWornItems(e,unequippedSet);
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

	private static final Dictionary<Player,Item> lastCheckedItem = new Hashtable<>();
	private static final Dictionary<Player,Long> LastItemTimeStamp = new Hashtable<>() ;
	private static final Dictionary<Player,List<Component>> lastCheckedTooltip = new Hashtable<>();
	public static void AddSetItemTooltip(ItemTooltipEvent e){
		if(e.getEntity() == null)
			return;
		else if(lastCheckedTooltip.get(e.getEntity()) == null){
			//lastCheckedItem.put(e.getEntity(), null);
			LastItemTimeStamp.put(e.getEntity(), 0L);
			lastCheckedTooltip.put(e.getEntity(), new ArrayList<>());
		}


		Player player = e.getEntity();
		Item item = e.getItemStack().getItem();
		try {
			if(item == lastCheckedItem.get(player) && System.currentTimeMillis() - LastItemTimeStamp.get(player) < 500){ //already generated once, so no need to regenerate it (unless a second has passed)
				var tooltip = e.getToolTip();
				tooltip.addAll(lastCheckedTooltip.get(player));
			}
			else if (ItemSetManager.SetsByItems.containsKey(item)) {
				LastItemTimeStamp.put(player,System.currentTimeMillis());
				ItemSet set = ItemSetManager.SetsByItems.get(item);
				List<Item> worn = ItemSetManager.GetWornItems(e.getEntity(),set);

				var tooltip = e.getToolTip();
				lastCheckedTooltip.get(player).clear();
				lastCheckedTooltip.get(player).add(Component.empty());//Empty line
				//Set Name (0/5)
				lastCheckedTooltip.get(player).add(Component.literal(set.getName() + " (" + worn.size() + "/" + set.GetItems().size() + ")").withStyle(ChatFormatting.GOLD));
				//Listing of items (Gray/Gold)
				for (ItemSetItem i : set.GetItems()) {
					i.getItem().ifPresent(k->{
						if (worn.contains(k))
							lastCheckedTooltip.get(player).add(Component.literal("  " + k.getDescription().getString()).withStyle(ChatFormatting.YELLOW));
						else
							lastCheckedTooltip.get(player).add(Component.literal("  " + k.getDescription().getString()).withStyle(ChatFormatting.DARK_GRAY));
					});

				}
				lastCheckedTooltip.get(player).add(Component.empty());//Empty line
				//Listing of set bonuses (Gray/Green)
				for (var power : set.getBonuses()) {
					if (worn.size() >= power.getRequiredItems())
						lastCheckedTooltip.get(player).add(Component.literal("(" + power.getRequiredItems() + ") Set: " + power.getTooltip()).withStyle(ChatFormatting.GREEN));
					else
						lastCheckedTooltip.get(player).add(Component.literal("(" + power.getRequiredItems() + ") Set: " + power.getTooltip()).withStyle(ChatFormatting.DARK_GRAY));
				}
				lastCheckedItem.put(player,item);
				tooltip.addAll(lastCheckedTooltip.get(player));
			}
		}
		catch (Exception ex){
			APJRelitCore.LOGGER.info(ex.toString());
		}
	}
}
