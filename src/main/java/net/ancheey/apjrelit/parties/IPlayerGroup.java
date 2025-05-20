package net.ancheey.apjrelit.parties;

import net.minecraft.world.entity.player.Player;

import javax.swing.*;
import java.util.*;

public interface IPlayerGroup {
	boolean isInParty(Player player);
	HashSet<Player> getPlayers();
	int count();
	int maxSize();
	boolean add(Player player);
	boolean remove(Player player);
}
