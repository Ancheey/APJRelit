package net.ancheey.apjrelit.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.ancheey.apjrelit.APJAttributeRegistry;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.enmity.EnmityManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import java.util.List;

public class ProvokeSpell extends AbstractSpell {
	private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"provoke");
	private final DefaultConfig defaultConfig = new DefaultConfig()
			.setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
			.setMaxLevel(3).setCooldownSeconds(10).setAllowCrafting(false).build();

	public ProvokeSpell(){
		this.manaCostPerLevel = 3;
		this.baseManaCost = 7;
		this.spellPowerPerLevel = 0;
		this.castTime = 0;
	}

	@Override
	public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		if(playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
			var targetEntity = targetData.getTarget((ServerLevel) level);
			if (targetEntity != null) {
				int enmity = getEnmity(spellLevel,entity);
				var enmityData = EnmityManager.getEntityData(targetEntity);
				if(enmityData != null){
					var top = enmityData.getTopEnmity();
					enmityData.Set(entity,top+1+enmity);
					APJRelitCore.LOGGER.info("Provoked. Setting enmity to: "+top+1+enmity+" Top player: "+enmityData.getTopEnmityEntity().getDisplayName().getString() + " with"+ top);
				}
			}
		}
		super.onCast(level, spellLevel, entity, castSource, playerMagicData);
	}

	@Override
	public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
		var bool = Utils.preCastTargetHelper(level,entity,playerMagicData,this,6,0.15f);
		if(bool && playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
			var targetEntity = targetData.getTarget((ServerLevel) level);
			return targetEntity != null && EnmityManager.getEntityData(targetEntity) != null;
		}
		return false;
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
		return List.of(Component.translatable("ui.apjrelit.provoke_description"),
				Component.translatable("ui.apjrelit.provoke_additional_enmity",getEnmity(spellLevel,caster)));
	}
	private int getEnmity(int spellLevel, LivingEntity caster){
		return 4 +(int)((caster.getAttributeValue(Attributes.ARMOR_TOUGHNESS)/(5-spellLevel)) * caster.getAttributeValue(APJAttributeRegistry.ENMITY_MODIFIER.get()));
	}
}
