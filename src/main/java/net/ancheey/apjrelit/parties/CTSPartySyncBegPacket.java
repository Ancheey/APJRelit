package net.ancheey.apjrelit.parties;

import net.ancheey.apjrelit.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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
			var sender = ctx.get().getSender();
			var grp = ServerPartyManager.GetPlayerParty(sender);
			if(grp != null)
				NetworkHandler.sendToPlayer(new STCPartySyncPacket(grp),sender);
			else
				NetworkHandler.sendToPlayer(new STCPartySyncPacket(),sender);
		});
		ctx.get().setPacketHandled(true);
	}
}
