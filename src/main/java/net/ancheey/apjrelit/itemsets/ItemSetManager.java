package net.ancheey.apjrelit.itemsets;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ItemSetManager {
	public static Map<String,ItemSet> ItemSets = new HashMap<>();
	public static Map<Item,ItemSet> SetsByItems = new HashMap<>();
	private static final String JSON_PATH = "/data/"+ APJRelitCore.MODID+"/sets.json";
	private static Map<String, Consumer<LivingDamageEvent>> ScriptRegistry = new HashMap<>();
	private static Map<LivingEntity, List<String>> OnStruckRegistry = new HashMap<>();
	private static Map<LivingEntity, List<String>> OnTickRegistry = new HashMap<>();
	private static Map<LivingEntity, List<String>> OnDamageRegistry = new HashMap<>();

	public static boolean RegisterScript(String key, Consumer<LivingDamageEvent> scriptEvent){
		if(ScriptRegistry.containsKey(key))
			return false;
		ScriptRegistry.put(key, scriptEvent);
		return true;
	}
	public static List<Consumer<LivingDamageEvent>> GetEntityOnStruckSetEvents(LivingEntity e){
		List<Consumer<LivingDamageEvent>> tr = new ArrayList<>();
		if(!OnStruckRegistry.containsKey(e))
			return tr;
		for (String s: OnStruckRegistry.get(e)) {
			if(ScriptRegistry.containsKey(s))
				tr.add(ScriptRegistry.get(s));
		}
		return tr;
	}
	public static List<Consumer<LivingDamageEvent>> GetEntityOnTickSetEvents(LivingEntity e){
		List<Consumer<LivingDamageEvent>> tr = new ArrayList<>();
		if(!OnTickRegistry.containsKey(e))
			return tr;
		for (String s: OnTickRegistry.get(e)) {
			if(ScriptRegistry.containsKey(s))
				tr.add(ScriptRegistry.get(s));
		}
		return tr;
	}
	public static List<Consumer<LivingDamageEvent>> GetEntityOnDamageSetEvents(LivingEntity e){
		List<Consumer<LivingDamageEvent>> tr = new ArrayList<>();
		if(!OnDamageRegistry.containsKey(e))
			return tr;
		for (String s: OnDamageRegistry.get(e)) {
			if(ScriptRegistry.containsKey(s))
				tr.add(ScriptRegistry.get(s));
		}
		return tr;
	}
	public static void loadItemSets(){
		InputStream stream = ItemSetManager.class.getResourceAsStream(JSON_PATH);
		if(stream == null)
			APJRelitCore.LOGGER.info("No sets file found");
		try (InputStreamReader reader = new InputStreamReader(stream)) {


			Type type = new TypeToken<Map<String,ItemSet>>(){}.getType();
			Map<String,ItemSet> sets = new Gson().fromJson(reader,type);
			ItemSets = sets;

			for(var set : sets.values()){
				APJRelitCore.LOGGER.info("Loading set: "+set.getName());
				for(var b : set.getBonuses())
					if(b.getAttributeUuid()=="")
						APJRelitCore.LOGGER.info("One of the bonuses doesn't have an UUID: "+set.getName());
				for(Item i : set.GetItems()){
					SetsByItems.put(i,set);
					APJRelitCore.LOGGER.info("Added item to set: "+i.getDescription());
				}
			}
		}
		catch (IOException e){
			APJRelitCore.LOGGER.info("Error loading sets: "+ e.toString());
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
			OnStruckRegistry.get(entity).add(bonus.getOnDamageScriptName());
		}
		if(!bonus.getOnTickScriptName().equals("")){
			if(!OnTickRegistry.containsKey(entity))
				OnTickRegistry.put(entity, new ArrayList<>());
			OnTickRegistry.get(entity).add(bonus.getOnDamageScriptName());
		}
		if(!bonus.getOnDamageScriptName().equals("")){
			if(!OnDamageRegistry.containsKey(entity))
				OnDamageRegistry.put(entity, new ArrayList<>());
			OnDamageRegistry.get(entity).add(bonus.getOnDamageScriptName());
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
			OnStruckRegistry.get(entity).remove(bonus.getOnDamageScriptName());
		}
		if(!bonus.getOnTickScriptName().equals("")){
			if(!OnTickRegistry.containsKey(entity))
				OnTickRegistry.put(entity, new ArrayList<>());
			OnTickRegistry.get(entity).remove(bonus.getOnDamageScriptName());
		}
		if(!bonus.getOnDamageScriptName().equals("")){
			if(!OnDamageRegistry.containsKey(entity))
				OnDamageRegistry.put(entity, new ArrayList<>());
			OnDamageRegistry.get(entity).remove(bonus.getOnDamageScriptName());
		}
	}
	public static List<Item> GetWornItems(LivingEntity e, ItemSet set){
		List<Item> worn = new ArrayList<>();
		List<Item> yetToWear = new ArrayList<>(set.GetItems());
		List<ItemStack> toCheck = ItemSetManager.GetAllEntitySetRelevantSlots(e);

		for (var s : toCheck) {
			Item wornItem = s.getItem();
			if (yetToWear.size() == 0)
				break;
			if (s.isEmpty())
				continue;
			for (int i = 0; i < yetToWear.size(); i++) {
				if (wornItem == yetToWear.get(i)) {
					worn.add(wornItem);
					yetToWear.remove(wornItem);
					break;
				}
			}
		}
		return worn;
	}
	public static List<ItemStack> GetAllEntitySetRelevantSlots(LivingEntity e){
		List<ItemStack> toCheck = new ArrayList<>();
		toCheck.add(e.getMainHandItem()); //mainhand
		toCheck.add(e.getOffhandItem()); //offhand
		e.getArmorSlots().forEach(toCheck::add); //all armor
		CuriosApi.getCuriosInventory(e).ifPresent((curios) ->{
			var curiosslots = curios.getSlots();
			for (int i = 0; i < curiosslots; i++)
				toCheck.add(curios.getEquippedCurios().getStackInSlot(i));
		});
		return toCheck;
	}
}
