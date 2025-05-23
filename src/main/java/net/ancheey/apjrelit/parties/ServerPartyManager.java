package net.ancheey.apjrelit.parties;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerPartyManager {
	private static final Map<ServerPlayer, ServerPlayerParty> partiesPerPlayer = new HashMap<>();
	private static final Map<ServerPlayer,ServerPlayerInvite> playerInvites = new HashMap<>();

	public static @Nullable ServerPlayerParty GetPlayerParty(Player player){
		if(!partiesPerPlayer.containsKey(player))
			return null;
		return partiesPerPlayer.get(player);
	}
	public static ReturnMessage InvitePlayer(ServerPlayer inviter, ServerPlayer invitee){
		if(partiesPerPlayer.containsKey(invitee))
			return ReturnMessage.INGRP; //invitee already in a group
		if(playerInvites.containsKey(invitee) && playerInvites.get(invitee).IsValid())
			return ReturnMessage.HASINVITE; //has a valid invite
		if(partiesPerPlayer.containsKey(inviter)) { //inviter has party
			if (partiesPerPlayer.get(inviter).getLeader() != inviter)
				return ReturnMessage.NOTLEAD; //inviter isn't a leader
			else if(!partiesPerPlayer.get(inviter).isFull())
				playerInvites.put(invitee,new ServerPlayerInvite(inviter, partiesPerPlayer.get(inviter)));
			else
				return ReturnMessage.FULL; //party is full
		}
		else{ //inviter doesn't have a party yet, so we create one
			var pt = new ServerPlayerParty().add(inviter);
			partiesPerPlayer.put(inviter, pt);
			playerInvites.put(invitee,new ServerPlayerInvite(inviter,pt)); //send invite to player STCPlayerGroupInvite

		}
		return ReturnMessage.GOOD;
	}
	public static ReturnMessage acceptInvite(ServerPlayer invitee){
		if(!playerInvites.containsKey(invitee))
			return ReturnMessage.OLD; //fabricated or old invite
		var invite = playerInvites.remove(invitee);
		if(!invite.IsValid())
			return ReturnMessage.OLD; //invalid invite
		if(invite.party.isFull())
			return ReturnMessage.FULL; //party is full
		if(invite.party.count() == 0)
			return ReturnMessage.OLD; //party is empty (closed before accepting)
		invite.party.add(invitee);
		partiesPerPlayer.put(invitee,invite.party);
		return ReturnMessage.GOOD;
	}
	public static void declineInvite(ServerPlayer invitee){
		var invite = playerInvites.remove(invitee);
		invite.party.pendingInvites.remove(invite);
		if(invite.party.pendingInvites.size() == 0 && invite.party.count() == 1) //disbanded only pending invite and the lead is left alone;
			disbandParty(invite.party.getLeader());
	}
	public static void removePlayer(ServerPlayer leader, ServerPlayer kicked){
		var pt = partiesPerPlayer.get(leader);
		if(pt != null && pt.hasPlayer(kicked) && pt.getLeader() == leader){
			pt.remove(kicked);
			partiesPerPlayer.remove(kicked);

			if(pt.count() == 1){ //only leader left - disband party
				disbandParty(leader);
			}
		}
	}
	public static void forceRemovePlayer(ServerPlayer kicked){
		var pt = partiesPerPlayer.get(kicked);
		if(pt != null && pt.hasPlayer(kicked)){
			pt.remove(kicked);
			partiesPerPlayer.remove(kicked);

			if(pt.count() == 1){ //only leader left - disband party
				disbandParty(pt.getLeader());
			}
		}
	}
	public static void disbandParty(ServerPlayer leader){
		var pt = partiesPerPlayer.get(leader);
		if(pt.getLeader() == leader){
			var players = pt.clear();
			for(var p : players)
				partiesPerPlayer.remove(p);
		}
	}
	public  static ReturnMessage passLeader(ServerPlayer leader, ServerPlayer aspirant){
		var pt = partiesPerPlayer.get(leader);
		if(pt == null || !pt.hasPlayer(aspirant))
			return ReturnMessage.NOTINGRP;
		if(pt.getLeader() != leader)
			return ReturnMessage.NOTLEAD;
		pt.assignLeader(aspirant);
		return  ReturnMessage.GOOD;
	}
	//public void //adding players (invites) (creating an invite - player inviting, player targetted, max accept time
	//Accepting an invite removes all other invites that are over time or for the invited player
	//send a STCplayeropertationpacket packet to all players from the group when accepted
	//create a packet CTSInvitePlayer(PlayerUUID)
	//On SetCamera(Entity) call stcpartysyncpacket by a ctspartysyncbegpacket
	//TODO: Finish serverside parties and add a way to invite
	public enum ReturnMessage{
		GOOD,FULL,NOTLEAD,HASINVITE,OLD,INGRP,NOTINGRP;
		public String msg(){
			switch (this){
				case FULL -> {
					return "Party is full";
				}
				case GOOD -> {
					return "";
				}
				case NOTLEAD -> {
					return "You're not the leader";
				}
				case HASINVITE -> {
					return "Invitee already has a pending party invite";
				}
				case OLD -> {
					return "Invite is old or not valid";
				}
				case INGRP -> {
					return "Invitee is already in a group";
				}
			}
			return "";
		}
		public boolean value(){
			return this == GOOD;
		}
	}
}
