package net.ancheey.apjrelit.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.APJSoundRegistry;
import net.ancheey.apjrelit.enmity.EnmityManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class IntimidateSpell extends AbstractSpell {
	private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"intimidate");
	private final DefaultConfig defaultConfig = new DefaultConfig()
			.setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
			.setMaxLevel(1).setCooldownSeconds(120).setAllowCrafting(false).build();

	public IntimidateSpell(){
		this.manaCostPerLevel = 80;
		this.baseManaCost = 23;
		this.spellPowerPerLevel = 0;
		this.castTime = 0;
	}

	@Override
	public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		var entities = level.getEntitiesOfClass(LivingEntity.class,entity.getBoundingBox().inflate(10), e-> e != entity);
		for(var e : entities){
			if(e.isAlliedTo(entity))
				continue;
			var enmityData = EnmityManager.getEntityData(e);
			if(enmityData != null){
				var top = enmityData.getTopEnmity();
				enmityData.Set(entity,top+1);
			}
		}
		level.playSound(null, entity.getOnPos().above(1), APJSoundRegistry.INTIMIDATE.get(), SoundSource.PLAYERS,3f,0.7f);
		super.onCast(level, spellLevel, entity, castSource, playerMagicData);
	}

	@Override
	public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
		return true;
	}

	@Override
	public ResourceLocation getSpellResource() {
		return spellId;
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	@Override
	public CastType getCastType() {
		return CastType.INSTANT;
	}
	@Override
	public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
		return List.of(Component.translatable("ui.apjrelit.intimidate_description"));
	}
}
