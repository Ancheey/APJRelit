package net.ancheey.apjrelit.parties;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerPlayerInvite {
	public static final long VALID_TIME = 30000; //30s
	ServerPlayer inviter;
	long timestamp;
	ServerPlayerParty party;
	public ServerPlayerInvite(ServerPlayer inviter, ServerPlayerParty party){
		this.inviter = inviter;
		this.party = party;
		timestamp = System.currentTimeMillis();
	}
	public ServerPlayer GetInviter(){
		return inviter;
	}
	public boolean IsValid(){
		if(timestamp+VALID_TIME<System.currentTimeMillis())
			return false;
		return party.hasPlayer(inviter);
	}
}
