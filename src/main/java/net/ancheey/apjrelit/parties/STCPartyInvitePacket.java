package net.ancheey.apjrelit.parties;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class STCPartyInvitePacket {
	private final UUID inviter;
	private final long timestamp;
	public STCPartyInvitePacket(UUID inviter, long timestamp) {
		this.inviter = inviter;
		this.timestamp = timestamp;
	}
	public static void encode(STCPartyInvitePacket msg, FriendlyByteBuf buf) {
		buf.writeUUID(msg.inviter);
		buf.writeLong(msg.timestamp);
	}
	public static STCPartyInvitePacket decode(FriendlyByteBuf buf) {
		return new STCPartyInvitePacket(buf.readUUID(),buf.readLong());
	}
	public static void handle(STCPartyInvitePacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			if (mc.level == null) return;

			Player target = mc.level.getPlayerByUUID(msg.inviter);
			if (target == null) return;

			LocalPlayerParty.setInvite(target,msg.timestamp);
		});
		ctx.get().setPacketHandled(true);
	}
}
