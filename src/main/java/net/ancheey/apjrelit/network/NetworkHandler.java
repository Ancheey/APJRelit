package net.ancheey.apjrelit.network;

import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.parties.STCPlayerOperationPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

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
