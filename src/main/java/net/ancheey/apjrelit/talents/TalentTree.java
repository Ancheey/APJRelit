package net.ancheey.apjrelit.talents;
import net.ancheey.apjrelit.APJRelitCore;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TalentTree {
	private final String name;
	private final String icon;
	private final String description;
	public final List<TalentNode> nodes;

	public TalentTree(String name, String icon, String description) {
		this.name = name;
		this.icon = icon;
		this.description = description;
		nodes = new ArrayList<>();
	}
	public void addNode(TalentNode node){
		nodes.add(node);
	}
	public ResourceLocation getIcon(){
		return ResourceLocation.fromNamespaceAndPath(APJRelitCore.MODID,"textures/gui/talent_tree_icons/"+icon+".png");
	}
}
