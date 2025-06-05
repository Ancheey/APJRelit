package net.ancheey.apjrelit.parties;

import net.minecraft.world.entity.player.Player;

public class LocalPlayerInvite {
	public static final long VALID_TIME = 30000; //30s
	String inviter;
	long timestamp;
	public LocalPlayerInvite(String inviter, long timestamp){
		this.inviter = inviter;
		this.timestamp = timestamp;
	}
	public String getInviter(){
		return inviter;
	}
	public long getTimestamp(){
		return timestamp;
	}
	public boolean EnsureValid(){
		return !(timestamp+VALID_TIME<System.currentTimeMillis());
	}
}
