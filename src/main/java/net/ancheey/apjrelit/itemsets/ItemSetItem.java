package net.ancheey.apjrelit.itemsets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Optional;

public class ItemSetItem {
	private String id;
	private int type;
	private boolean itemChecked = false;
	private Item item;

	public String getItemId() {
		return id;
	}
public Optional<Item> getItem(){
		if(item == null && !itemChecked){
			@Nullable Item it = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(getItemId()));
			item = it;
			itemChecked = true;
		}
		return Optional.ofNullable(item);
}
	public int getItemType() {
		return type;
	}
}
