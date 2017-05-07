package info;

import java.util.ArrayList;

public class  MonsterInfo implements Comparable<MonsterInfo>{

	private String prefix;
	private ArrayList<String> bfEvents; // before-fight events
	private ArrayList<String> afEvents; // after-fight events
	private int hp, attack, defend, coins, exp;
	private boolean isBfeEnded = false;

	public MonsterInfo(String prefix, ArrayList<String> bfe, ArrayList<String> afe, int hp, int attack, int defend,
			int coins, int exp) {
		this.prefix = prefix;
		this.bfEvents = bfe;
		this.afEvents = afe;
		this.hp = hp;
		this.attack = attack;
		this.defend = defend;
		this.coins = coins;
		this.exp = exp;
		
		if(bfEvents.get(0).equals("null")) {
			setBfeEnded(true);
		}
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

	public String getPrefix() {
		return prefix;
	}
	
	public ArrayList<String> getBfEvents() {
		return bfEvents;
	}

	public ArrayList<String> getAfEvents() {
		return afEvents;
	}
	
	public int getCoins() {
		return coins;
	}
	public int getExp() {
		return exp;
	}
	
	public boolean isBfeEnded() {
		return isBfeEnded;
	}

	public void setBfeEnded(boolean isBfeEnded) {
		this.isBfeEnded = isBfeEnded;
	}

	@Override
	public int compareTo(MonsterInfo o) {
		if(hp + attack + defend <= o.getHp() + o.getAttack() + o.getDefend())
			return -1;
		return 1;
	}
	
}
