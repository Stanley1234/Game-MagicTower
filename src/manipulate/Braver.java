package manipulate;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import collector.ClipLoader;
import collector.DoorCollector;
import collector.ImagesLoader;
import collector.TileCollector;
import effects.ImagesPlayer;
import gamestate.PlayState;
import info.ItemInfo;
import info.MonsterInfo;
import info.NpcInfo;
import manager.Floor;
import manager.Keys;

public class Braver {
	
	// location info
	private static final int LEFT = 0;
	private static final int UP = 1;
	private static final int RIGHT = 2;
	private static final int DOWN = 3;
	private int x, y;
	private int direction = UP;
	
	// basic attr info for braver
	private int level = 1;
	private int hp = 1000;
	private int attack = 10;
	private int defend = 10;
	private int coins = 0;
	private int exp = 0;
	private int redKeys = 0;
	private int blueKeys = 0;
	private int yellowKeys = 0;
	
	// tools for npc
	private boolean hasHammer = false;
	private boolean hasCross = false;
	private boolean hasTeleporter = false;
	private boolean hasDetector = false;
	
	// manipulate
	private MapLoader mapLoader;
	private PlayState ps;
	// image effects
	private ImagesPlayer imsPlayer = new ImagesPlayer();
	
	// fighting
	private boolean isFirstFighting = true;
	private int prevHp;   // avoid error
	private long hurts;
	private int fightingTimes;
	private MonsterInfo fightedMonster;
	
	public Braver(PlayState ps, MapLoader mapLoader, int x, int y) {
		this.ps = ps;
		this.mapLoader = mapLoader;
		
		// intialize location
		this.x = x;
		this.y = y;
	}
	
	
	
	public ArrayList<Integer> getBraverInfo() {
		// this may be called by monster detector
		ArrayList<Integer> infos = new ArrayList<Integer>();
		infos.add(level);
		infos.add(hp);
		infos.add(attack);
		infos.add(defend);
		infos.add(coins);
		infos.add(exp);
		infos.add(redKeys);
		infos.add(blueKeys);
		infos.add(yellowKeys);
		return infos;
	}
	
	public void setLocation(Point pos) {
		x = pos.x;
		y = pos.y;
	}
	
	
	public int getLevel() {
		return level;
	}



	public void setLevel(int level) {
		this.level = level;
	}



	public int getHp() {
		return hp;
	}



	public void setHp(int hp) {
		this.hp = hp;
	}



	public int getAttack() {
		return attack;
	}



	public void setAttack(int attack) {
		this.attack = attack;
	}



	public int getDefend() {
		return defend;
	}



	public void setDefend(int defend) {
		this.defend = defend;
	}



	public int getCoins() {
		return coins;
	}



	public void setCoins(int coins) {
		this.coins = coins;
	}



	public int getExp() {
		return exp;
	}



	public void setExp(int exp) {
		this.exp = exp;
	}



	public int getRedKeys() {
		return redKeys;
	}



	public void setRedKeys(int redKeys) {
		this.redKeys = redKeys;
	}



	public int getBlueKeys() {
		return blueKeys;
	}



	public void setBlueKeys(int blueKeys) {
		this.blueKeys = blueKeys;
	}



	public int getYellowKeys() {
		return yellowKeys;
	}



	public void setYellowKeys(int yellowKeys) {
		this.yellowKeys = yellowKeys;
	}


	// ************************ for fighting *******************************
	public void updateAttrForFighting(int restbattleRounds, boolean isLastFight) {
		// this may be called by playstate when fighting with monster
		// isLastFight flags whether this is the last hurt in order avoid any error
		if(isFirstFighting) {  // isLastFight may also contain several animation, so should be omitted
			prevHp = hp;
			isFirstFighting = false;
		}
		
		if(!isLastFight) 
			hp = prevHp - ((int)hurts / fightingTimes) * (fightingTimes- restbattleRounds);
		else {
			hp = prevHp - (int)hurts;  // avoid error resulting from inaccurate calculation
			coins += fightedMonster.getCoins();
			exp += fightedMonster.getExp();
			
			isFirstFighting = true;  // for next fighting
		}
	}
	
