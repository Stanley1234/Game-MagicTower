package info;

public class TileInfo {
	
	private String prefix;
	private int type;
	private boolean isEnabled;
	private boolean isObstacle;
	
	public TileInfo(String prefix, int type, boolean isEnabled, boolean isObstacle) {
		/*
		 * TileInfo() loads the intial status of each tile
		 * The attributes of tiles may change later in the game
		 * */
		this.prefix = prefix;
		this.type = type;
		this.isEnabled = isEnabled;
		this.isObstacle = isObstacle;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public int getType() {
		return type;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public boolean isObstacle() {
		return isObstacle;
	}
	
	@Override
	public String toString() {
		return "[prefix=" + prefix + ",type=" + type + ",enabled=" + isEnabled + "]";
	}
}
