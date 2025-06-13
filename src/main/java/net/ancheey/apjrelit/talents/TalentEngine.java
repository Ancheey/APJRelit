package net.ancheey.apjrelit.talents;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.ancheey.apjrelit.APJRelitCore;
import net.ancheey.apjrelit.itemsets.ItemSet;
import net.ancheey.apjrelit.itemsets.ItemSetManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TalentEngine {
	private static final String NODES_PATH = "/data/"+ APJRelitCore.MODID+"/talent_nodes.json";
	private static final String TREES_PATH = "/data/"+ APJRelitCore.MODID+"/talent_trees.json";

	private static final List<TalentTree> trees = new ArrayList<>();
	private static List<TalentNode> nodes = new ArrayList<>();
	public static int treeCount(){
		return trees.size();
	}
	public static void load(){
		InputStream stream = ItemSetManager.class.getResourceAsStream(NODES_PATH);
		if(stream == null){
			APJRelitCore.LOGGER.info("No talent nodes file found");
			return;
		}
		try (InputStreamReader reader = new InputStreamReader(stream)) {
			Type type = new TypeToken<List<UnparsedTalentNode>>(){}.getType();
			List<UnparsedTalentNode> uprnds = new Gson().fromJson(reader,type);
			nodes.clear();
			for (var node : uprnds) {
				nodes.add(new TalentNode(node.name,node.iconId,node.description));
			}
		}
		catch (IOException e){
			APJRelitCore.LOGGER.info("Error loading nodes: "+ e);
		}
		stream = ItemSetManager.class.getResourceAsStream(TREES_PATH);
		if(stream == null){
			APJRelitCore.LOGGER.info("No talent trees file found");
			return;
		}
		try (InputStreamReader reader = new InputStreamReader(stream)) {
			Type type = new TypeToken<List<UnparsedTalentTree>>(){}.getType();
			List<UnparsedTalentTree> utrs = new Gson().fromJson(reader,type);
			trees.clear();
			for (var tree : utrs){
				var ptr = new TalentTree(tree.name,tree.icon,tree.description);
				for (int node : tree.nodes) {
					ptr.addNode(nodes.get(node));
				}
				trees.add(ptr);
			}
		}
		catch (IOException e){
			APJRelitCore.LOGGER.info("Error loading sets: "+ e);
		}
	}
	public static TalentTree getTree(int index){
		return trees.get(index);
	}
	private class UnparsedTalentTree{
		String name;
		String icon;
		String description;
		List<Integer> nodes;
	}
	private class UnparsedTalentNode{
		String name;
		int iconId;
		String description;
	}
}
