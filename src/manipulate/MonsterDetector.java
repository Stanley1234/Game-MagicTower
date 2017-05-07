package manipulate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import collector.ImagesLoader;
import collector.MonsterCollector;
import effects.ImagesPlayer;
import gamestate.PlayState;
import info.MonsterInfo;
import manager.Floor;
import manager.Keys;

public class MonsterDetector {
	
	/*
	 * the monster detector can only display nine monsters each time
	 * it would be better than each floor does not contain more than nine kinds of monster
	 * */
	private static final int MAX_MONSTER_NUM = 9;  // the max number of the monster displaying
	private static final int Y_OFFSET = PlayState.GAME_Y_OFFSET + 10;
	private static final int X_OFFSET = PlayState.GAME_X_OFFSET + 10;
	private static final int GAP_X_1 = 5;  // the gap between the icon and first col of attr
	private static final int GAP_X_2 = 55; // the gap between the first col and second col of an attr description
	private static final int GAP_X_3 = 30; // the gap between different attr descriptions
	
	private static final int GAP_Y_1 = 10; // the gap between the starting line and the starting location of a monster
	private static final int GAP_Y_2 = 20; // the gap between the first row and the second row of a monster info
	private PlayState ps;
	private MapLoader mapLoader;
	private Braver braver;
	private MonsterInfo[] monsters;
	private ImagesPlayer imsplayer = new ImagesPlayer();
	
	public MonsterDetector(PlayState ps, MapLoader mapLoader, Braver braver) {
		this.ps = ps;
		this.mapLoader = mapLoader;
		this.braver = braver;
	}
	
	public void init() {
		// remove repeated monster
		ArrayList<MonsterInfo> monsterArray = new ArrayList<MonsterInfo>();
		Iterator<MonsterInfo> it = mapLoader.getAllMonstersInfo(Floor.getCurFloor()).iterator();
		while (it.hasNext()) {
			MonsterInfo monsterInfo = it.next();

			// checks whether there are monsters with same prefix already in monster array
			boolean isSame = false;
			Iterator<MonsterInfo> it_check = monsterArray.iterator();
			while(it_check.hasNext()) {
				MonsterInfo comMonster = it_check.next();
				if(comMonster.getPrefix().equals(monsterInfo.getPrefix())) {
					isSame = true;
					break;
				}
			}
			 
			if(!isSame)
				monsterArray.add(monsterInfo);
		}
		
		
		monsters = new MonsterInfo[monsterArray.size()];
		int counter = 0;
		it = monsterArray.iterator();
		while(it.hasNext()) 
			monsters[counter ++] = it.next();
		
		
		// sort by some rules(look at MonsterInfo)
		Arrays.sort(monsters);
		
		
		// start imsplayer
		imsplayer.startCollectiveSeq(1.0, true, 4);
	}
	public void handleInput() {
		if(Keys.getCurCode() == KeyEvent.VK_L) {
			ps.setFocusState(PlayState.BRAVER);
			imsplayer.endSeq();
		}
	}
	
