package net.ancheey.apjrelit.gui.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.*;

public class DiceTooltipComponent implements TooltipComponent {
	public int D1, D2, D3, D4, D5;
	public DiceTooltipComponent(){}
	public DiceTooltipComponent(int d1,int d2,int d3,int d4, int d5){
		D1=d1;
		D2=d2;
		D3=d3;
		D4=d4;
		D5=d5;
	}
}
