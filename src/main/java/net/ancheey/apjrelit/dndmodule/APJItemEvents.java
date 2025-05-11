package net.ancheey.apjrelit.dndmodule;

import com.google.common.collect.Multimap;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.attributes.AttributeHelper;
import net.ancheey.apjrelit.item.APJSwordItem;
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

		e.getToolTip().clear();

		e.getToolTip().add(Component.literal(item.getItem().getDescription().getString()).withStyle(ChatFormatting.BLUE));

		if(item.getItem() instanceof SwordItem){
			AddWeaponTooltip(e);
		}
		AddSetItemTooltip(e);
	}
	private void AddWeaponTooltip(ItemTooltipEvent e){
		Player player = e.getEntity();
		ItemStack item = e.getItemStack();

		//var mhDamage = (int)AttributeHelper.GetValue(item, Attributes.ATTACK_DAMAGE, EquipmentSlot.MAINHAND);
		//var mhDice = (int)AttributeHelper.GetValue(item,APJAttributeRegistry.ATTACK_DAMAGE_DICE.get(), EquipmentSlot.MAINHAND);

		//var ohDamage = (int)AttributeHelper.GetValue(item, Attributes.ATTACK_DAMAGE, EquipmentSlot.MAINHAND);
		//var ohDice = (int)AttributeHelper.GetValue(item,APJAttributeRegistry.ATTACK_DAMAGE_DICE.get(), EquipmentSlot.OFFHAND);

		int weaponType = 0; //0 = Onehand, 1 = Mainhand, 2 = Offhand

		//if(mhDice + mhDamage == 0)
		//	weaponType = 2;
		//else if(ohDamage+ohDice == 0) {
		//	weaponType = 1;
		//}
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
		String weaponPropertiesText = "";
		double speed = 4 + AttributeHelper.GetValue(item, Attributes.ATTACK_SPEED, EquipmentSlot.MAINHAND);

		var range  = attribs.attackRange();

		/*if(mhDice > 0)
			e.getToolTip().add(Component.literal(mhDamage +1+ " - " + (mhDice+mhDamage+1 + " Damage")).withStyle(ChatFormatting.WHITE));
		else
			e.getToolTip().add(Component.literal(mhDamage+1 + " Damage").withStyle(ChatFormatting.WHITE));*/
		e.getToolTip().add(Component.literal("{apj.weapon.damage.dice}"));
		e.getToolTip().add(Component.literal("Reach: "+range + "  Speed: " + String.format("%.1f",speed)).withStyle(ChatFormatting.WHITE));
	}
}
