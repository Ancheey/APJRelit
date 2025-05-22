package net.ancheey.apjrelit.parties;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerPlayerInvite {
	public static final long VALID_TIME = 30000; //30s
	ServerPlayer inviter;
	long timestamp;
	ServerPlayerGroup group;
	public ServerPlayerInvite(ServerPlayer inviter, ServerPlayerGroup group){
		this.inviter = inviter;
		this.group = group;
		timestamp = System.currentTimeMillis();
	}
	public ServerPlayer GetInviter(){
		return inviter;
	}
	public boolean IsValid(){
		if(timestamp+VALID_TIME<System.currentTimeMillis())
			return false;
		return group.hasPlayer(inviter);
	}
}
