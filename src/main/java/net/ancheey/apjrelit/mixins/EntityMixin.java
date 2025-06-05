package net.ancheey.apjrelit.mixins;

import net.ancheey.apjrelit.parties.LocalPlayerParty;
import net.ancheey.apjrelit.parties.ServerPartyManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin{
@OnlyIn(Dist.DEDICATED_SERVER)
	@Inject(
			method = "isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	private void overrideIsAlliedToServer(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
		Entity self = (Entity) (Object) this;
		// Your logic here
		if (self instanceof ServerPlayer p1 && pEntity instanceof ServerPlayer p2) {
			if(ServerPartyManager.GetPlayerParty(p1) == ServerPartyManager.GetPlayerParty(p2))
				cir.setReturnValue(true); // or false
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Inject(
			method = "isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	private void overrideIsAlliedToClient(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
		Entity self = (Entity) (Object) this;

		// Your logic here
		if (self instanceof Player p1 && pEntity instanceof Player p2) {
			if(LocalPlayerParty.hasPlayer(p2))
				cir.setReturnValue(true); // or false
		}
	}
}
