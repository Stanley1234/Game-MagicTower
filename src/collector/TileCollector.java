package collector;

import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import gamestate.PlayState;
import info.TileInfo;
import manager.Floor;

public class TileCollector {

	private static final String TILE_CONFIG = "Images/tileInfo.txt";
	private static final String MAP_CONFIG = "Maps/mapInfo.txt";
	private static final String ENABLE_CONFIG = "Images/tileEnable.txt";

	// the following static constants describes how type represents the type of
	// tile
	// more tiles can be added with the type from 6 to 18
	public static final int ROAD = 0;
	public static final int WALL = 1;
	public static final int ICE_WALL = 2;
	public static final int STAR = 3;
	public static final int PLASMA = 4;
	public static final int SEA = 5;
	public static final int UPSTAIRS = 19;
	public static final int DOWNSTAIRS = 20;

	// store the location and doorInfo
	private HashMap<ArrayList<Integer>, TileInfo> tilesMap = new HashMap<ArrayList<Integer>, TileInfo>();
	private HashMap<String, TileInfo> mapcodeMap = new HashMap<String, TileInfo>();

	// store the location where animation is played
	// each time there is only one animation played

	public TileCollector() {
		readAttr();
		readMap();
		readDisable();
	}

	private void readAttr() {
		// read "tileInfo.txt"
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(TILE_CONFIG);
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

				// read isObstacle
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				boolean isObstacle = stk.nextToken().equals("true") ? true : false;

				// put into hashmap
				mapcodeMap.put(mapcode, new TileInfo(prefix, type, true, isObstacle));
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
			// read the location info of tile
			int floorCounter = -1;
			while (!(line = br.readLine()).equals("#")) {
				// ignore comments and blank lines
				if (line.startsWith("//") || line.equals(""))
					continue;

				floorCounter++;
				// for each floor, it contains a 13x13 map
				// the mapcode of item ranges from a0-b9
				for (int i = 0; i < 13; i++) {
					StringTokenizer spaceStk = new StringTokenizer(line);
					for (int j = 0; j < 13; j++) {
						String mapcode = spaceStk.nextToken();

						// if there is only tile, then load its corresponding
						// tile
						// otherwise load movable tile/road
						TileInfo tiletInfo;
						if ('a' <= mapcode.charAt(0) && mapcode.charAt(0) <= 'b')
							tiletInfo = mapcodeMap.get(mapcode);
						else
							tiletInfo = mapcodeMap.get("a0");

						
						// add it into hashmap
						TileInfo newTileInfo = new TileInfo(tiletInfo.getPrefix(), tiletInfo.getType(),
								tiletInfo.isEnabled(), tiletInfo.isObstacle());
						ArrayList<Integer> floorKey = new ArrayList<Integer>();
						floorKey.add(floorCounter);
						floorKey.add(j);
						floorKey.add(i);
						tilesMap.put(floorKey, newTileInfo);
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

	private void readDisable() {
		// read "tileEnable.txt"
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(ENABLE_CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			// read the enable/disable info of tile
			while (!(line = br.readLine()).equals("#")) {
				// ignore comments and blank lines
				if (line.startsWith("//") || line.equals(""))
					continue;
				StringTokenizer stk_space = new StringTokenizer(line);
				int floor = Integer.parseInt(stk_space.nextToken());
				int x = Integer.parseInt(stk_space.nextToken());
				int y = Integer.parseInt(stk_space.nextToken());

				// update info
				ArrayList<Integer> floorKey = new ArrayList<Integer>();
				floorKey.add(floor);
				floorKey.add(x);
				floorKey.add(y);
				
				TileInfo oldInfo = tilesMap.get(floorKey);
				TileInfo newTiletInfo = new TileInfo(oldInfo.getPrefix(), oldInfo.getType(), false, oldInfo.isObstacle());
				tilesMap.replace(floorKey, newTiletInfo);
			}
			in.close();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isHasObstacle(int floor, int x, int y) {

		/*
		 * isHasObstacle() determines whether the tile at (floor,x,y) is an
		 * obstacle
		 */

		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		if (tilesMap.containsKey(floorKey))
			return tilesMap.get(floorKey).isObstacle();
		// if it does not exist
		return false;
	}

	public boolean isHasStair(int floor, int x, int y) {

		/*
		 * isHasStair() determines whether there is a enabled stair at (floor,
		 * x, y)
		 */
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);

		if (tilesMap.containsKey(floorKey)) {
			TileInfo tileInfo = tilesMap.get(floorKey);
			if ((tileInfo.getType() == UPSTAIRS || tileInfo.getType() == DOWNSTAIRS) && tileInfo.isEnabled())
				return true;
		}

		return false;
	}
	
	public int getStairType(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		return tilesMap.get(floorKey).getType();
	}

	// The next two functions are used for teleporter
	public Point getEntryPos(int floor) {

		/*
		 * getEntryX(int floor) get the upstair pos regardless of being disabled
		 */

		Iterator<Entry<ArrayList<Integer>, TileInfo>> it = tilesMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<ArrayList<Integer>, TileInfo> entry = it.next();
			int f = entry.getKey().get(0);

		
			if (f != floor)
				continue;
			if (entry.getValue().getType() == UPSTAIRS) 
				return new Point(entry.getKey().get(1), entry.getKey().get(2));
			

		}
		return null;
	}

	public Point getExitPos(int floor) {

		/*
		 * getExitPos(int floor) get the downstair pos regardless of being disabled
		 */

		Iterator<Entry<ArrayList<Integer>, TileInfo>> it = tilesMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<ArrayList<Integer>, TileInfo> entry = it.next();
			int f = entry.getKey().get(0);
			//System.out.println(entry.getKey().get(0) + " " + entry.getKey().get(1) + " " + entry.getKey().get(2) + " "
			//		+ entry.getValue().getType());
			if (f != floor)
				continue;
			if (entry.getValue().getType() == DOWNSTAIRS)
				return new Point(entry.getKey().get(1), entry.getKey().get(2));

		}
		return null;
	}

	public void remove(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);

		if (tilesMap.containsKey(floorKey)) {
			tilesMap.remove(floorKey);

			// put a normal tile onto this place
			TileInfo normTile = new TileInfo("road", ROAD, true, false);
			tilesMap.put(floorKey, normTile);
		}
	}

	public void setEnabled(int floor, int x, int y) {
		// called in NPC events
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);

		if (tilesMap.containsKey(floorKey)) {
			TileInfo newTileInfo = tilesMap.get(floorKey);
			newTileInfo.setEnabled(true);
			tilesMap.replace(floorKey, newTileInfo);
		}
	}

	public void draw(Graphics dbg) {
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 13; j++) {

				ArrayList<Integer> temp = new ArrayList<Integer>();

				temp.add(Floor.getCurFloor());
				temp.add(j);
				temp.add(i);

				if (tilesMap.containsKey(temp)) {
					TileInfo tiletInfo = tilesMap.get(temp);
		
					if(!tiletInfo.isEnabled()) {
						dbg.drawImage(ImagesLoader.getImage("road"), 
								PlayState.GAME_X_OFFSET + j * ImagesLoader.ICON_SIZE, 
								PlayState.GAME_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
					} else {
						dbg.drawImage(ImagesLoader.getImage(tiletInfo.getPrefix()), 
								PlayState.GAME_X_OFFSET + j * ImagesLoader.ICON_SIZE, 
								PlayState.GAME_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
					}
				}
			}
	}

}
