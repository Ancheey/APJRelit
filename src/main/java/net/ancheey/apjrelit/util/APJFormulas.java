package net.ancheey.apjrelit.util;

public class APJFormulas {
	public static final int MAX_PLAYER_LEVEL = 30;
	public static int getExpForLevel(int level){
		if(level < 30)
			return classicFormula(level);

		return Integer.MAX_VALUE;
	}
	private static int classicFormula(int level){ //works for levels 0-30
		return (int)(50+(Math.pow(level,(1+((double)level/66))))*350);
	}

}
