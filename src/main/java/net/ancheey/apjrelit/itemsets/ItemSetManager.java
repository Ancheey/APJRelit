package net.ancheey.apjrelit.itemsets;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

public class ItemSetManager {
	public static Map<String,ItemSet> ItemSets = new HashMap<>();
	public static Map<Item,ItemSet> SetsByItems = new HashMap<>();
	private static final String JSON_PATH = "/data/"+ APJRelitCore.MODID+"/sets.json";
	private static final Map<String, Consumer<LivingDamageEvent>> OnStruckScriptRegistry = new HashMap<>();
	private static final Map<String, Consumer<LivingEvent.LivingTickEvent>> OnTickScriptRegistry = new HashMap<>();
	private static final Map<String, Consumer<LivingDamageEvent>> OnDamageScriptRegistry = new HashMap<>();
	private static final Map<LivingEntity, List<Consumer<LivingDamageEvent>>> OnStruckRegistry = new HashMap<>();
	private static final Map<LivingEntity, List<Consumer<LivingEvent.LivingTickEvent>>> OnTickRegistry = new HashMap<>();
	private static final Map<LivingEntity, List<Consumer<LivingDamageEvent>>> OnDamageRegistry = new HashMap<>();

	public static boolean RegisterOnStruckScript(String key, Consumer<LivingDamageEvent> scriptEvent){
		if(OnStruckScriptRegistry.containsKey(key))
			return false;
		OnStruckScriptRegistry.put(key, scriptEvent);
		return true;
	}
	public static boolean RegisterOnTickScript(String key, Consumer<LivingEvent.LivingTickEvent> scriptEvent){
		if(OnTickScriptRegistry.containsKey(key))
			return false;
		OnTickScriptRegistry.put(key, scriptEvent);
		return true;
	}
	public static boolean RegisterOnDamageScript(String key, Consumer<LivingDamageEvent> scriptEvent){
		if(OnDamageScriptRegistry.containsKey(key))
			return false;
		OnDamageScriptRegistry.put(key, scriptEvent);
		return true;
	}
	public static List<Consumer<LivingDamageEvent>> GetEntityOnStruckSetEvents(LivingEntity e){
		if(!OnStruckRegistry.containsKey(e))
			return new ArrayList<>();
		return  OnStruckRegistry.get(e);
	}
	public static List<Consumer<LivingEvent.LivingTickEvent>> GetEntityOnTickSetEvents(LivingEntity e){
		if(!OnTickRegistry.containsKey(e))
			return new ArrayList<>();
		return  OnTickRegistry.get(e);
	}
	public static List<Consumer<LivingDamageEvent>> GetEntityOnDamageSetEvents(LivingEntity e){
		if(!OnDamageRegistry.containsKey(e))
			return new ArrayList<>();
		return  OnDamageRegistry.get(e);
	}

