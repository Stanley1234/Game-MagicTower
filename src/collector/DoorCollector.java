package collector;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import effects.ImagesPlayer;
import gamestate.PlayState;
import info.DoorInfo;
import manager.Floor;

public class DoorCollector {

	private static final String DOOR_CONFIG = "Images/doorInfo.txt";
	private static final String MAP_CONFIG = "Maps/mapInfo.txt";

	public static int YELLOW_DOOR = 0;
	public static int BLUE_DOOR = 1;
	public static int RED_DOOR = 2;
	public static int GREEN_DOOR = 3;
	
	public static double DOOR_OPEN_TIME = 0.5;  // the length of time for animation (secs)

	private HashMap<ArrayList<Integer>, DoorInfo> doorsMap = new HashMap<ArrayList<Integer>, DoorInfo>();
	private HashMap<String, DoorInfo> mapcodeMap = new HashMap<String, DoorInfo>();
	
	private ImagesPlayer imsplayer = new ImagesPlayer();
	private ArrayList<Integer> animePos = null;

	public DoorCollector() {
		readAttr();
		readMap();
	}

	private void readAttr() {
		// read "doorInfo.txt"
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(DOOR_CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			while (!(line = br.readLine()).equals("#")) {

				// ignore comments and blank lines
				if (line.startsWith("//") || line.equals(""))
					continue;

				// read mapcode
				StringTokenizer stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String mapcode = stk.nextToken();

				// read prefix
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String prefix = stk.nextToken();

				// read type
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int type = Integer.parseInt(stk.nextToken());

				// read isOpenByKey
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				boolean isOpenByKey = stk.nextToken().equals("true") ? true : false;

				// put into hashmap
				mapcodeMap.put(mapcode, new DoorInfo(prefix, type, isOpenByKey));
			}
			in.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void readMap() {
		// read "mapInfo.txt"
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(MAP_CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			// read the location info of door
			int floorCounter = -1;
			while (!(line = br.readLine()).equals("#")) {
				// ignore comments and blank lines
				if (line.startsWith("//") || line.equals(""))
					continue;

				floorCounter++;
				// for each floor, it contains a 13x13 map
				// the mapcode of item ranges from h0-h9
				for (int i = 0; i < 13; i++) {
					StringTokenizer spaceStk = new StringTokenizer(line);
					for (int j = 0; j < 13; j++) {
						String mapcode = spaceStk.nextToken();

						DoorInfo doorInfo;
						if ('h' == mapcode.charAt(0)) {
							doorInfo = mapcodeMap.get(mapcode);
							
							// add it into hashmap							
							DoorInfo newDoorInfo = new DoorInfo(doorInfo.getPrefix(), doorInfo.getType(), doorInfo.isOpenByKey());
							ArrayList<Integer> floorKey = new ArrayList<Integer>();
							floorKey.add(floorCounter);
							floorKey.add(j);
							floorKey.add(i);
							doorsMap.put(floorKey, newDoorInfo);
						}
					}
					if (i < 12)
						line = br.readLine();
				}

			}
			in.close();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isHasDoor(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		
		return doorsMap.containsKey(floorKey);
	}
	
	public boolean isDoorOpenByKey(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		
		if(doorsMap.containsKey(floorKey)) 
			return doorsMap.get(floorKey).isOpenByKey();
		
		return false;
	}
	
	public int getDoorType(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		
		if(doorsMap.containsKey(floorKey))
			return doorsMap.get(floorKey).getType();
		
		return -1;
	}
	
	public DoorInfo getDoorInfo(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		
		if(doorsMap.containsKey(floorKey))
			return doorsMap.get(floorKey);
		
		return null;
	}
	
	public void setDoorOpenAnim(int floor, int x, int y) {
		
		// there might be a bug without this statement
		// the braver opens one door and immediately open another door, causing the first door remain the same
		// and cannot disappear 
		// so if the animation is running, braver cannot open another door
		if(animePos != null)
			return;
		
		
		animePos = new ArrayList<Integer>();
		animePos.add(floor);
		animePos.add(x);
		animePos.add(y);
		
		// there should be a door, no need to debug here
		DoorInfo doorInfo = doorsMap.get(animePos);
		imsplayer.startSeq(doorInfo.getPrefix(), DOOR_OPEN_TIME, false);
	}
	
	public boolean isAnimEnd() {
		// animePos != null indicates the animation has not finished and the braver cannot proceed here
		// !imsplayer.isRunning() indicates the door has been graphically removed(removed in memory)
		return animePos != null && !imsplayer.isRunning();
	}
	public void endAnime() {
		animePos.clear();
		animePos = null;
		imsplayer.endSeq();
	}
	
	public void remove(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		
		if(doorsMap.containsKey(floorKey))
			doorsMap.remove(floorKey);
	}
	
	public void updateAnimSeq() {
		// if the animation is deactivated, updateTick() will ignore this message
		imsplayer.updateTick();
	}
	
	// incomplete
	public void draw(Graphics dbg) {
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 13; j++) {

				ArrayList<Integer> temp = new ArrayList<Integer>();

				temp.add(Floor.getCurFloor());
				temp.add(j);
				temp.add(i);

				if (doorsMap.containsKey(temp)) {  // for closed door
					DoorInfo doorInfo = doorsMap.get(temp);
				
					
						dbg.drawImage(ImagesLoader.getImage(doorInfo.getPrefix()),
								PlayState.GAME_X_OFFSET + + j * ImagesLoader.ICON_SIZE, 
								PlayState.GAME_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
					
				} else if (imsplayer.isRunning() && animePos.get(0) == Floor.getCurFloor()
						&& animePos.get(1) == j && animePos.get(2) == i) {
					
					// for opening door
					dbg.drawImage(imsplayer.getCurImage(),
							PlayState.GAME_X_OFFSET + j * ImagesLoader.ICON_SIZE, 
							PlayState.GAME_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
				}
			}
	}

		
	
	
	
}
