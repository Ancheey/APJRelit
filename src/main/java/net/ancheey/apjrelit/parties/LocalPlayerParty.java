package net.ancheey.apjrelit.parties;

import net.ancheey.apjrelit.network.NetworkHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class LocalPlayerParty {
	private static final List<Player> members = new ArrayList<>();
	public static int AwayPlayers = 0;
	private static @Nullable LocalPlayerInvite invite;
	public static void syncParty(List<Player> players){
		members.clear();
		members.addAll(players);
	}
	public static void clear(){
		AwayPlayers=0;
		members.clear();
	}
	public static void add(Player player){
		members.add(player);
	}
	public static void remove(Player player){
		members.remove(player);
	}
	public static boolean isInParty(){
		return members.size() + AwayPlayers > 1; //at least one more person
	}
	public static List<Player> getLevelMembers(){
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
	public static void assignLeader(Player player){
		if(!members.contains(player) || getLeader() == player)
			return;
		members.remove(player);
		members.add(0,player);
	}
	public static void setInvite(String inviter, long timestamp){
		invite = new LocalPlayerInvite(inviter,timestamp);
	}
	public static @Nullable LocalPlayerInvite getInvite(){
		if(invite != null &&!invite.EnsureValid()) {
			declineInvite();
		}
		return invite;
	}
	public static void acceptCurrentInvite(){
		InvalidateInvite();
		NetworkHandler.sendToServer(new CTSPartyInviteResponsePacket(CTSPartyInviteResponsePacket.InviteResponse.ACCEPT));
	}
	public static void declineInvite(){
		InvalidateInvite();
		NetworkHandler.sendToServer(new CTSPartyInviteResponsePacket(CTSPartyInviteResponsePacket.InviteResponse.DECLINE));
	}
	public static void InvalidateInvite(){
		invite = null;
	}
}
