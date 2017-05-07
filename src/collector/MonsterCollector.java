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
import info.MonsterInfo;
import manager.Floor;

public class MonsterCollector {

	private static final String MONSTER_CONFIG = "Images/monsterInfo.txt";
	private static final String MAP_CONFIG = "Maps/mapInfo.txt";

	private HashMap<ArrayList<Integer>, MonsterInfo> monstersMap = new HashMap<ArrayList<Integer>, MonsterInfo>();
	private HashMap<String, MonsterInfo> mapcodeMap = new HashMap<String, MonsterInfo>();

	private ImagesPlayer imsplayer = new ImagesPlayer();
	
	public static final long CANNOT_HURT = 9999999;

	public MonsterCollector() {
		readAttr();
		readMap();

		// intialize images player
		imsplayer.startCollectiveSeq(1.0, true, 4);
	}

	private void readAttr() {
		// Analyze monsterInfo.txt
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(MONSTER_CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			while (!(line = br.readLine()).equals("#")) {
				// ignore comments and blank lines
				if (line.startsWith("//") || line.equals(""))
					continue;

				// the following several lines(no blank lines) contains the
				// basic attributes for each monster
				// read mapcode
				StringTokenizer stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String mapcode = stk.nextToken();

				// read name/prefix
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String prefix = stk.nextToken();

				// read before-fight events
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String bfeventsLine = stk.nextToken();
				StringTokenizer stk_comma = new StringTokenizer(bfeventsLine, ",");
				ArrayList<String> bfe = new ArrayList<String>();
				while (stk_comma.hasMoreTokens())
					bfe.add(stk_comma.nextToken());

				// read after-fight events
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				String afeventsLine = stk.nextToken();
				stk_comma = new StringTokenizer(afeventsLine, ",");
				ArrayList<String> afe = new ArrayList<String>();
				while (stk_comma.hasMoreTokens())
					afe.add(stk_comma.nextToken());

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

				// save them into an monsterInfo and put it into mapcodeMaps
				MonsterInfo monsterInfo = new MonsterInfo(prefix, bfe, afe, hp, attack, defend, coins, exp);
				mapcodeMap.put(mapcode, monsterInfo);
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
				// the mapcode of monster ranges from i0-q9
				for (int i = 0; i < 13; i++) {
					StringTokenizer spaceStk = new StringTokenizer(line);
					for (int j = 0; j < 13; j++) {
						String mapcode = spaceStk.nextToken();

						// choose the element that is an monster
						if ('i' <= mapcode.charAt(0) && mapcode.charAt(0) <= 'q') {
							MonsterInfo monsterInfo = mapcodeMap.get(mapcode);

							
							// add it into hashmap
							MonsterInfo newMonsterInfo = new MonsterInfo(monsterInfo.getPrefix(), monsterInfo.getBfEvents()
									, monsterInfo.getAfEvents(), monsterInfo.getHp(), monsterInfo.getAttack(), 
									monsterInfo.getDefend(), monsterInfo.getCoins(), monsterInfo.getExp());
							ArrayList<Integer> floorKey = new ArrayList<Integer>();
							floorKey.add(floorCounter);
							floorKey.add(j);
							floorKey.add(i);

							monstersMap.put(floorKey, newMonsterInfo);
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

	public MonsterInfo getMonsterInfo(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);

		if (monstersMap.containsKey(floorKey))
			return monstersMap.get(floorKey);
		return null;
	}

	public ArrayList<MonsterInfo> getAllMonsterInfo(int floor) {
		ArrayList<MonsterInfo> monsInfos = new ArrayList<MonsterInfo>();

		Iterator<Entry<ArrayList<Integer>, MonsterInfo>> it = monstersMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<ArrayList<Integer>, MonsterInfo> entry = it.next();
			if (entry.getKey().get(0) == floor)
				monsInfos.add(entry.getValue());
		}
		return monsInfos;
	}

	public boolean isHasMonster(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);
		return monstersMap.containsKey(floorKey);
	}

	public void remove(int floor, int x, int y) {
		ArrayList<Integer> floorKey = new ArrayList<Integer>();
		floorKey.add(floor);
		floorKey.add(x);
		floorKey.add(y);

		if (monstersMap.containsKey(floorKey))
			monstersMap.remove(floorKey);
	}
	
	public long getMonsterHurt(MonsterInfo monsterInfo, int bHp, int bAttack, int bDefend) {

		// there are some monsters that have different formula to calculate
		// hurts
		
		int fightTimes = getFightTimes(monsterInfo, bHp, bAttack, bDefend);
		if (monsterInfo.getPrefix().equals("blue_knight") || monsterInfo.getPrefix().equals("black_knight")
				|| monsterInfo.getPrefix().equals("yellow_knight") || monsterInfo.getPrefix().equals("green_knight")) {
			if(bAttack <= monsterInfo.getDefend())
				return CANNOT_HURT;
			
			// 1 / 2
			if(bDefend >= monsterInfo.getAttack())
				return fightTimes * (bHp / 10);
			return (long)fightTimes * (long)(monsterInfo.getAttack() - bDefend); 
		} else if(monsterInfo.getPrefix().equals("nether_master")) {
			if(bAttack <= monsterInfo.getDefend())
				return CANNOT_HURT;
			
			// 4 / 5
			if(bDefend >= monsterInfo.getAttack())
				return fightTimes * (bHp / 5);
			return (long)fightTimes * (long)(monsterInfo.getAttack() - bDefend); 
		} else {
			// braver cannot beat monster
			if(bAttack <= monsterInfo.getDefend())
				return CANNOT_HURT;
			
			return (long)fightTimes * (long)(monsterInfo.getAttack() - bDefend) ;
		}
		
	}
	
	public int getFightTimes(MonsterInfo monsterInfo, int bHp, int bAttack, int bDefend) {
		
	
		// different calculations for knight
		if (monsterInfo.getPrefix().equals("blue_knight") || monsterInfo.getPrefix().equals("black_knight")
				|| monsterInfo.getPrefix().equals("yellow_knight") || monsterInfo.getPrefix().equals("green_knight")) {
			if (bAttack <= monsterInfo.getDefend()) 
				return (int) CANNOT_HURT;
			
			// there will be fixed 10 fighting times
			if(bDefend >= monsterInfo.getAttack())
				return 5;
			return monsterInfo.getHp() / (bAttack - monsterInfo.getDefend()); 
		} else if(monsterInfo.getPrefix().equals("nether_master")) {
			if(bAttack <= monsterInfo.getDefend())
				return (int)CANNOT_HURT;
			
			// 4 / 5
			if(bDefend >= monsterInfo.getAttack())
				return 4;
			return monsterInfo.getHp() / (bAttack - monsterInfo.getDefend()); 
		} else {
			
			// bAttack < mDefend
			// theoretically there will be infinitely hurt if bAttack <= mDefend
			// here do some advanced calculation after taking the "intelligence"
			// into consideration
			// so that braver can always beat the monster provided that he has
			// enough HP
			if (bAttack <= monsterInfo.getDefend()) 
				return (int) CANNOT_HURT;
					
			// there will be no hurt if bDefend >= mAttack and bAttack >= mDefend
			if(bDefend >= monsterInfo.getAttack())
				return 0;
			
			// otherwise
			return monsterInfo.getHp() / (bAttack - monsterInfo.getDefend());
		}
		
		
		
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

				if (monstersMap.containsKey(temp)) {
					MonsterInfo monsterInfo = monstersMap.get(temp);
					dbg.drawImage(ImagesLoader.getImage(monsterInfo.getPrefix(), imsplayer.getCurIndex()),
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

				if (monstersMap.containsKey(temp)) {
					MonsterInfo monsterInfo = monstersMap.get(temp);
					g.drawImage(ImagesLoader.getImage(monsterInfo.getPrefix(), imsplayer.getCurIndex()),
							j * ImagesLoader.ICON_SIZE, i * ImagesLoader.ICON_SIZE, null);
				}
			}
	}
}
