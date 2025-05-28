package net.ancheey.apjrelit.gui.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class PowerTooltipComponent implements TooltipComponent {
	public int D1, D2, D3;
	public PowerTooltipComponent(){}
	public PowerTooltipComponent(int d1, int d2, int d3){
		D1=d1;
		D2=d2;
		D3=d3;
	}
}
