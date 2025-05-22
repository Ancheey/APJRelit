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
	private static final HashSet<ServerPlayerGroup> groups = new HashSet<>();
	private static final Map<Player, ServerPlayerGroup>  groupsPerPlayer = new HashMap<>();

	public static @Nullable ServerPlayerGroup GetPlayerGroup(Player player){
		if(!groupsPerPlayer.containsKey(player))
			return null;
		return groupsPerPlayer.get(player);
	}
	//public void //adding players (invites) (creating an invite - player inviting, player targetted, max accept time
	//Accepting an invite removes all other invites that are over time or for the invited player
	//send a STCplayeropertationpacket packet to all players from the group when accepted
	//create a packet CTSInvitePlayer(PlayerUUID)
	//On SetCamera(Entity) call stcpartysyncpacket by a ctspartysyncbegpacket
	//TODO: Finish serverside parties and add a way to invite
}
