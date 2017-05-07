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
import info.NpcInfo;
import manager.Floor;

public class NpcCollector {

	private static final String NPC_CONFIG = "Images/npcInfo.txt";
	private static final String MAP_CONFIG = "Maps/mapInfo.txt";

	private HashMap<ArrayList<Integer>, NpcInfo> npcsMap = new HashMap<ArrayList<Integer>, NpcInfo>();
	private HashMap<String, NpcInfo> mapcodeMap = new HashMap<String, NpcInfo>();

	private ImagesPlayer imsplayer = new ImagesPlayer();

	public NpcCollector() {
		readAttr();
		readMap();
		
		// intialize images player
		imsplayer.startCollectiveSeq(1.0, true, 4);
	}

	private void readAttr() {
		// Analyze NpcInfo.txt
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(NPC_CONFIG);
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

				// read name/prefix
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String prefix = stk.nextToken();

				// read netrual statement
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String netrLine = stk.nextToken();
				StringTokenizer stk_comma = new StringTokenizer(netrLine, ",");
				ArrayList<String> netr = new ArrayList<String>();
				while (stk_comma.hasMoreTokens())
					netr.add(stk_comma.nextToken());

				// read negtive statement
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String negLine = stk.nextToken();
				stk_comma = new StringTokenizer(negLine, ",");
				ArrayList<String> neg = new ArrayList<String>();
				while (stk_comma.hasMoreTokens())
					neg.add(stk_comma.nextToken());

				// read positive statement
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String posLine = stk.nextToken();
				stk_comma = new StringTokenizer(posLine, ",");
				ArrayList<String> pos = new ArrayList<String>();
				while (stk_comma.hasMoreTokens())
					pos.add(stk_comma.nextToken());

				// save them into an monsterInfo and put it into mapcodeMaps
				NpcInfo npcInfo = new NpcInfo(prefix, netr, neg, pos);
				mapcodeMap.put(mapcode, npcInfo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void readMap() {
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(MAP_CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			int floorCounter = -1;
			while (!(line = br.readLine()).equals("#")) {
				// ignore the comments and blank lines
				if (line.startsWith("//") || line.equals(""))
					continue;

				floorCounter++;
				// for each floor, it contains a 13x13 map
				// the mapcode of npc ranges from r0-s9
				for (int i = 0; i < 13; i++) {
					StringTokenizer spaceStk = new StringTokenizer(line);
					for (int j = 0; j < 13; j++) {
						String mapcode = spaceStk.nextToken();

						// choose the element that is an npc
						if ('r' <= mapcode.charAt(0) && mapcode.charAt(0) <= 's') {
							NpcInfo npcInfo = mapcodeMap.get(mapcode);

							// add it into hashmap
							NpcInfo newNpcInfo = new NpcInfo(npcInfo.getPrefix(), npcInfo.getNeutrStatement(), 
									npcInfo.getNegStatement(), npcInfo.getPosStatement());
							ArrayList<Integer> floorKey = new ArrayList<Integer>();
							floorKey.add(floorCounter);
							floorKey.add(j);
							floorKey.add(i);

							npcsMap.put(floorKey, newNpcInfo);
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
	
	
	public boolean isHasNpc(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		return npcsMap.containsKey(floorKey);
	}
	
	public NpcInfo getNpcInfo(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		return npcsMap.get(floorKey);
	}
	
	public void remove(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		if(npcsMap.containsKey(floorKey))
			npcsMap.remove(floorKey);
	}
	
	

	public void updateAnimSeq() {
		imsplayer.updateTick();
	}

	public void draw(Graphics dbg) {
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 13; j++) {

				ArrayList<Integer> temp = new ArrayList<Integer>();

				temp.add(Floor.getCurFloor());
				temp.add(j);
				temp.add(i);

				if (npcsMap.containsKey(temp)) {
					NpcInfo npcInfo = npcsMap.get(temp);
					dbg.drawImage(ImagesLoader.getImage(npcInfo.getPrefix(), imsplayer.getCurIndex()),
							PlayState.GAME_X_OFFSET + j * ImagesLoader.ICON_SIZE, 
							PlayState.GAME_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
				}
			}
	}

	public void debug(Graphics g) {
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 13; j++) {

				ArrayList<Integer> temp = new ArrayList<Integer>();

				temp.add(Floor.getCurFloor());
				temp.add(j);
				temp.add(i);

				if (npcsMap.containsKey(temp)) {
					NpcInfo npcInfo = npcsMap.get(temp);
					g.drawImage(ImagesLoader.getImage(npcInfo.getPrefix(), imsplayer.getCurIndex()),
							j * ImagesLoader.ICON_SIZE, i * ImagesLoader.ICON_SIZE, null);
				}
			}
	}
}