	public void updateAnimSeq() {
		imsplayer.updateTick();
	}
	
	
	public void draw(Graphics dbg) {
		
		
		dbg.setColor(Color.BLACK);
		dbg.fillRect(PlayState.GAME_X_OFFSET, PlayState.GAME_Y_OFFSET, 
				PlayState.GAME_X_END - PlayState.GAME_X_OFFSET, 
				PlayState.GAME_Y_END - PlayState.GAME_Y_OFFSET);
		
		int line_height = (PlayState.GAME_Y_END - PlayState.GAME_Y_OFFSET) / MAX_MONSTER_NUM;
		int attr_width = (PlayState.GAME_X_END - PlayState.GAME_X_OFFSET - ImagesLoader.ICON_SIZE - GAP_X_2) / 3;
		dbg.setFont(new Font(Font.SERIF, Font.BOLD, 15));
		dbg.setColor(Color.WHITE);
		for(int i = 0;i < Math.min(MAX_MONSTER_NUM, monsters.length);i ++) {
			
			// draw the icon of monster
			dbg.drawImage(ImagesLoader.getImage(monsters[i].getPrefix(), imsplayer.getCurIndex()), 
					X_OFFSET, Y_OFFSET + i * line_height, null);
			// draw the prefix, attack, coin/exp 
			dbg.drawString("Name:", 
					X_OFFSET + ImagesLoader.ICON_SIZE + GAP_X_1, 
					Y_OFFSET + GAP_Y_1 + i * line_height);
			dbg.drawString(monsters[i].getPrefix(), 
					X_OFFSET + ImagesLoader.ICON_SIZE + GAP_X_1 + GAP_X_2, 
					Y_OFFSET + GAP_Y_1 + i * line_height);
			
			dbg.drawString("Attack:", 
					X_OFFSET + ImagesLoader.ICON_SIZE + attr_width + GAP_X_3 , 
					Y_OFFSET + GAP_Y_1 + i * line_height);
			dbg.drawString(monsters[i].getAttack() + "", 
					X_OFFSET + ImagesLoader.ICON_SIZE + attr_width + GAP_X_3 + GAP_X_2, 
					Y_OFFSET + GAP_Y_1 + i * line_height);
			
			dbg.drawString("Coins¡¤Exp:", 
					X_OFFSET + ImagesLoader.ICON_SIZE + attr_width * 2 + 10 , 
					Y_OFFSET + GAP_Y_1 + i * line_height);
			dbg.drawString(monsters[i].getCoins() + "¡¤" + monsters[i].getExp(), 
					X_OFFSET + ImagesLoader.ICON_SIZE + attr_width * 2 + 10 + 80, 
					Y_OFFSET + GAP_Y_1 + i * line_height);
			
			// draw the hp, defend, hurt 
			dbg.drawString("HP:", 
					X_OFFSET + ImagesLoader.ICON_SIZE + GAP_X_1, 
					Y_OFFSET + GAP_Y_1 + GAP_Y_2 + i * line_height);
			dbg.drawString(monsters[i].getHp() + " ", 
					X_OFFSET + ImagesLoader.ICON_SIZE + GAP_X_1 + GAP_X_2, 
					Y_OFFSET + GAP_Y_1 + GAP_Y_2 + i * line_height);
			
			dbg.drawString("Defend:", 
					X_OFFSET + ImagesLoader.ICON_SIZE + attr_width + GAP_X_3 , 
					Y_OFFSET + GAP_Y_1 + GAP_Y_2 + i * line_height);
			dbg.drawString(monsters[i].getDefend() + "", 
					X_OFFSET + ImagesLoader.ICON_SIZE + attr_width + GAP_X_3 + GAP_X_2, 
					Y_OFFSET + GAP_Y_1 + GAP_Y_2 + i * line_height);
			
			dbg.drawString("Hurts:", 
					X_OFFSET + ImagesLoader.ICON_SIZE + attr_width * 2 + 10 , 
					Y_OFFSET + GAP_Y_1 + GAP_Y_2 + i * line_height);
			long hurt = (long) mapLoader.getMonsterHurt(monsters[i], braver.getHp(), braver.getAttack(), braver.getDefend());
			if(hurt >= MonsterCollector.CANNOT_HURT) {
				dbg.drawString("???????", 
						X_OFFSET + ImagesLoader.ICON_SIZE + attr_width * 2 + 10 + 80, 
						Y_OFFSET + GAP_Y_1 + GAP_Y_2 + i * line_height);
			} else {
				dbg.drawString(hurt + "", 
						X_OFFSET + ImagesLoader.ICON_SIZE + attr_width * 2 + 10 + 80, 
						Y_OFFSET + GAP_Y_1 + GAP_Y_2 + i * line_height);
			}
			
			
		}
		
	}
	

}
