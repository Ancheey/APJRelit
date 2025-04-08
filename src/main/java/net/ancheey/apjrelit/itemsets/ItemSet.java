package net.ancheey.apjrelit.itemsets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ItemSet {
	private String name;
	private List<String> items;
	private List<ItemSetBonus> bonuses;

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public List<ItemSetBonus> getBonuses() {return bonuses;}
	public void setBonuses(List<ItemSetBonus> bonuses) {this.bonuses = bonuses;}

	private final List<Item> convertedItems = new ArrayList<>();
	public List<Item> GetItems(){
		if(convertedItems.size()!= items.size()) {
			for (var a : items) {
				try {
					convertedItems.add(ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(a)));
				}
				catch(Exception ignored){}
			}
		}
		return convertedItems;
	}
}
