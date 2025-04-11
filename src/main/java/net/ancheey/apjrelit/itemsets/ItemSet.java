package net.ancheey.apjrelit.itemsets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ItemSet {
	private String name;
	private List<ItemSetItem> items;
	private List<ItemSetBonus> bonuses;

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public List<ItemSetBonus> getBonuses() {return bonuses;}
	public void setBonuses(List<ItemSetBonus> bonuses) {this.bonuses = bonuses;}

	public List<ItemSetItem> GetItems(){
		return items;
	}
}
