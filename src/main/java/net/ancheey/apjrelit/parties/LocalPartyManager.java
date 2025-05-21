package net.ancheey.apjrelit.parties;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashSet;

@OnlyIn(Dist.CLIENT)
public class LocalPartyManager {
	private static final PlayerGroup group = new PlayerGroup();
	public static PlayerGroup getGroup(){
		return group;
	}
}