	public static void loadItemSets(){
		InputStream stream = ItemSetManager.class.getResourceAsStream(JSON_PATH);
		if(stream == null){
			APJRelitCore.LOGGER.info("No sets file found");
			return;
		}
		try (InputStreamReader reader = new InputStreamReader(stream)) {


			Type type = new TypeToken<Map<String,ItemSet>>(){}.getType();
			Map<String,ItemSet> sets = new Gson().fromJson(reader,type);
			ItemSets = sets;

			for(var set : sets.values()){
				APJRelitCore.LOGGER.info("Loading set: "+set.getName());
				for(var b : set.getBonuses())
					if(b.getAttributeUuid().equals(""))
						APJRelitCore.LOGGER.info("One of the bonuses doesn't have an UUID: "+set.getName());
				for(var i : set.GetItems()){
					i.getItem().ifPresent(item -> SetsByItems.put(item, set));
				}
			}
		}
		catch (IOException e){
			APJRelitCore.LOGGER.info("Error loading sets: "+ e);
		}
	}
	public static void ApplySetBonus(LivingEntity entity, ItemSetBonus bonus){
		if(bonus.getAttributeUuid().equals(""))
			throw new IllegalArgumentException("Set bonus doesn't have an UUID");
		if(!bonus.getAttribute().equals("")){
			Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse(bonus.getAttribute()));
			if(attribute != null){
			var attrinst = entity.getAttribute(attribute);
			if(attrinst != null && attrinst.getModifier(UUID.fromString(bonus.getAttributeUuid()))== null)
				attrinst.addPermanentModifier(
					new AttributeModifier(
							UUID.fromString(bonus.getAttributeUuid()),
							bonus.getTooltip(),
							bonus.getAttributeValue(),
							AttributeModifier.Operation.values()[bonus.getAttributeOperation()]));
			}
		}
		if(!bonus.getOnStruckScriptName().equals("")){
			if(!OnStruckRegistry.containsKey(entity))
				OnStruckRegistry.put(entity, new ArrayList<>());
			if(OnStruckScriptRegistry.containsKey(bonus.getOnDamageScriptName()))
				OnStruckRegistry.get(entity).add(OnStruckScriptRegistry.get(bonus.getOnDamageScriptName()));
		}
		if(!bonus.getOnTickScriptName().equals("")){
			if(!OnTickRegistry.containsKey(entity))
				OnTickRegistry.put(entity, new ArrayList<>());
			if(OnTickScriptRegistry.containsKey(bonus.getOnDamageScriptName()))
				OnTickRegistry.get(entity).add(OnTickScriptRegistry.get(bonus.getOnDamageScriptName()));
		}
		if(!bonus.getOnDamageScriptName().equals("")){
			if(!OnDamageRegistry.containsKey(entity))
				OnDamageRegistry.put(entity, new ArrayList<>());
			if(OnDamageScriptRegistry.containsKey(bonus.getOnDamageScriptName()))
				OnDamageRegistry.get(entity).add(OnDamageScriptRegistry.get(bonus.getOnDamageScriptName()));
		}
	}
	public static void UndoSetBonus(LivingEntity entity, ItemSetBonus bonus){

		if(bonus.getAttributeUuid().equals(""))
			throw new IllegalArgumentException("Set bonus doesn't have an UUID");
		if(!bonus.getAttribute().equals("")){
			Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse(bonus.getAttribute()));
			if(attribute != null){
				var attrinst = entity.getAttribute(attribute);
				if(attrinst != null)
					attrinst.removeModifier(UUID.fromString(bonus.getAttributeUuid()));
				if (attribute.getDescriptionId().equals("attribute.name.generic.max_health")) {
					float maxHealth = (float) entity.getAttributeValue(attribute);
					if (entity.getHealth() > maxHealth && bonus.getAttributeValue() > 0) {
						entity.setHealth(maxHealth);
					}
				}
			}
		}
		if(!bonus.getOnStruckScriptName().equals("")){
			if(!OnStruckRegistry.containsKey(entity))
				OnStruckRegistry.put(entity, new ArrayList<>());
			OnStruckRegistry.get(entity).remove(OnDamageScriptRegistry.get(bonus.getOnDamageScriptName()));
		}
		if(!bonus.getOnTickScriptName().equals("")){
			if(!OnTickRegistry.containsKey(entity))
				OnTickRegistry.put(entity, new ArrayList<>());
			OnTickRegistry.get(entity).remove(OnTickScriptRegistry.get(bonus.getOnDamageScriptName()));
		}
		if(!bonus.getOnDamageScriptName().equals("")){
			if(!OnDamageRegistry.containsKey(entity))
				OnDamageRegistry.put(entity, new ArrayList<>());
			OnDamageRegistry.get(entity).remove(OnDamageScriptRegistry.get(bonus.getOnDamageScriptName()));
		}
	}
	public static List<Item> GetWornItems(LivingEntity e, ItemSet set){
		List<Item> worn = new ArrayList<>();
		//check mh, oh, armor
		for(var item : set.GetItems().stream().filter(d->d.getItemType()!= 3).toList()){
			switch (item.getItemType()) {
				case 0 -> item.getItem().ifPresent(i -> {
						if (e.getMainHandItem().getItem() == i || e.getOffhandItem().getItem() == i) {
							worn.add(i);
						}
					});//Main hand or offhand
				case 1 -> item.getItem().ifPresent(i -> {
						if (e.getOffhandItem().getItem() == i) {
							worn.add(i);
						}
					});//offhand only
				case 2 -> item.getItem().ifPresent(i -> e.getArmorSlots().forEach(s -> {
					if (s.getItem() == i) {
						worn.add(i);
					}
				}));//any armor piece (head, chest, legs, boots)
			}
		}
		//check curio slots
		if(set.GetItems().stream().anyMatch(k->k.getItemType() == 3)){
			CuriosApi.getCuriosInventory(e).ifPresent(handler ->{
				var items = set.GetItems().stream().filter(d -> d.getItemType() == 3).toList();
				var curios = handler.findCurios(s->true);
				for (var item : items) {
					item.getItem().ifPresent(i->{
						for(SlotResult slot : curios) {
							if(slot.stack().getItem() == i){
								worn.add(i);
								break;
							}
						}
					});
				}
			});
		}
		return worn;
	}
}
