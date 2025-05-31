package net.ancheey.apjrelit.item;

import java.util.ArrayList;
import java.util.List;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.item.renderer.BasicGeoItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.RenderUtils;

import java.util.function.Consumer;

public class SpellboundGeoWeaponItem extends BasicGeoWeaponItem implements GeoItem, IPresetSpellContainer {
	public SpellboundGeoWeaponItem(String modelIdentifier, float swingsPerSecond, Rarity rarity) {
		super(modelIdentifier, swingsPerSecond, rarity);
	}
	List<spellPair> spellList = new ArrayList<>();
	public SpellboundGeoWeaponItem addSpell(ResourceLocation spell, int level){
		spellList.add(new spellPair(spell, level));
		return this;
	}

	@Override
	public void initializeSpellContainer(ItemStack itemStack) {
		if(itemStack == null)
			return;
		if(!ISpellContainer.isSpellContainer(itemStack)){
			var spellContainer = ISpellContainer.create(spellList.size(),true,false);
			for(var s : spellList){
				spellContainer.addSpell(SpellRegistry.getSpell(s.spell),s.level,true,itemStack);
			}
			spellContainer.save(itemStack);
		}
	}
	private static class spellPair{
		public spellPair(ResourceLocation spell, int level) {
			this.spell = spell;
			this.level = level;
		}

		public ResourceLocation spell;
		public int level;

	}
}
