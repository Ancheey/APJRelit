package net.ancheey.apjrelit.parties;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class CTSPartySyncBegPacket {
	private final UUID player;

	public CTSPartySyncBegPacket(UUID player) {
		this.player = player;
	}
	public static void encode(CTSPartySyncBegPacket msg, FriendlyByteBuf buf) {
		buf.writeUUID(msg.player);
	}
	public static CTSPartySyncBegPacket decode(FriendlyByteBuf buf) {
		return new CTSPartySyncBegPacket(buf.readUUID());
	}
	public static void handle(CTSPartySyncBegPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

		});
		ctx.get().setPacketHandled(true);
	}
}
