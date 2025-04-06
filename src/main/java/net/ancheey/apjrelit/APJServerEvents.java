package net.ancheey.apjrelit;

import net.ancheey.apjrelit.itemsets.ItemSetManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class APJServerEvents {
	@SubscribeEvent(priority =  EventPriority.HIGH)
	public void onEquipmentChange(LivingEquipmentChangeEvent event){
		var fromItem = event.getFrom().getItem();
		var toItem = event.getTo().getItem();
		if(ItemSetManager.SetsByItems.containsKey(fromItem)){
			var unequippedSet = ItemSetManager.SetsByItems.get(fromItem);
			var worn = ItemSetManager.GetWornItems(event.getEntity(),unequippedSet);
			APJRelitCore.LOGGER.info("Removing bonus for "+worn.size()+1+" items");
			unequippedSet.getBonuses().stream()
					.filter(b -> b.getRequiredItems() == worn.size()+1)
					.forEach(b-> ItemSetManager.UndoSetBonus(event.getEntity(),b)); //remove all bonuses that require an item more than currently equipped
		}
		if(ItemSetManager.SetsByItems.containsKey(toItem)){
			var equippedSet = ItemSetManager.SetsByItems.get(toItem);
			var worn = ItemSetManager.GetWornItems(event.getEntity(),equippedSet);
			equippedSet.getBonuses().stream()
					.filter(b -> b.getRequiredItems() == worn.size())
					.forEach(b-> ItemSetManager.ApplySetBonus(event.getEntity(),b)); //apply a set bonus that matches the amount of items on
		}
	}
}
