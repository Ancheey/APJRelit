package net.ancheey.apjrelit.dndmodule;

import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.armor.APJArmorItem;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.ancheey.apjrelit.item.APJSwordItem;
import net.ancheey.apjrelit.item.BasicGeoChargedProjectileWeapon;
import net.ancheey.apjrelit.item.BasicGeoShieldItem;
import net.bettercombat.BetterCombat;
import net.bettercombat.client.BetterCombatClient;
import net.bettercombat.forge.BetterCombatForge;
import net.bettercombat.logic.ItemStackNBTWeaponAttributes;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.common.extensions.IForgeItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

import static net.ancheey.apjrelit.itemsets.APJSetModuleEventHandler.AddSetItemTooltip;

public class APJItemEvents {


	private Dictionary<Player,ItemStack> lastCheckedItem = new Hashtable<>();
	private Dictionary<Player,Long> LastItemTimeStamp = new Hashtable<>() ;
	private final Dictionary<Player, List<Component>> lastCheckedTooltip = new Hashtable<>();


	@SubscribeEvent(priority =  EventPriority.LOWEST)
	public void onItemTooltip(ItemTooltipEvent e){
		if(e.getEntity() == null)
			return;
		else if(lastCheckedTooltip.get(e.getEntity()) == null){
			//lastCheckedItem.put(e.getEntity(), null);
			LastItemTimeStamp.put(e.getEntity(), 0L);
			lastCheckedTooltip.put(e.getEntity(), new ArrayList<>());
		}
		Player player = e.getEntity();
		ItemStack item = e.getItemStack();
		if(item == lastCheckedItem.get(player) && System.currentTimeMillis() - LastItemTimeStamp.get(player) < 500){ //already generated once, so no need to regenerate it (unless a second has passed)
			var tooltip = e.getToolTip();
			tooltip.addAll(lastCheckedTooltip.get(player));
			return;
		}

		if(item.getItem() instanceof SwordItem){
			ItemSuitableForTooltip(e);
			AddWeaponTooltip(e);
			AddBonusStatsTooltip(e);
		}
		else if(item.getItem() instanceof  BasicGeoChargedProjectileWeapon){
			ItemSuitableForTooltip(e);
			AddChargedRangedWeaponTooltip(e);
			AddBonusStatsTooltip(e);
		}
		else if(item.getItem() instanceof  BasicGeoShieldItem){
			ItemSuitableForTooltip(e);
			AddShieldTooltip(e);
			AddBonusStatsTooltip(e);
		}
		else if(item.getItem() instanceof APJArmorItem){
			ItemSuitableForTooltip(e);
			AddArmorTooltip(e);
			AddDefaultStatsTooltip(e);
			AddBonusStatsTooltip(e);
		}
		AddSetItemTooltip(e);
	}
	private void ItemSuitableForTooltip(ItemTooltipEvent e){
		e.getToolTip().clear();
		ItemStack item = e.getItemStack();
		e.getToolTip().add(Component.literal(item.getItem().getDescription().getString()).withStyle(item.getRarity().getStyleModifier()));
	}
	private void AddWeaponTooltip(ItemTooltipEvent e){
		ItemStack item = e.getItemStack();

		var attribs = WeaponRegistry.getAttributes(item);
		//decide weapon category, speed, reach
		String weaponTypeText = "";
		if(attribs.isTwoHanded())
			weaponTypeText += "Two-hand ";
		else
			weaponTypeText += "One-hand ";
		var category = attribs.category();
		weaponTypeText += category.substring(0,1).toUpperCase() + category.substring(1);
		e.getToolTip().add(Component.literal(weaponTypeText).withStyle(ChatFormatting.WHITE));
		double speed = 4 + AttributeHelper.GetValue(item, Attributes.ATTACK_SPEED, EquipmentSlot.MAINHAND);

		var range  = attribs.attackRange();

		AddDefaultStatsTooltip(e);
		e.getToolTip().add(Component.literal("Reach: "+ String.format("%.1f",range) + "  Speed: " + String.format("%.1f",speed)).withStyle(ChatFormatting.WHITE));
	}
	private void AddChargedRangedWeaponTooltip(ItemTooltipEvent e){
		var wepbase = (BasicGeoChargedProjectileWeapon) e.getItemStack().getItem();
		e.getToolTip().add(Component.literal("Charged Ranged Weapon"));
		AddDefaultStatsTooltip(e);
		e.getToolTip().add(Component.literal("Reach: "+String.format("%.1f",(double)wepbase.getDefaultProjectileRange()) + "  Speed: " + String.format("%.1f",wepbase.getNookSpeed())).withStyle(ChatFormatting.WHITE));
	}
	private void AddShieldTooltip(ItemTooltipEvent e){
		var shieldbase = (BasicGeoShieldItem) e.getItemStack().getItem();
		var block = AttributeHelper.GetValue(e.getItemStack(),APJAttributeRegistry.BLOCK_AMOUNT.get(),EquipmentSlot.OFFHAND);
		e.getToolTip().add(Component.literal("Offhand Shield"));
		AddDefaultStatsTooltip(e);
	}
	private void AddArmorTooltip(ItemTooltipEvent e){
		var type = ((ArmorItem)e.getItemStack().getItem()).getMaterial().getName();
		type = type.substring(0,1).toUpperCase() + type.substring(1);
		var eqslot = LivingEntity.getEquipmentSlotForItem(e.getItemStack()).getName();
		eqslot = eqslot.substring(0,1).toUpperCase() + eqslot.substring(1);
		e.getToolTip().add(Component.literal(type + " " +eqslot));
	}
	private void AddDefaultStatsTooltip(ItemTooltipEvent e){
		var eqslot = LivingEntity.getEquipmentSlotForItem(e.getItemStack());
		if(e.getItemStack().getItem() instanceof SwordItem)
			e.getToolTip().add(Component.literal("{apj.weapon.damage.dice}"));
		else if(e.getItemStack().getItem()  instanceof  BasicGeoChargedProjectileWeapon)
			e.getToolTip().add(Component.literal("{apj.weapon.damage.power}"));
		else if(e.getItemStack().getItem()  instanceof  BasicGeoShieldItem){
			var block = (int)AttributeHelper.GetValue(e.getItemStack(),APJAttributeRegistry.BLOCK_AMOUNT.get(),EquipmentSlot.OFFHAND);
			e.getToolTip().add(Component.literal(block + " Block"));
			eqslot = EquipmentSlot.OFFHAND;
		}
		int toughness = (int)AttributeHelper.GetValue(e.getItemStack(), Attributes.ARMOR_TOUGHNESS, eqslot);
		var armor = (int)AttributeHelper.GetValue(e.getItemStack(),Attributes.ARMOR,eqslot);
		if(armor > 0)
			e.getToolTip().add(Component.literal(armor + " Armor"));
		if(toughness > 0)
			e.getToolTip().add(Component.literal(toughness + " Toughness"));
	}
	private void AddBonusStatsTooltip(ItemTooltipEvent e){
		var eqslot = LivingEntity.getEquipmentSlotForItem(e.getItemStack());
		if(!(e.getItemStack().getItem() instanceof SwordItem)) {
			var die = (int)AttributeHelper.GetValue(e.getItemStack(), APJAttributeRegistry.ATTACK_PRECISE_BLOW.get(), eqslot);
			if(die> 0)
				e.getToolTip().add(Component.literal("+" +die+ " Precise Blows"));

			die = (int)AttributeHelper.GetValue(e.getItemStack(), APJAttributeRegistry.ATTACK_GREAT_BLOW.get(), eqslot);
			if(die> 0)
				e.getToolTip().add(Component.literal("+" +die+ " Great Blows"));

			die = (int)AttributeHelper.GetValue(e.getItemStack(), APJAttributeRegistry.ATTACK_GOOD_BLOW.get(), eqslot);
			if(die> 0)
				e.getToolTip().add(Component.literal("+" +die+ " Good Blows"));

			die = (int)AttributeHelper.GetValue(e.getItemStack(), APJAttributeRegistry.ATTACK_FINE_BLOW.get(), eqslot);
			if(die> 0)
				e.getToolTip().add(Component.literal("+" +die+ " Fine Blows"));

			die = (int)AttributeHelper.GetValue(e.getItemStack(), APJAttributeRegistry.ATTACK_CONNECTING_BLOW.get(), eqslot);
			if(die> 0)
				e.getToolTip().add(Component.literal("+" +die+ " Connecting Blows"));
		}
		else if(!(e.getItemStack().getItem()  instanceof  BasicGeoChargedProjectileWeapon)){
			var die = (int)AttributeHelper.GetValue(e.getItemStack(), APJAttributeRegistry.SHOOT_STRONG.get(), eqslot);
			if(die> 0)
				e.getToolTip().add(Component.literal("+" +die+ " Strong Shots"));

			die = (int)AttributeHelper.GetValue(e.getItemStack(), APJAttributeRegistry.SHOOT_GOOD.get(), eqslot);
			if(die> 0)
				e.getToolTip().add(Component.literal("+" +die+ " Good Shots"));

			die = (int)AttributeHelper.GetValue(e.getItemStack(), APJAttributeRegistry.SHOOT_QUICK.get(), eqslot);
			if(die> 0)
				e.getToolTip().add(Component.literal("+" +die+ " Quick Shots"));
		}
		else if(!(e.getItemStack().getItem()  instanceof  BasicGeoShieldItem)){
			var block = (int)AttributeHelper.GetValue(e.getItemStack(),APJAttributeRegistry.BLOCK_AMOUNT.get(),eqslot);
			if(block > 0)
				e.getToolTip().add(Component.literal("+"+block + " Block"));
		}
		var value = (int)AttributeHelper.GetValue(e.getItemStack(), Attributes.MAX_HEALTH, eqslot);
		if(value> 0)
			e.getToolTip().add(Component.literal("+" +value+ " Max Health"));
	}
}
