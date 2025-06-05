package net.ancheey.apjrelit.parties;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class STCPartyInvitePacket {
	private final String inviter;
	private final long timestamp;
	public STCPartyInvitePacket(String inviter, long timestamp) {
		this.inviter = inviter;
		this.timestamp = timestamp;
	}
	public static void encode(STCPartyInvitePacket msg, FriendlyByteBuf buf) {
		buf.writeLong(msg.timestamp);
		buf.writeInt(msg.inviter.length());
		for(int i = 0; i < msg.inviter.length();i++){
			buf.writeChar(msg.inviter.charAt(i));
		}
	}
	public static STCPartyInvitePacket decode(FriendlyByteBuf buf) {
		var stamp = buf.readLong();
		var len = buf.readInt();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < len; i++){
			sb.append(buf.readChar());
		}
		return new STCPartyInvitePacket(sb.toString(),stamp);
	}
	public static void handle(STCPartyInvitePacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			LocalPlayerParty.setInvite(msg.inviter, msg.timestamp);
		});
		ctx.get().setPacketHandled(true);
	}
}
