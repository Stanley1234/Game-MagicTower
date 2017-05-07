package manager;

import java.util.ArrayList;

public class Dialogue {
	private static ArrayList<String> events;
	private static int curIndex = 0;
	
	public static boolean isHasNext() {
		return curIndex < events.size() - 1;
	}
	
	public static void nextDialogue() {
		curIndex ++;
	}
	
	public static String getDialogue() {
		return events.get(curIndex);
	
	}
	
	public static void setDialogue(ArrayList<String> eventsArray) {
		events = eventsArray;
		curIndex = 0;
	}
}
