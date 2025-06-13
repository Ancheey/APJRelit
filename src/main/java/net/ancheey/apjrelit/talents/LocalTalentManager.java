package net.ancheey.apjrelit.talents;

import net.ancheey.apjrelit.APJRelitCore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.management.ValueExp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class LocalTalentManager {
	public static int talentPoints = 21;
	private static final int talentGridSize = 6;
	private static final List<BitSet> dataset = new ArrayList<>();
	public static @Nullable BitSet getTree(int index){
		if(index < dataset.size() && index >= 0)
			return dataset.get(index);
		return null;
	}

	public static boolean setTalent(int tree, int index, boolean value){
		if(tree >= dataset.size() || tree <0)
			return false;
		dataset.get(tree).set(index, value);
		//temp
		if(value) {
			talentPoints--;
		}
		else {
			talentPoints++;
		}
		return true;
	}
	public static boolean setTalent(int tree, int r, int l, boolean value){
		return setTalent(tree,r*talentGridSize+l,value);
	}
	public static boolean getTalent(int tree, int r, int l){
		if(tree >= dataset.size() || tree < 0)
			return false;
		return dataset.get(tree).get(r*talentGridSize+l);
	}
	public static boolean isAvailable(int tree, int r, int l){
		if(talentPoints <= 0)
			return false;
		if(r == 0 && l == 0)
			return true;
		if(r > 0 && getTalent(tree, r - 1, l))
			return true;
		if(r < talentGridSize-1 && getTalent(tree, r + 1, l))
			return true;
		if(l > 0 && getTalent(tree, r, l - 1))
			return true;
		return l < talentGridSize-1 && getTalent(tree, r, l + 1);
	}
	static {
		for (int i = 0; i < TalentEngine.treeCount(); i++) {
			var bitset = new BitSet();
			for (int j = 0; j < 36; j++)
				bitset.set(j, false);
			dataset.add(bitset);
		}
	}
}