	public void move() {
		int newx = x, newy = y;
		if(Keys.getCurCode() == KeyEvent.VK_UP) {
			newy --;
			direction = UP;
		} else if(Keys.getCurCode() == KeyEvent.VK_DOWN) {
			newy ++;
			direction = DOWN;
		} else if (Keys.getCurCode() == KeyEvent.VK_LEFT) {
			newx --;
			direction = LEFT;
		} else if(Keys.getCurCode() == KeyEvent.VK_RIGHT) {
			newx ++;
			direction = RIGHT;
		} else           // not direction input, then exit
			return;
		
		if(newx < 0 || newx >= 13 || newy < 0 || newy >= 13)  // out of bound
			return;
		
		if(mapLoader.isHasItem(Floor.getCurFloor(), newx, newy)) {
			ItemInfo itemInfo = mapLoader.getItemInfo(Floor.getCurFloor(), newx, newy);
			
			// updates the effects
			level += itemInfo.getLevel();
			hp += itemInfo.getHp();
			attack += itemInfo.getAttack();
			defend += itemInfo.getDefend();
			coins += itemInfo.getCoins();
			exp += itemInfo.getExp();
			redKeys += itemInfo.getRedKeys();
			blueKeys += itemInfo.getBlueKeys();
			yellowKeys += itemInfo.getYellowKeys();
			
			// special updates
			if(itemInfo.getPrefix().equals("hammer"))
				hasHammer = true;
			else if(itemInfo.getPrefix().equals("cross"))
				hasCross = true;
			else if(itemInfo.getPrefix().equals("teleporter"))
				hasTeleporter = true;
			else if(itemInfo.getPrefix().equals("monster_detector"))
				hasDetector = true;
			
			if(itemInfo.isHasEvents()) 
				mapLoader.setDialogue(itemInfo.getEvents());
			mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "item");
			
			ClipLoader.play("obtain_item", ClipLoader.FFF);
			
			
		} else if(mapLoader.isHasDoor(Floor.getCurFloor(), newx, newy)) {
			int doorType = mapLoader.getDoorType(Floor.getCurFloor(), newx, newy);
			
			if(doorType == DoorCollector.YELLOW_DOOR && yellowKeys >= 1) {
				mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "door");
				yellowKeys --;
				
				ClipLoader.play("open_door", 70.0f);
			} else if(doorType == DoorCollector.BLUE_DOOR && blueKeys >= 1) {
				mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "door");
				blueKeys --;
				ClipLoader.play("open_door", 70.0f);
			} else if(doorType == DoorCollector.RED_DOOR && redKeys >= 1) {
				mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "door");
				redKeys --;
				ClipLoader.play("open_door", 70.0f);
			}
			
			// special door associated with some events
			if(doorType == DoorCollector.GREEN_DOOR) {
	
				
				if (Floor.getCurFloor() == 4) {
					// boys
					if(!mapLoader.isHasMonster(4, 6, 4)) {
						mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "door");
						ClipLoader.play("open_door", 70.0f);
					}
				} else if(Floor.getCurFloor() == 7) {
					// blue knight (6,3) (6,7) (4,5) (4,9)
					if(!mapLoader.isHasMonster(7, 6, 3) && !mapLoader.isHasMonster(7, 6, 7)
							&& !mapLoader.isHasDoor(7, 4, 5) && !mapLoader.isHasDoor(7, 4, 9)) {
						mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "door");
						ClipLoader.play("open_door", 70.0f);
					}
					
				} else if (Floor.getCurFloor() == 13) {
					// high-price exp blueman 3,7 4,6 5,5
					if(!mapLoader.isHasMonster(13, 3, 7) && !mapLoader.isHasMonster(13, 4, 6)
							&& !mapLoader.isHasMonster(13, 5, 5)) {
						mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "door");
						ClipLoader.play("open_door", 70.0f);
					}
				} else if (Floor.getCurFloor() == 10 || Floor.getCurFloor() == 14 || Floor.getCurFloor() == 18
						|| Floor.getCurFloor() == 19 || Floor.getCurFloor() == 21) {
					mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "door");
					ClipLoader.play("open_door", 70.0f);
					
				}
				
			}
			
		} else if(mapLoader.isHasMonster(Floor.getCurFloor(), newx, newy)) {
			MonsterInfo monsterInfo = mapLoader.getMonsterInfo(Floor.getCurFloor(), newx, newy);
			
			// determine whether the monster can be beaten
			hurts = mapLoader.getMonsterHurt(monsterInfo, hp, attack, defend);
			fightingTimes = mapLoader.getFightTimes(monsterInfo, hp, attack, defend);
			if(fightingTimes == 0)
				fightingTimes = 1; // at least fighting once
			if((long)hp > hurts) {
				
				if(!monsterInfo.isBfeEnded()) {

					mapLoader.setDialogue(monsterInfo.getBfEvents());
					monsterInfo.setBfeEnded(true);
					
				} else {
					// here tranfer long to int is safe because actually hp cannot be so large
					mapLoader.eliminate(Floor.getCurFloor(), newx, newy, x, y, fightingTimes, "monster");
				
					System.out.println("hurts:" + hurts);
					System.out.println("fighting times:" + fightingTimes);

					fightedMonster = monsterInfo;
				}
			} else {
				ClipLoader.play("cannot_fight", ClipLoader.FFF); 
			}
			
			
		} else if(mapLoader.isHasNpc(Floor.getCurFloor(), newx, newy)) {
			NpcInfo npcInfo = mapLoader.getNpcInfo(Floor.getCurFloor(), newx, newy);
			
			// F0 nymph -- offer the key
			if(Floor.getCurFloor() == 0 && npcInfo.getPrefix().equals("nymph")) {
				if(!npcInfo.isNetrEnd()) {
					mapLoader.setDialogue(npcInfo.getNeutrStatement());
					npcInfo.setNetrEnd(true);
					yellowKeys ++;
					blueKeys ++;
					redKeys ++;
				}
				else {
					mapLoader.setDialogue(npcInfo.getPosStatement());
				}
			}
			
			// F4 boy -- offer the hammer -- eliminate the door at floor 2
			if(Floor.getCurFloor() == 4 && npcInfo.getPrefix().equals("boy")) {
				if(!npcInfo.isNetrEnd()) {
					mapLoader.setDialogue(npcInfo.getNeutrStatement());
					npcInfo.setNetrEnd(true);
				}
				else {
					if(!hasHammer) 
						mapLoader.setDialogue(npcInfo.getNegStatement());
					else {
						// elimiate the door at floor 2
						if(!npcInfo.isPosEnd()) {
							mapLoader.eliminate(2, 2, 7, "door");
							npcInfo.setPosEnd(true);
							
						} else {						
							mapLoader.setDialogue(npcInfo.getPosStatement());
						}
					}
				}
			}
			// F18 princess -- offer the cross -- enable the stair at floor 18
			if(Floor.getCurFloor() == 18 && npcInfo.getPrefix().equals("princess")) {
				if(!npcInfo.isNetrEnd()) {
					mapLoader.setDialogue(npcInfo.getNeutrStatement());
					npcInfo.setNetrEnd(true);
				}
				else {
					if(!hasCross) 
						mapLoader.setDialogue(npcInfo.getNegStatement());
					else {
						
						if(!npcInfo.isPosEnd()) {
							mapLoader.setStairEnabled(18, 11, 11);
							ClipLoader.play("18_stair", ClipLoader.FFF);
							npcInfo.setPosEnd(true);
							
						} else {						
							mapLoader.setDialogue(npcInfo.getPosStatement());
						}
					}
				}
			}
			
			// Floor 2 blue man
			if(Floor.getCurFloor() == 2 && npcInfo.getPrefix().equals("blueman")) {
				if(!npcInfo.isNetrEnd()) {
					mapLoader.setDialogue(npcInfo.getNeutrStatement());
					npcInfo.setNetrEnd(true);
					
					setAttack(getAttack() + 200);
					mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "npc");
				}
				
			}
			
			// Floor 2 red man
			if (Floor.getCurFloor() == 2 && npcInfo.getPrefix().equals("redman")) {
				if (!npcInfo.isNetrEnd()) {
					mapLoader.setDialogue(npcInfo.getNeutrStatement());
					npcInfo.setNetrEnd(true);
					
					setDefend(getDefend() + 200);
					mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "npc");
				} 
				
			}
			// Floor 15 blue man
			if (Floor.getCurFloor() == 15 && npcInfo.getPrefix().equals("blueman")) {
				if (!npcInfo.isNetrEnd()) {
					mapLoader.setDialogue(npcInfo.getNeutrStatement());
					npcInfo.setNetrEnd(true);

					setAttack(getAttack() + 450);
					mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "npc");
				}

			}
						
			// Floor 15 red man
			if (Floor.getCurFloor() == 15 && npcInfo.getPrefix().equals("redman")) {
				if (!npcInfo.isNetrEnd()) {
					mapLoader.setDialogue(npcInfo.getNeutrStatement());
					npcInfo.setNetrEnd(true);

					setDefend(getDefend() + 450);
					mapLoader.eliminate(Floor.getCurFloor(), newx, newy, "npc");
				}

			}
			
		} else if (mapLoader.isHasStair(Floor.getCurFloor(), newx, newy)) { 
			// isHasStair determines whether there is a enabled stair
			// updates the initial location of braver after he appears in a new floor
			
			if(mapLoader.getStairType(Floor.getCurFloor(), newx, newy) == TileCollector.DOWNSTAIRS) {
				Floor.setCurFloor(Floor.getCurFloor() - 1);
				
				x = mapLoader.getEntryPos(Floor.getCurFloor()).x;
				y = mapLoader.getEntryPos(Floor.getCurFloor()).y;
				
				ClipLoader.play("go_up_and_down", 7.0f);
			}
			else { 
				Floor.setCurFloor(Floor.getCurFloor() + 1);
				
				x = mapLoader.getExitPos(Floor.getCurFloor()).x;
				y = mapLoader.getExitPos(Floor.getCurFloor()).y;
				ClipLoader.play("go_up_and_down", 7.0f);
			}
		} else if(mapLoader.isHasShop(Floor.getCurFloor(), newx, newy)) {
			
			mapLoader.startShopping(Floor.getCurFloor(), newx, newy);
			
		} else if (mapLoader.isHasObstacle(Floor.getCurFloor(), newx, newy)) {
			// do nothing 
		
			/*// movable road()!!!!!!!!!!!!!!!!!!!!!!!!
			final double seqDuration = 0.1;
			if (direction == LEFT)
				imsPlayer.startSeq("braver_left", seqDuration, false);
			else if (direction == UP)
				imsPlayer.startSeq("braver_up", seqDuration, false);
			else if (direction == RIGHT)
				imsPlayer.startSeq("braver_right", seqDuration, false);
			else if (direction == DOWN)
				imsPlayer.startSeq("braver_down", seqDuration, false);
			x = newx;
			y = newy;*/
		} else {
			// movable road
			final double seqDuration = 0.1;
			if(direction == LEFT) 
				imsPlayer.startSeq("braver_left", seqDuration, false);
			else if (direction == UP) 
				imsPlayer.startSeq("braver_up", seqDuration, false);
			else if(direction == RIGHT) 
				imsPlayer.startSeq("braver_right", seqDuration, false); 
			else if(direction == DOWN) 
				imsPlayer.startSeq("braver_down", seqDuration, false);
			x = newx;
			y = newy;
			ClipLoader.play("move", ClipLoader.FFF);
		} 
		
	}
	
	
	public void handleInput() {
		
		
		if(Keys.getCurCode() == KeyEvent.VK_M && hasTeleporter && Floor.getCurFloor() != 21) {
			ps.setFocusState(PlayState.TELEPORTER);
			
		} else if(Keys.getCurCode() == KeyEvent.VK_L && hasDetector) {
			ps.setFocusState(PlayState.MONSTER_DETECTOR);
		} else if(Keys.getCurCode() == KeyEvent.VK_LEFT || Keys.getCurCode() == KeyEvent.VK_RIGHT
				|| Keys.getCurCode() == KeyEvent.VK_UP || Keys.getCurCode() == KeyEvent.VK_DOWN){
			
			move();
		}
		
	}
	
	public void updateSeq() {
		imsPlayer.updateTick();
	}
	
	public void draw(Graphics dbg) {
		
		if (direction == LEFT) {
			dbg.drawImage(ImagesLoader.getImage("braver_left", imsPlayer.getCurIndex()),
					PlayState.GAME_X_OFFSET + x * ImagesLoader.ICON_SIZE, 
					PlayState.GAME_Y_OFFSET + y * ImagesLoader.ICON_SIZE, null);
			
			
		} else if (direction == UP) {
			dbg.drawImage(ImagesLoader.getImage("braver_up", imsPlayer.getCurIndex()),
					PlayState.GAME_X_OFFSET + x * ImagesLoader.ICON_SIZE, 
					PlayState.GAME_Y_OFFSET + y * ImagesLoader.ICON_SIZE, null);
			
		} else if (direction == RIGHT) {
			dbg.drawImage(ImagesLoader.getImage("braver_right", imsPlayer.getCurIndex()),
					PlayState.GAME_X_OFFSET + x * ImagesLoader.ICON_SIZE, 
					PlayState.GAME_Y_OFFSET + y * ImagesLoader.ICON_SIZE, null);
		} else if (direction == DOWN) {
			dbg.drawImage(ImagesLoader.getImage	("braver_down", imsPlayer.getCurIndex()),
					PlayState.GAME_X_OFFSET + x * ImagesLoader.ICON_SIZE, 
					PlayState.GAME_Y_OFFSET + y * ImagesLoader.ICON_SIZE, null);
			
		}
	}
	
}
