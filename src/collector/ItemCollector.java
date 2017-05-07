package collector;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import gamestate.PlayState;
import info.ItemInfo;
import manager.Floor;

public class ItemCollector {
	
	private static final String ITEM_CONFIG = "Images/itemInfo.txt";
	private static final String MAP_CONFIG = "Maps/mapInfo.txt";
	
	private HashMap<ArrayList<Integer>, ItemInfo> itemsMap = new HashMap<ArrayList<Integer>, ItemInfo>();
	
	
	public ItemCollector() {
		/*
		 * ItemCollector() loads all the items in the map into itemsMap
		 * */
		
		HashMap<String, ItemInfo> mapcodeMap = new HashMap<String, ItemInfo>();
		
		// Analyze itemInfo.txt
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(ITEM_CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while(!(line = br.readLine()).equals("#")) {
				// format:
				// mapcode,name,events,level,hp,attack,defend,coins,exp,redKeys,blueKeys,yellowKeys
				
				
				// ignore comments and blank lines
				if(line.startsWith("//") || line.equals(""))
					continue;
				
				// the following several lines(no blank lines) contains the basic attributes for each item
				// read mapcode
				StringTokenizer stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String mapcode = stk.nextToken();
				
				// read name/prefix
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String prefix = stk.nextToken();
				
				// read events
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String eventsLine = stk.nextToken();
				StringTokenizer stk_comma = new StringTokenizer(eventsLine, ",");
				ArrayList<String> events = new ArrayList<String>();
				while(stk_comma.hasMoreTokens()) 
					events.add(stk_comma.nextToken());
				
				// read level
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int level = Integer.parseInt(stk.nextToken());
				
				// read hp
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int hp = Integer.parseInt(stk.nextToken());
				
				// read attack
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int attack = Integer.parseInt(stk.nextToken());
				
				// read defend
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int defend = Integer.parseInt(stk.nextToken());
				
				// read coins
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int coins = Integer.parseInt(stk.nextToken());
				
				// read exp
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int exp = Integer.parseInt(stk.nextToken());
				
				// read redKeys
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int redKeys = Integer.parseInt(stk.nextToken());
				
				
				// read blueKeys
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int blueKeys = Integer.parseInt(stk.nextToken());
				
				// read yellowKeys
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int yellowKeys = Integer.parseInt(stk.nextToken());
				
				// save them into an itemInfo and put it into mapcodeMaps
				ItemInfo item = new ItemInfo(prefix, events, level, hp, attack, defend, coins, exp, redKeys,
						blueKeys, yellowKeys);
				//System.out.println(prefix + " " + events + " " + level + " " + hp + " " + attack + " " + defend + " " + coins + " "
				//		+ exp + " " + redKeys + " " + blueKeys + " " + yellowKeys);
		        mapcodeMap.put(mapcode, item);	
			}
			in.close();
			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Analyze mapInfo.txt
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(MAP_CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			int floorCounter = -1;
			while(!(line = br.readLine()).equals("#")) {
				// ignore the comments and blank lines
				if(line.startsWith("//") || line.equals("")) 
					continue;
	
				floorCounter ++;
				// for each floor, it contains a 13x13 map
				// the mapcode of item ranges from c0-g9
				for(int i = 0;i < 13;i ++) {
					StringTokenizer spaceStk = new StringTokenizer(line);
					for(int j = 0;j < 13;j ++) {
						String mapcode = spaceStk.nextToken();
						
						// choose the element that is an item
						if('c' <= mapcode.charAt(0) && mapcode.charAt(0) <= 'g') {  
							ItemInfo itemInfo = mapcodeMap.get(mapcode);
							
							// add it into hashmap
							ItemInfo newItemInfo = new ItemInfo(itemInfo.getPrefix(), itemInfo.getEvents(), 
									itemInfo.getLevel(), itemInfo.getHp(), itemInfo.getAttack(), 
									itemInfo.getDefend(), itemInfo.getCoins(), itemInfo.getExp(), 
									itemInfo.getRedKeys(), itemInfo.getBlueKeys(), itemInfo.getYellowKeys());
							ArrayList<Integer> floorKey = new ArrayList<Integer>();
							floorKey.add(floorCounter);
							floorKey.add(j); 
							floorKey.add(i);
							
							itemsMap.put(floorKey, newItemInfo);
							//System.out.println(itemInfo);
						}
					}
					if(i < 12)
						line = br.readLine();
				}
			}
			in.close();
			br.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getItemName(int floor, int x, int y) {
		/*
		 * getItemName(floor,x,y) gets the item at (floor,x,y)
		 * */
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		
		if(itemsMap.containsKey(floorKey)) 
			return itemsMap.get(floorKey).getPrefix();
		
		return null;
	}
	
	public ItemInfo getItemInfo(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		if(itemsMap.containsKey(floorKey)) 
			return itemsMap.get(floorKey);
		return null;
	}
	
	public boolean isHasItem(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		return itemsMap.containsKey(floorKey);
	}
	
	public boolean isHasEvent(int floor, int x, int y) {

		/*
		 * isHasEvent() returns true if there exists an item at (floor, x, y)
		 *              and has events
		 * */
		
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		if(!itemsMap.containsKey(floorKey)) {
			System.out.println("isHasEvent(): does not contain key");
			return false;
		}
		return itemsMap.get(floorKey).getEvents().get(0).equals("null") ? false : true;
	}

	public void remove(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		if(itemsMap.containsKey(floorKey))
			itemsMap.remove(floorKey);
	}

	// incompleted
	public void startDialogue(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
	}
	
	// incompleted
	public void draw(Graphics dbg) {
		for(int i = 0;i < 13;i ++) 
			for(int j = 0;j < 13;j ++) {
				
				ArrayList<Integer> temp = new ArrayList<Integer>();
				
				temp.add(Floor.getCurFloor());
				temp.add(j);
				temp.add(i);
				
				if(itemsMap.containsKey(temp)) {
					ItemInfo itemInfo = itemsMap.get(temp);
					dbg.drawImage(ImagesLoader.getImage(itemInfo.getPrefix()),
							PlayState.GAME_X_OFFSET + j * ImagesLoader.ICON_SIZE, 
							PlayState.GAME_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
				}
			}
	}
	
	public void debug(Graphics g) {
		
		
		
		for(int i = 0;i < 13;i ++) 
			for(int j = 0;j < 13;j ++) {
				
				ArrayList<Integer> temp = new ArrayList<Integer>();
				
				temp.add(Floor.getCurFloor());
				temp.add(j);
				temp.add(i);
				
				if(itemsMap.containsKey(temp)) {
					ItemInfo itemInfo = itemsMap.get(temp);
					g.drawImage(ImagesLoader.getImage(itemInfo.getPrefix()), j * ImagesLoader.ICON_SIZE, 
							i * ImagesLoader.ICON_SIZE, null);
				}
			}
	}
}
