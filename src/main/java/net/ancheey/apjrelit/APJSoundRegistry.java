package net.ancheey.apjrelit;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class APJSoundRegistry {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, APJRelitCore.MODID);
	public static final RegistryObject<SoundEvent> INTIMIDATE =
			SOUNDS.register("intimidate", () ->
					SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID, "intimidate"))
			);
	public static void register(IEventBus bus){
		SOUNDS.register(bus);
	}
}
