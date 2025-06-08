package net.ancheey.apjrelit.enmity;

import net.ancheey.apjrelit.parties.LocalPlayerParty;
import net.ancheey.apjrelit.parties.STCPartyInvitePacket;
import net.ancheey.apjrelit.parties.STCPartySyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class STCEnmityDataPackage {
private final List<Integer> entities = new ArrayList<>();
private final List<Float> enmities = new ArrayList<>();
private final int enmityHost;

	public STCEnmityDataPackage(LivingEntity enmityHost, EnmityManager.EnmityList enmity) {
		this.enmityHost = enmityHost.getId();
		for(var e : enmity.getEnmity()){
			entities.add(e.entity.getId());
			enmities.add(e.enmity);
		}
	}
	public STCEnmityDataPackage(int enmityHost) {
		this.enmityHost = enmityHost;
	}
	public void AddEnmity(int entity, float enmity){
		entities.add(entity);
		enmities.add(enmity);
	}
	public static void encode(STCEnmityDataPackage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.enmityHost);
		buf.writeInt(msg.entities.size());
		for (int i =0; i < msg.enmities.size();i++) {
			buf.writeInt(msg.entities.get(i));
			buf.writeFloat(msg.enmities.get(i));
		}
	}
	public static STCEnmityDataPackage decode(FriendlyByteBuf buf){
		var host = buf.readInt();
		var pkg = new STCEnmityDataPackage(host);
		var cnt = buf.readInt();
		for(int i = 0; i < cnt; i++){
			pkg.AddEnmity(buf.readInt(),buf.readFloat());
		}
		return pkg;
	}
	public static void handle(STCEnmityDataPackage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			Minecraft mc = Minecraft.getInstance();
			if (mc.level == null) return;
			if(!(mc.level.getEntity(msg.enmityHost) instanceof LivingEntity entity)) return;
			var list = new EnmityManager.EnmityList(entity);
			for (int i =0; i < msg.enmities.size();i++) {
				if(mc.level.getEntity(msg.entities.get(i)) instanceof LivingEntity enmityEntity)
					list.Add(enmityEntity, msg.enmities.get(i));
			}
			if(list != null) {
				EnmityManager.EnmityData.put(list.getHost(), list);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
