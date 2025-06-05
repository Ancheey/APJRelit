package net.ancheey.apjrelit.parties;

import com.google.common.collect.ImmutableList;
import net.ancheey.apjrelit.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerPlayerParty {
	public static int MAX_PARTY_SIZE = 4;
	public static int MAX_PARTIES_IN_RAID = 2;
	private List<ServerPlayer> members = new ArrayList<>();
	public Map<ServerPlayerInvite, ServerPlayer> pendingInvites = new HashMap<>();
	public boolean isFull(){
		return members.size() == MAX_PARTY_SIZE*MAX_PARTIES_IN_RAID;
	}
	public boolean isRaid(){
		return members.size() > MAX_PARTY_SIZE;
	}
	public ServerPlayerParty add(ServerPlayer player){
		if(!members.contains(player)) {
			for(var member : GetPlayers())
				NetworkHandler.sendToPlayer(new STCPlayerOperationPacket(player.getUUID(), STCPlayerOperationPacket.Operation.ADD),member);
			members.add(player);
			NetworkHandler.sendToPlayer(new STCPartySyncPacket(this),player);
		}
		return this;
	}
	public ServerPlayerParty remove(ServerPlayer player){
		if(members.remove(player)){
			for(var member : GetPlayers())
				NetworkHandler.sendToPlayer(new STCPartySyncPacket(this),member); //syncing because removing by uuid is weird
			NetworkHandler.sendToPlayer(new STCPartySyncPacket(),player);
		}
		return this;
	}
	public void sync(){
		for(var member : GetPlayers())
			NetworkHandler.sendToPlayer(new STCPartySyncPacket(this),member); //syncing because removing by uuid is weird
	}
	public List<ServerPlayer> clear(){
		var players = List.copyOf(GetPlayers());
		members.clear();
		for(var member : players)
			NetworkHandler.sendToPlayer(new STCPartySyncPacket(),member);
		return  players;
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
	public void updatePlayerEntity(ServerPlayer player){
		for(int i = 0; i < count(); i++){
			if(members.get(i).getUUID() == player.getUUID()){
				members.remove(i);
				members.add(i,player);
				sync();
				return;
			}
		}
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
