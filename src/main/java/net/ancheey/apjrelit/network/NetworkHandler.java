package net.ancheey.apjrelit.network;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.enmity.STCEnmityDataPackage;
import net.ancheey.apjrelit.parties.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Supplier;

public class NetworkHandler {
	private static SimpleChannel channel;
	private static int packetId = 0;

	private static int id() {
		return packetId++;
	}
	public static void register() {
		channel = NetworkRegistry.ChannelBuilder
				.named(ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"networkhandler"))
				.networkProtocolVersion(()->"1.0")
				.clientAcceptedVersions(s->true)
				.serverAcceptedVersions(s->true)
				.simpleChannel();

		channel.messageBuilder(STCPlayerOperationPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(STCPlayerOperationPacket::decode)
				.encoder(STCPlayerOperationPacket::encode)
				.consumerMainThread(STCPlayerOperationPacket::handle)
				.add();
		channel.messageBuilder(STCPartySyncPacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(STCPartySyncPacket::decode)
				.encoder(STCPartySyncPacket::encode)
				.consumerMainThread(STCPartySyncPacket::handle)
				.add();
		channel.messageBuilder(STCPartyInvitePacket.class,id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(STCPartyInvitePacket::decode)
				.encoder(STCPartyInvitePacket::encode)
				.consumerMainThread(STCPartyInvitePacket::handle)
				.add();
		channel.messageBuilder(CTSPartySyncBegPacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(CTSPartySyncBegPacket::decode)
				.encoder(CTSPartySyncBegPacket::encode)
				.consumerMainThread(CTSPartySyncBegPacket::handle)
				.add();
		channel.messageBuilder(CTSPartyInviteResponsePacket.class,id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(CTSPartyInviteResponsePacket::decode)
				.encoder(CTSPartyInviteResponsePacket::encode)
				.consumerMainThread(CTSPartyInviteResponsePacket::handle)
				.add();
		channel.messageBuilder(STCEnmityDataPackage.class,id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(STCEnmityDataPackage::decode)
				.encoder(STCEnmityDataPackage::encode)
				.consumerMainThread(STCEnmityDataPackage::handle)
				.add();
	}
	public static <MSG> void sendToServer(MSG message) {
		channel.sendToServer(message);
	}

	public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
		channel.send(PacketDistributor.PLAYER.with(() -> player), message);

	}

	public static <MSG> void sendToAllPlayers(MSG message) {
		channel.send(PacketDistributor.ALL.noArg(), message);
	}
}
