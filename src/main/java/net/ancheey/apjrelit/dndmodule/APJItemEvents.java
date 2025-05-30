package net.ancheey.apjrelit.dndmodule;

import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.ancheey.apjrelit.item.APJSwordItem;
import net.ancheey.apjrelit.item.BasicGeoChargedProjectileWeapon;
import net.bettercombat.BetterCombat;
import net.bettercombat.client.BetterCombatClient;
import net.bettercombat.forge.BetterCombatForge;
import net.bettercombat.logic.ItemStackNBTWeaponAttributes;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
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
		}
		else if(item.getItem() instanceof  BasicGeoChargedProjectileWeapon){
			ItemSuitableForTooltip(e);
			AddChargedRangedWeaponTooltip(e);
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
			weaponTypeText += "Two-handed ";
		else
			weaponTypeText += "One-handed ";
		var category = attribs.category();
		weaponTypeText += category.substring(0,1).toUpperCase() + category.substring(1);
		e.getToolTip().add(Component.literal(weaponTypeText).withStyle(ChatFormatting.WHITE));
		double speed = 4 + AttributeHelper.GetValue(item, Attributes.ATTACK_SPEED, EquipmentSlot.MAINHAND);

		var range  = attribs.attackRange();

		e.getToolTip().add(Component.literal("{apj.weapon.damage.dice}"));
		e.getToolTip().add(Component.literal("Reach: "+ String.format("%.1f",range) + "  Speed: " + String.format("%.1f",speed)).withStyle(ChatFormatting.WHITE));
	}
	private void AddChargedRangedWeaponTooltip(ItemTooltipEvent e){
		var wepbase = (BasicGeoChargedProjectileWeapon) e.getItemStack().getItem();
		e.getToolTip().add(Component.literal("Charged Ranged Weapon"));
		e.getToolTip().add(Component.literal("{apj.weapon.damage.power}"));
		e.getToolTip().add(Component.literal("Reach: "+String.format("%.1f",(double)wepbase.getDefaultProjectileRange()) + "  Speed: " + String.format("%.1f",wepbase.getNookSpeed())).withStyle(ChatFormatting.WHITE));
	}
}
