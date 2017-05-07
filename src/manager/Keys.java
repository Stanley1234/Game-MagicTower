package manager;

import main.GameFrame;

public class Keys {
	
	public static int NO_INPUT = -1;
	private static int curCode = NO_INPUT;
	
	private static final int GAP = 45;  // ms
	private static int previousTime = 0;
	private static int totalTime = 0;
	
	public static void update() {
		totalTime += GameFrame.PERIOD;
	}
	
	public static void setCurCode(int c) {
		if(totalTime - previousTime >= GAP) {
			curCode = c;
			previousTime = totalTime;
		}
	}
	
	public static int getCurCode() {
		return curCode;
	}
	
	public static void clear() {
		curCode = NO_INPUT;
	}

}
