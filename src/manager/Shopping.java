package manager;

import java.util.ArrayList;

public class Shopping {

	private static ArrayList<String> productionInfo;
	
	public static void setProductionInfo(ArrayList<String> pi) {
		productionInfo = pi;
	}
	
	public static ArrayList<String> getProductionInfo() {
		return productionInfo;
	}
	
	
}
