 package collector;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import effects.ImagesPlayer;
import gamestate.PlayState;
import info.ShopInfo;
import manager.Floor;

public class ShopCollector {
	private static final String NPC_CONFIG = "Images/shopInfo.txt";
	private static final String MAP_CONFIG = "Maps/mapInfo.txt";

	private HashMap<ArrayList<Integer>, ShopInfo> shopsMap = new HashMap<ArrayList<Integer>, ShopInfo>();
	private HashMap<String, ShopInfo> mapcodeMap = new HashMap<String, ShopInfo>();

	private ImagesPlayer imsplayer = new ImagesPlayer();
	
	public ShopCollector() {
		readAttr();
		readMap();
		
		// intialize images player
		imsplayer.startCollectiveSeq(1.0, true, 4);
	}

	private void readAttr() {
		// Analyze ShopInfo.txt
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
				String prefixLine = stk.nextToken();
				StringTokenizer stk_comma = new StringTokenizer(prefixLine, ",");
				ArrayList<String> prefix = new ArrayList<String>();
				while (stk_comma.hasMoreTokens())
					prefix.add(stk_comma.nextToken());

				// read product info
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String productLine = stk.nextToken();
				stk_comma = new StringTokenizer(productLine, ",");
				ArrayList<String> products = new ArrayList<String>();
				while (stk_comma.hasMoreTokens())
					products.add(stk_comma.nextToken());

				// read animPart
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String animpartLine = stk.nextToken();
				stk_comma = new StringTokenizer(animpartLine, ",");
				ArrayList<Integer> animpart = new ArrayList<Integer>();
				while (stk_comma.hasMoreTokens())
					animpart.add(Integer.parseInt(stk_comma.nextToken()));


				// save them into an monsterInfo and put it into mapcodeMaps
				ShopInfo shopInfo = new ShopInfo(prefix, products, animpart);
				mapcodeMap.put(mapcode, shopInfo);
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
						if ('t' <= mapcode.charAt(0) && mapcode.charAt(0) <= 'u') {
							ShopInfo shopInfo = mapcodeMap.get(mapcode);
							//System.out.println(mapcode);
							// add it into hashmap
							ShopInfo newShopInfo = new ShopInfo(shopInfo.getPrefix(), shopInfo.getProductInfo(),
									shopInfo.getAnimePart());
							ArrayList<Integer> floorKey = new ArrayList<Integer>();
							floorKey.add(floorCounter);
							floorKey.add(j);
							floorKey.add(i);

							shopsMap.put(floorKey, newShopInfo);
							
							
							// skip next several mapcodes, which describe the same shop
							j += (shopInfo.shopSize() - 1); 
							for(int k = 0;k < shopInfo.shopSize() - 1;k ++)
								spaceStk.nextToken();
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
	
	public boolean isHasShop(int floor, int x, int y) {
		/*
		 * isHasShop() determines whether (floor, x, y) is within a shop
		 * The size of the shop is used
		 * */
		Iterator<Entry<ArrayList<Integer>, ShopInfo>> it = shopsMap.entrySet().iterator();
		while(it.hasNext()) {
			Entry<ArrayList<Integer>, ShopInfo> entry = it.next();
			if(entry.getKey().get(0) != floor)
				continue;
			
			// the leftmost pos of a shop
			int shopX = entry.getKey().get(1);
			int shopY = entry.getKey().get(2);
			if(y == shopY && shopX <= x && x <= shopX + entry.getValue().shopSize() - 1)
				return true;
		}
		return false;
	}
	
	public ShopInfo getShopInfo(int floor, int x, int y) {
		Iterator<Entry<ArrayList<Integer>, ShopInfo>> it = shopsMap.entrySet().iterator();
		while(it.hasNext()) {
			Entry<ArrayList<Integer>, ShopInfo> entry = it.next();
			if(entry.getKey().get(0) != floor)
				continue;
			
			// the leftmost pos of a shop
			int shopX = entry.getKey().get(1);
			int shopY = entry.getKey().get(2);
			if(y == shopY && shopX <= x && x <= shopX + entry.getValue().shopSize())
				return entry.getValue();
		}
		return null;

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

				if (shopsMap.containsKey(temp)) {
					ShopInfo shopInfo = shopsMap.get(temp);
					
					for(int k = 0;k < shopInfo.shopSize();k ++) {
						if(shopInfo.isAnimePart(k)) 
							dbg.drawImage(ImagesLoader.getImage(shopInfo.getPrefix().get(k), imsplayer.getCurIndex()), 
									PlayState.GAME_X_OFFSET + (j + k) * ImagesLoader.ICON_SIZE, 
									PlayState.GAME_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
						else
							dbg.drawImage(ImagesLoader.getImage(shopInfo.getPrefix().get(k)), 
								   PlayState.GAME_X_OFFSET + (j + k) * ImagesLoader.ICON_SIZE, 
								   PlayState.GAME_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
					}
					
					j += (shopInfo.shopSize() - 1);
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

				if (shopsMap.containsKey(temp)) {
					ShopInfo shopInfo = shopsMap.get(temp);
					
					for(int k = 0;k < shopInfo.shopSize();k ++) {
						if(shopInfo.isAnimePart(k)) 
							g.drawImage(ImagesLoader.getImage(shopInfo.getPrefix().get(k), imsplayer.getCurIndex()), 
									(j + k) * ImagesLoader.ICON_SIZE, i * ImagesLoader.ICON_SIZE, null);
						else
							g.drawImage(ImagesLoader.getImage(shopInfo.getPrefix().get(k)), 
									(j + k) * ImagesLoader.ICON_SIZE, i * ImagesLoader.ICON_SIZE, null);
					}
					
					j += (shopInfo.shopSize() - 1);
				}
			}
	}
}
