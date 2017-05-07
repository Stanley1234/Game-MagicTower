package manager;


public class Floor {
	public static int FLOOR_NUM = 21; 
	private static int CurFloor = 0;
	private static boolean[] IsBeenFloor = new boolean[FLOOR_NUM + 2];  // 0 - 21 
	
	static
	{
		IsBeenFloor[0] = true;
		//Arrays.fill(IsBeenFloor, true);
	}
	
	public static int getCurFloor() {
		return CurFloor;
	}
	
	public static void setCurFloor(int curFloor) {
		CurFloor = curFloor;
		IsBeenFloor[curFloor] = true;
	}
	
	public static boolean IsBeenFloor(int f) {
		return IsBeenFloor[f];
	}
	
	public static boolean isBeenFloor(int floor) {
		return IsBeenFloor[floor];
	}	
	
}
