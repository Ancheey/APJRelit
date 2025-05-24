package net.ancheey.apjrelit.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.network.NetworkHandler;
import net.ancheey.apjrelit.parties.STCPlayerOperationPacket;
import net.ancheey.apjrelit.parties.ServerPartyManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
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
		dispatcher.register(Commands.literal("invite").requires(CommandSourceStack::isPlayer).then(Commands.argument("player", EntityArgument.player()).executes(APJCommands::invite)));
		dispatcher.register(Commands.literal("remove").requires(CommandSourceStack::isPlayer).then(Commands.argument("player", EntityArgument.player()).executes(APJCommands::kick)));
		dispatcher.register(Commands.literal("lead").requires(CommandSourceStack::isPlayer).then(Commands.argument("player", EntityArgument.player()).executes(APJCommands::lead)));
		dispatcher.register(Commands.literal("leave").requires(CommandSourceStack::isPlayer).executes(APJCommands::leave));
		dispatcher.register(Commands.literal("disband").requires(CommandSourceStack::isPlayer).executes(APJCommands::disband));
		dispatcher.register(Commands.literal("ptinfodump").requires(CommandSourceStack::isPlayer).requires(s-> s.hasPermission(4)).executes(APJCommands::ptinfodump));
		dispatcher.register(Commands.literal("p").requires(CommandSourceStack::isPlayer).then(Commands.argument("message", StringArgumentType.greedyString()).executes(APJCommands::partychat)));
	}
	private static final SimpleCommandExceptionType ERROR_TARGET_SELF = new SimpleCommandExceptionType(
			Component.literal("You cannot target self"));
	private static int invite(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(command, "player");
		ServerPlayer player = command.getSource().getPlayer();
		if(target == player){
			throw ERROR_TARGET_SELF.create();
		}
		var inviteret = ServerPartyManager.InvitePlayer(player,target);
		if(!inviteret.value())
			throw new SimpleCommandExceptionType(Component.literal(inviteret.msg())).create();
		return Command.SINGLE_SUCCESS;
	}
	private static int kick(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(command, "player");
		ServerPlayer player = command.getSource().getPlayer();
		if(target == player){
			throw ERROR_TARGET_SELF.create();
		}
		var inviteret = ServerPartyManager.removePlayer(player,target);
		if(!inviteret.value())
			throw new SimpleCommandExceptionType(Component.literal(inviteret.msg())).create();
		return Command.SINGLE_SUCCESS;
	}
	private static int lead(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(command, "player");
		ServerPlayer player = command.getSource().getPlayer();
		if(target == player){
			throw ERROR_TARGET_SELF.create();
		}
		var inviteret = ServerPartyManager.passLeader(player,target);
		if(!inviteret.value())
			throw new SimpleCommandExceptionType(Component.literal(inviteret.msg())).create();
		return Command.SINGLE_SUCCESS;
	}
	private static int leave(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
		ServerPlayer player = command.getSource().getPlayer();

		var ret = ServerPartyManager.forceRemovePlayer(player);
		if(!ret.value())
			throw new SimpleCommandExceptionType(Component.literal(ret.msg())).create();
		return Command.SINGLE_SUCCESS;
	}
	private static int disband(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
		ServerPlayer player = command.getSource().getPlayer();
		var inviteret = ServerPartyManager.disbandParty(player);
		if(!inviteret.value())
			throw new SimpleCommandExceptionType(Component.literal(inviteret.msg())).create();
		return Command.SINGLE_SUCCESS;
	}
	private static int ptinfodump(CommandContext<CommandSourceStack> command) {
		try {
			ServerPartyManager.getPartiesPerPlayer().forEach((k,v)->{
				APJRelitCore.LOGGER.info("[Party] "+k.getDisplayName().getString()+": " + v.count());
			});
			ServerPartyManager.getPlayerInvites().forEach((k,v)->{
				APJRelitCore.LOGGER.info("[Invite] "+k.getDisplayName().getString()+" by " + v.GetInviter().getDisplayName().getString() + " | Valid: "+ v.IsValid());
			});
		}
		catch(Exception e){
			APJRelitCore.LOGGER.info(e.toString());
		}
		return Command.SINGLE_SUCCESS;
	}
	private static int partychat(CommandContext<CommandSourceStack> command) {
		ServerPlayer player = command.getSource().getPlayer();
		var pt = ServerPartyManager.GetPlayerParty(player);
		var msg = StringArgumentType.getString(command, "message");
		if(pt != null && player != null){
			var players = pt.GetPlayers();
			for(var ptpl : players){
				ptpl.sendSystemMessage(Component.literal("[Party]<"+player.getDisplayName().getString()+">: "+ msg));
			}
		}
		return Command.SINGLE_SUCCESS;
	}

}
