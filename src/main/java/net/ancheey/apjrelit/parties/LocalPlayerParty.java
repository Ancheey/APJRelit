package net.ancheey.apjrelit.parties;

import net.ancheey.apjrelit.network.NetworkHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class LocalPlayerParty {
	private static List<Player> members = new ArrayList<>();
	private static @Nullable LocalPlayerInvite invite;
	public static void syncParty(List<Player> players){
		members.clear();
		members.addAll(players);
	}
	public static void clear(){
		members.clear();
	}
	public static void add(Player player){
		members.add(player);
	}
	public static void remove(Player player){
		members.remove(player);
	}
	public static boolean isInParty(){
		return members.size() > 1; //at least one more person
	}
	public static List<Player> getMembers(){
		return List.copyOf(members);
	}
	public static @Nullable Player getLeader(){
		if(members.size()>0)
			return members.get(0);
		return null;
	}
	public static boolean hasPlayer(Player player){
		return members.contains(player);
	}
	public static boolean assignLeader(Player player){
		if(!members.contains(player) || getLeader() == player)
			return false;
		members.remove(player);
		members.add(0,player);
		return true;
	}
	public static void setInvite(Player inviter, long timestamp){
		invite = new LocalPlayerInvite(inviter,timestamp);
	}
	public static LocalPlayerInvite getInvite(){
		return invite;
	}
	public static void acceptCurrentInvite(){
		NetworkHandler.sendToServer(new CTSPartyInviteResponsePacket(CTSPartyInviteResponsePacket.InviteResponse.ACCEPT));
	}
	public static void declineInvite(){
		NetworkHandler.sendToServer(new CTSPartyInviteResponsePacket(CTSPartyInviteResponsePacket.InviteResponse.DECLINE));
	}
}
