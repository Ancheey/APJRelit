package net.ancheey.apjrelit.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class APJSpellRegistry {
	private static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, APJRelitCore.MODID);
	public static final Supplier<AbstractSpell> PROVOKE_SPELL = registerSpell(new ProvokeSpell());

	public static void register(IEventBus eventBus) {
		SPELLS.register(eventBus);
	}
	private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
		return SPELLS.register(spell.getSpellName(), () -> spell);
	}
}

