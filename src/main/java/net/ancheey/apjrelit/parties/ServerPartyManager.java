package net.ancheey.apjrelit.parties;

import net.ancheey.apjrelit.network.NetworkHandler;
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

	public static Map<ServerPlayer, ServerPlayerParty> getPartiesPerPlayer(){
		return partiesPerPlayer;
	}
	public static Map<ServerPlayer, ServerPlayerInvite> getPlayerInvites(){
		return playerInvites;
	}
	public static @Nullable ServerPlayerParty GetPlayerParty(ServerPlayer player){
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
			else if(!partiesPerPlayer.get(inviter).isFull()) {
				var pt = partiesPerPlayer.get(inviter);
				var invite =new ServerPlayerInvite(inviter,pt);
				playerInvites.put(invitee, new ServerPlayerInvite(inviter, partiesPerPlayer.get(inviter)));
				NetworkHandler.sendToPlayer(new STCPartyInvitePacket(inviter.getDisplayName().getString(),invite.timestamp),invitee);
			}
			else
				return ReturnMessage.FULL; //party is full
		}
		else{ //inviter doesn't have a party yet, so we create one
			var pt = new ServerPlayerParty().add(inviter);
			partiesPerPlayer.put(inviter, pt);
			var invite =new ServerPlayerInvite(inviter,pt);
			playerInvites.put(invitee,invite); //send invite to player STCPlayerGroupInvite
			pt.pendingInvites.put(invite,invitee);
			NetworkHandler.sendToPlayer(new STCPartyInvitePacket(inviter.getDisplayName().getString(),invite.timestamp),invitee);
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
		invite.party.pendingInvites.remove(invite);
		return ReturnMessage.GOOD;
	}
	public static void declineInvite(ServerPlayer invitee){
		var invite = playerInvites.remove(invitee);
		if(invite == null)
			return;
		invite.party.pendingInvites.remove(invite);
		if(invite.party.pendingInvites.size() == 0 && invite.party.count() == 1) //disbanded only pending invite and the lead is left alone;
			disbandParty(invite.party.getLeader());
	}
	public static ReturnMessage removePlayer(ServerPlayer leader, ServerPlayer kicked){
		var pt = partiesPerPlayer.get(leader);
		if(pt == null || !pt.hasPlayer(kicked))
			return ReturnMessage.NOTINGRP;
		if(pt.getLeader() != leader)
			return ReturnMessage.NOTLEAD;

		pt.remove(kicked);
		partiesPerPlayer.remove(kicked);

		if(pt.count() == 1){ //only leader left - disband party
			disbandParty(leader);
		}

		return ReturnMessage.GOOD;
	}
	public static ReturnMessage forceRemovePlayer(ServerPlayer kicked){
		var pt = partiesPerPlayer.get(kicked);
		if(pt != null && pt.hasPlayer(kicked)){
			pt.remove(kicked);
			partiesPerPlayer.remove(kicked);

			if(pt.count() == 1){ //only leader left - disband party
				disbandParty(pt.getLeader());
			}
			return ReturnMessage.GOOD;
		}
		return ReturnMessage.NOTINGRP;
	}
	public static ReturnMessage disbandParty(ServerPlayer leader){
		var pt = partiesPerPlayer.get(leader);
		if(pt.getLeader() == leader){
			var players = pt.clear();
			for(var p : players)
				partiesPerPlayer.remove(p);
			return ReturnMessage.GOOD;
		}
		return ReturnMessage.NOTLEAD;
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
				case NOTINGRP -> {
					return "Not in a party";
				}
			}
			return "";
		}
		public boolean value(){
			return this == GOOD;
		}
	}
}
