package net.ancheey.apjrelit.parties;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.function.Supplier;

public class STCPartySyncPacket {
		private final List<UUID> members;

	public STCPartySyncPacket(List<UUID> playerUUID) {
		this.members = playerUUID;
	}

	public static void encode(STCPartySyncPacket msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.members.size());
		for (UUID uuid : msg.members) {
			buf.writeUUID(uuid);
		}
		}
		public static STCPartySyncPacket decode(FriendlyByteBuf buf) {
			int size = buf.readInt();
			List<UUID> members = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				members.add(buf.readUUID());
			}
			return new STCPartySyncPacket(members);
		}
		public static void handle(STCPartySyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				var grp = LocalPartyManager.getGroup();
				grp.Clear();

				Minecraft mc = Minecraft.getInstance();
				if (mc.level == null) return;

				for (var member : msg.members) {
					Player target = mc.level.getPlayerByUUID(member);
					if (target == null) continue;
					grp.add(target);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
