package manipulate;

import main.GameFrame;

public class TimeCounter {
	
	private int totalTime = 0;  // ms 
	
	public void updateTick() {
		totalTime += GameFrame.PERIOD;
	}
	
	public void draw() {}
}
