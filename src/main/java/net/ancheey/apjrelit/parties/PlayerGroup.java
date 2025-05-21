package net.ancheey.apjrelit.parties;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;

public class PlayerGroup{
	public static int MAX_PARTY_SIZE = 4;
	public static int MAX_PARTIES_IN_RAID = 2;
	HashSet<Player> members = new HashSet<>();
	public boolean isFull(){
		return members.size() == MAX_PARTY_SIZE*MAX_PARTIES_IN_RAID;
	}
	public boolean isRaid(){
		return members.size() > MAX_PARTY_SIZE;
	}
	public void add(Player player){

		members.add(player);
	}
	public void remove(Player player){
		members.remove(player);
	}
	public void Clear(){
		members.clear();
	}
	public int Count(){
		return members.size();
	}
	public ImmutableSet<Player> GetPlayers(){
		return ImmutableSet.copyOf(members);
	}
}
