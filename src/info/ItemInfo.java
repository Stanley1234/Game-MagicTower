package info;

import java.util.ArrayList;

public class ItemInfo {
	// basic attribute
	private String prefix;
	private ArrayList<String> events;
	
	// effects on braver
	int level, hp, attack, defend, coins, exp, redKeys, blueKeys, yellowKeys;
	
	public ItemInfo(String prefix, ArrayList<String> events, int level, int hp, int attack, int defend,
			int coins, int exp, int redKeys, int blueKeys, int yellowKeys) {
	
		this.prefix = prefix;
		this.events = events;
		this.level = level;
		this.hp = hp;
		this.attack = attack;
		this.defend = defend;
		this.coins = coins;
		this.exp = exp;
		this.redKeys = redKeys;
		this.blueKeys = blueKeys;
		this.yellowKeys = yellowKeys;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public ArrayList<String> getEvents() {
		return events;
	}
	public int getLevel() {
		return level;
	}
	
	public int getHp() {
		return hp;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getDefend() {
		return defend;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public int getExp() {
		return exp;
	}
	
	public int getRedKeys() {
		return redKeys;
	}
	
	public int getBlueKeys() {
		return blueKeys;
	}
	
	public int getYellowKeys() {
		return yellowKeys;
	}
	
	public boolean isHasEvents() {
		return !events.get(0).equals("null");
	}
	
	// debug
	@Override
	public String toString() {
		return "[prefix=" + prefix + ",events=" + events + "]"; 
	}
	
}
