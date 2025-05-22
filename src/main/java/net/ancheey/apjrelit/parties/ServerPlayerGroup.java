package net.ancheey.apjrelit.parties;

import com.google.common.collect.ImmutableList;
import net.ancheey.apjrelit.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerPlayerGroup {
	public static int MAX_PARTY_SIZE = 4;
	public static int MAX_PARTIES_IN_RAID = 2;
	private List<ServerPlayer> members = new ArrayList<>();
	public boolean isFull(){
		return members.size() == MAX_PARTY_SIZE*MAX_PARTIES_IN_RAID;
	}
	public boolean isRaid(){
		return members.size() > MAX_PARTY_SIZE;
	}
	public void add(ServerPlayer player){
		if(!members.contains(player)) {
			members.add(player);
			for(var member : GetPlayers())
				NetworkHandler.sendToPlayer(new STCPlayerOperationPacket(player.getUUID(), STCPlayerOperationPacket.Operation.ADD),member);
		}
	}
	public void remove(ServerPlayer player){
		if(members.remove(player)){
			for(var member : GetPlayers())
				NetworkHandler.sendToPlayer(new STCPlayerOperationPacket(player.getUUID(), STCPlayerOperationPacket.Operation.REMOVE),member);
		}

	}
	public void clear(){
		var players = List.copyOf(GetPlayers());
		members.clear();
		for(var member : players)
			NetworkHandler.sendToPlayer(new STCPartySyncPacket(new ArrayList<>()),member);
	}
	public int count(){
		return members.size();
	}
	public ImmutableList<ServerPlayer> GetPlayers(){
		return ImmutableList.copyOf(members);
	}
	public boolean hasPlayer(ServerPlayer player){
		return members.contains(player);
	}
	public @Nullable ServerPlayer getLeader(){
		if(members.size()>0)
			return members.get(0);
		return null;
	}
	public boolean assignLeader(ServerPlayer player){
		if(!hasPlayer(player) || getLeader() == player)
			return false;
		members.remove(player);
		members.add(0,player);
		for(var member : GetPlayers())
			NetworkHandler.sendToPlayer(new STCPlayerOperationPacket(player.getUUID(), STCPlayerOperationPacket.Operation.LEAD),member);
		return true;
	}
}
