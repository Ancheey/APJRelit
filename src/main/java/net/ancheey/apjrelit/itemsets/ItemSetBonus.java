package net.ancheey.apjrelit.itemsets;

import com.google.gson.annotations.SerializedName;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemSetBonus {
	@SerializedName("attribute_uuid")
	private String uuid;
	@SerializedName("items_required")
	private int requiredItems;
	private String tooltip;
	private String attribute;
	@SerializedName("attribute_value")
	private double attributeValue;
	@SerializedName("on_struck_script")
	private String onStruckScriptName;
	@SerializedName("on_tick_script")
	private String onTickScriptName;
	@SerializedName("on_damage_script")
	private String onDamageScriptName;
	@SerializedName("attribute_operation")
	private int attributeOperation = 0;

	private @Nullable Consumer<LivingDamageEvent> onStruckEvent;
	public String getAttributeUuid(){return uuid;}
	public int getRequiredItems() {return requiredItems;}
	public String getTooltip() {return tooltip;}
	public String getAttribute() {return attribute;}
	public double getAttributeValue() {return attributeValue;}
	public  String getOnStruckScriptName() {return onStruckScriptName;}
	public String getOnTickScriptName() {return onTickScriptName;}
	public String getOnDamageScriptName() {return onDamageScriptName;}
	public int getAttributeOperation() {return attributeOperation;}
}
