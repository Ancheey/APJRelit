package net.ancheey.apjrelit.parties;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerPartyManager {
	private static final HashSet<PlayerGroup> groups = new HashSet<>();
	private static final Map<Player,PlayerGroup>  groupsPerPlayer = new HashMap<>();

	public @Nullable PlayerGroup GetPlayerGroup(Player player){
		if(!groupsPerPlayer.containsKey(player))
			return null;
		return groupsPerPlayer.get(player);
	}
	//public void //adding players (invites)
}
