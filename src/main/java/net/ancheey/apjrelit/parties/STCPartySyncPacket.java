package net.ancheey.apjrelit.parties;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.function.Supplier;

public class STCPartySyncPacket {
		private final List<UUID> members;

	public STCPartySyncPacket(List<UUID> players) {
		members = players;
	}
	public STCPartySyncPacket(ServerPlayerGroup group) {
		members = new ArrayList<>();
		var players = group.GetPlayers();
		for(var player : players)
			members.add(player.getUUID());
	}

	public static void encode(STCPartySyncPacket msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.members.size());
		for (UUID uuid : msg.members) {
			buf.writeUUID(uuid);
		}
		}
		public static STCPartySyncPacket decode(FriendlyByteBuf buf) {
			int size = buf.readInt();
			List< UUID> members = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				var uuid = buf.readUUID();
				members.add(uuid);
			}
			return new STCPartySyncPacket(members);
		}
		public static void handle(STCPartySyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				Minecraft mc = Minecraft.getInstance();
				if (mc.level == null) return;
				for (var member : msg.members) {
					Player target = mc.level.getPlayerByUUID(member);
					if (target == null) continue;
					LocalPlayerGroup.add(target);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
