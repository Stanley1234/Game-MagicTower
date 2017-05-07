package info;

import java.util.ArrayList;

public class ShopInfo {
	ArrayList<String> prefix;       // there might be many images making up a shop
	ArrayList<String> productInfo;  
	ArrayList<Integer> animePart;   // specify which parts the animation will be played
	
	public ShopInfo(ArrayList<String> prefix, ArrayList<String> productInfo, ArrayList<Integer> animePart) {
		this.prefix = prefix;
		this.productInfo = productInfo;
		this.animePart = animePart;
	}
	
	public ArrayList<String> getPrefix() {
		return prefix;
	}
	
	public ArrayList<String> getProductInfo() {
		return productInfo;
	}
	
	public ArrayList<Integer> getAnimePart() {
		return animePart;
	}
	
	public int shopSize() {
		return prefix.size();
	}
	
	public boolean isAnimePart(int index) {
		return animePart.contains(index);
	}
	
}
