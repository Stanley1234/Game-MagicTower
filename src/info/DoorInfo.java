package info;

public class DoorInfo {

	private String prefix;
	private int type;
	private boolean isOpenByKey;    // describe whether the door can be opened by key
								    // it will be opened through some events
	// Once the door is opened, the animation is played and the door disappears from the hashmap
	
	public DoorInfo(String prefix, int type, boolean isOpenByKey) {
		this.prefix = prefix;
		this.type = type;
		this.isOpenByKey = isOpenByKey;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean isOpenByKey() {
		return isOpenByKey;
	}
	
	public void setOpenByKey(boolean isOpenByKey) {
		this.isOpenByKey = isOpenByKey;
	}
}
