package net.ancheey.apjrelit.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.network.NetworkHandler;
import net.ancheey.apjrelit.parties.STCPlayerOperationPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = APJRelitCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class APJCommands {
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event){
		register(event.getDispatcher());
	}
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
		dispatcher.register(Commands.literal("apjparty").executes(APJCommands::apjparty));
	}
	private static int apjparty(CommandContext<CommandSourceStack> command) {
		try {
			ServerPlayer player = command.getSource().getPlayer();
			NetworkHandler.sendToPlayer(new STCPlayerOperationPacket(player.getUUID(), STCPlayerOperationPacket.Operation.ADD), player);
		}
		catch(Exception e){
			APJRelitCore.LOGGER.info(e.toString());
		}
		return Command.SINGLE_SUCCESS;
	}

}
