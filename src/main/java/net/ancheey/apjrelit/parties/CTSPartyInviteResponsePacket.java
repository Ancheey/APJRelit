package net.ancheey.apjrelit.parties;

import net.ancheey.apjrelit.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CTSPartyInviteResponsePacket {
	private final  InviteResponse response;
	public CTSPartyInviteResponsePacket(InviteResponse response) {
		this.response = response;
	}
	public static void encode(CTSPartyInviteResponsePacket msg, FriendlyByteBuf buf) {
		buf.writeByte(msg.response.toByte());
	}
	public static CTSPartyInviteResponsePacket decode(FriendlyByteBuf buf) {
		return new CTSPartyInviteResponsePacket(InviteResponse.fromByte(buf.readByte()));
	}
	public static void handle(CTSPartyInviteResponsePacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			var sender = ctx.get().getSender();
			ServerPartyManager.ReturnMessage message;
			if(msg.response == InviteResponse.ACCEPT)
				message = ServerPartyManager.acceptInvite(sender);
			else
				ServerPartyManager.declineInvite(sender);
		});
		ctx.get().setPacketHandled(true);
	}
	public enum InviteResponse{
		ACCEPT,DECLINE;
		public static CTSPartyInviteResponsePacket.InviteResponse fromByte(byte b) {
			return values()[b];
		}

		public byte toByte() {
			return (byte) this.ordinal();
		}
	}
}
