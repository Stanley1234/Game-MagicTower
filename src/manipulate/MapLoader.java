package manipulate;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.naming.TimeLimitExceededException;

import collector.DoorCollector;
import collector.ItemCollector;
import collector.MonsterCollector;
import collector.NpcCollector;
import collector.ShopCollector;
import collector.TileCollector;
import effects.Boom;
import gamestate.PlayState;
import info.DoorInfo;
import info.ItemInfo;
import info.MonsterInfo;
import info.NpcInfo;
import info.ShopInfo;
import info.TileInfo;
import manager.Dialogue;
import manager.Floor;
import manager.Shopping;

public class MapLoader {
	
	/*
	 * In Class MapLoader, I actually do not need the parameter "floor" in many functions below
	 * However, to make it extendable, I reserve this paramter.
	 * When two players play this game simultaneously, they share the same maploader but they 
	 * are likely in different floor. So it is vital to specify the parameter "floor"
	 * */
	
	private PlayState ps;
	
	private ItemCollector itemCollector = new ItemCollector();
	private TileCollector tileCollector = new TileCollector();
	private DoorCollector doorCollector = new DoorCollector();
	private MonsterCollector monsterCollector = new MonsterCollector();
	private NpcCollector npcCollector = new NpcCollector();
	private ShopCollector shopCollector = new ShopCollector();
	private Boom boom;

	public MapLoader(PlayState ps, Boom boom) {
		this.ps = ps;
		this.boom = boom;
	}

	// ------- for item -----------
	public boolean isHasItem(int floor, int x, int y) {
		return itemCollector.isHasItem(floor, x, y);
	}

	public ItemInfo getItemInfo(int floor, int x, int y) {
		return itemCollector.getItemInfo(floor, x, y);
	}

	// ------- for stair -----------
	public boolean isHasStair(int floor, int x, int y) {
		return tileCollector.isHasStair(floor, x, y);
	}
	
	public int getStairType(int floor, int x, int y) {
		return tileCollector.getStairType(floor, x, y);
	}
	
	public Point getEntryPos(int floor) {
		return tileCollector.getEntryPos(floor);
	}
	
	public Point getExitPos(int floor) {
		return tileCollector.getExitPos(floor);
	}
	
	public void setStairEnabled(int floor, int x, int y) {
		tileCollector.setEnabled(floor, x, y);
	}
	
	// ------- for door -----------
	public boolean isHasDoor(int floor, int x, int y) {
		return doorCollector.isHasDoor(floor, x, y);
	}

	public int getDoorType(int floor, int x, int y) {
		return doorCollector.getDoorType(floor, x, y);
	}
	
	public DoorInfo getDoorInfo(int floor, int x, int y) {
		return doorCollector.getDoorInfo(floor, x, y);
	}
	// ------- for monster -----------
	public boolean isHasMonster(int floor, int x, int y) {
		return monsterCollector.isHasMonster(floor, x, y);
	}
	
	public MonsterInfo getMonsterInfo(int floor, int x, int y) {
		return monsterCollector.getMonsterInfo(floor, x, y);
	}
	
	public ArrayList<MonsterInfo> getAllMonstersInfo(int floor) {
		return monsterCollector.getAllMonsterInfo(floor);
	}
	
	public long getMonsterHurt(MonsterInfo monsterInfo, int bHp, int bAttack, int bDefend) {
		return monsterCollector.getMonsterHurt(monsterInfo, bHp, bAttack, bDefend);
	}
	
	
	public int getFightTimes(MonsterInfo monsterInfo, int bHp, int bAttack, int bDefend) {
		return monsterCollector.getFightTimes(monsterInfo, bHp, bAttack, bDefend);
	}
	
	public void setMonsterDisappear() {
		MonsterInfo monsterInfo = monsterCollector.getMonsterInfo(Floor.getCurFloor(), boom.getObj1Pos().x, boom.getObj1Pos().y);
		
		// set after-fighting dialogues
		if(!monsterInfo.getAfEvents().get(0).equals("null")) { 
			setDialogue(monsterInfo.getAfEvents());
			ps.setFocusState(PlayState.DIALOGUE);
		} else {
			ps.setFocusState(PlayState.BRAVER);
		}
		// remove the elements and the animation object
		monsterCollector.remove(Floor.getCurFloor(), boom.getObj1Pos().x, boom.getObj1Pos().y);
	}

	// ------- for npc -----------
	public boolean isHasNpc(int floor, int x, int y) {
		return npcCollector.isHasNpc(floor, x, y);
	}

	public NpcInfo getNpcInfo(int floor, int x, int y) {
		return npcCollector.getNpcInfo(floor, x, y);
	}

	// ------- for shop -----------
	public boolean isHasShop(int floor, int x, int y) {
		return shopCollector.isHasShop(floor, x, y);
	}
	
	public void startShopping(int floor, int x, int y) {
		ShopInfo shopInfo = shopCollector.getShopInfo(floor, x, y);
		Shopping.setProductionInfo(shopInfo.getProductInfo());
		ps.setFocusState(PlayState.SHOP);
	}

	// ------- for obstacle -----------
	public boolean isHasObstacle(int floor, int x, int y) {
		return tileCollector.isHasObstacle(floor, x, y);
	}
	
	// ------- for dialogue -----------
	public void setDialogue(ArrayList<String> events) {
		ps.setFocusState(PlayState.DIALOGUE);
		Dialogue.setDialogue(events);
	}
	
	// ------- other functions --------
	public void eliminate(int floor, int x, int y, String type) {
		/*
		 * eliminate() differs from removing the objects at (floor, x, y), it should be viewed as a 
		 *             a flag of starting removing an object from its collector. 
		 *             It may start animation first and after the animation is ended, the object is removed
		 *             in update()
		 *             It may start dialogue if it has events
		 * */
		if(type.equals("tile")) {
			// this may be called when braver is about to fight with demon king
			// when the downstair disappears
			tileCollector.remove(floor, x, y);
			
		} else if(type.equals("item")) {
			itemCollector.remove(floor, x, y);
		} else if(type.equals("door")) {
			doorCollector.setDoorOpenAnim(floor, x, y);
			doorCollector.remove(floor, x, y);
			// then pressed key will be ignored, and player has to watch the animation
			ps.setFocusState(PlayState.ONLOOKER);
		} else if(type.equals("npc")) {
			npcCollector.remove(floor, x, y);
		}
	}
	
	
	public void eliminate(int floor, int x, int y, int bx, int by, int fightTimes, String type) {
		// for monster only
		if(type.equals("monster")) {
			boom.init(x, y, bx, by, fightTimes);
			ps.setFocusState(PlayState.ONLOOKER);
			// there might be some dialogues, but they will carry out after boom finishes
		}
	}
	
	// ----------------- manipulate --------------------
	public void update() {
		
		// update door
		if(doorCollector.isAnimEnd()) {
			ps.setFocusState(PlayState.BRAVER);
			doorCollector.endAnime();
		} else
			doorCollector.updateAnimSeq();
		
		// update monster
		monsterCollector.updateAnimSeq();
		
		
		// update npc
		npcCollector.updateAnimSeq();
		
		// update shop
		shopCollector.updateAnimSeq();
	}
	
	public void draw(Graphics dbg) {
		tileCollector.draw(dbg);
		itemCollector.draw(dbg);
		doorCollector.draw(dbg);
		monsterCollector.draw(dbg);
		npcCollector.draw(dbg);
		shopCollector.draw(dbg);
	}

}
