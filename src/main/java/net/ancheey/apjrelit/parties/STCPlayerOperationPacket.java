package net.ancheey.apjrelit.parties;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class STCPlayerOperationPacket {
	private final UUID playerUUID;
	private final Operation operation;

	public STCPlayerOperationPacket(UUID playerUUID, Operation operation) {
		this.playerUUID = playerUUID;
		this.operation = operation;
	}
	public static void encode(STCPlayerOperationPacket msg, FriendlyByteBuf buf) {
		buf.writeUUID(msg.playerUUID);
		buf.writeByte(msg.operation.toByte());
	}
	public static STCPlayerOperationPacket decode(FriendlyByteBuf buf) {
		UUID uuid = buf.readUUID();
		Operation op = Operation.fromByte(buf.readByte());
		return new STCPlayerOperationPacket(uuid, op);
	}

	public static void handle(STCPlayerOperationPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			if (mc.level == null) return;

			Player target = mc.level.getPlayerByUUID(msg.playerUUID);
			if (target == null) return;


			switch (msg.operation) {
				case ADD -> {
					LocalPlayerGroup.add(target);
				}
				case REMOVE -> {
					LocalPlayerGroup.remove(target);
				}
				case LEAD -> {
					LocalPlayerGroup.assignLeader(target);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public enum Operation {
		ADD, REMOVE, LEAD;

		public static Operation fromByte(byte b) {
			return values()[b];
		}

		public byte toByte() {
			return (byte) this.ordinal();
		}
	}
}
