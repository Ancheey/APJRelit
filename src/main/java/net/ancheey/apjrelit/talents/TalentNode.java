package net.ancheey.apjrelit.talents;

public class TalentNode {
	public TalentNode(String node, int iconId, String description) {
		this.node = node;
		this.iconId = iconId;
		this.description = description;
	}

	final String node;
	final int iconId;
	final String description;
	public String getNodeName() {
		return node;
	}

	public int getIconId() {
		return iconId;
	}

	public String getDescription() {
		return description;
	}


}
