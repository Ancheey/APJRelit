package net.ancheey.apjrelit.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.network.NetworkHandler;
import net.ancheey.apjrelit.parties.STCPlayerOperationPacket;
import net.ancheey.apjrelit.parties.ServerPartyManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
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
		dispatcher.register(Commands.literal("invite").requires(CommandSourceStack::isPlayer).executes(APJCommands::invite));
		dispatcher.register(Commands.literal("beginvite").requires(CommandSourceStack::isPlayer).executes(APJCommands::begInvite));
		dispatcher.register(Commands.literal("accept").requires(CommandSourceStack::isPlayer).executes(APJCommands::accept));
	}
	private static int invite(CommandContext<CommandSourceStack> command) {
		try {
			ServerPlayer player = command.getSource().getPlayer();
			//simulating self joining the pt
			if(beggar != null){
				APJRelitCore.LOGGER.info(ServerPartyManager.InvitePlayer(player,beggar).msg());
			}
		}
		catch(Exception e){
			APJRelitCore.LOGGER.info(e.toString());
		}
		return Command.SINGLE_SUCCESS;
	}
	private static ServerPlayer beggar;
	private static int begInvite(CommandContext<CommandSourceStack> command) {
		try {
			ServerPlayer player = command.getSource().getPlayer();
			//simulating self joining the pt
			beggar = player;
		}
		catch(Exception e){
			APJRelitCore.LOGGER.info(e.toString());
		}
		return Command.SINGLE_SUCCESS;
	}
	private static int accept(CommandContext<CommandSourceStack> command) {
		try {
			ServerPlayer player = command.getSource().getPlayer();
			//simulating self joining the pt
			APJRelitCore.LOGGER.info(ServerPartyManager.acceptInvite(player).msg());
		}
		catch(Exception e){
			APJRelitCore.LOGGER.info(e.toString());
		}
		return Command.SINGLE_SUCCESS;
	}

}
