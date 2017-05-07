package collector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import info.ClipInfo;

public class ClipLoader {
	
	public static final float FFF = 66.0f;
	public static final float PP = -15.0f;
	public static final float MP = -5.0f;
	private static final String SOUND_DIR = "Sounds/";
	private static final String SOUND_CONFIG = "clipsInfo.txt";
	private static HashMap<String, ClipInfo> clipsMap = new HashMap<String, ClipInfo>();
	
	
	static {
		InputStream in = ClipLoader.class.getClassLoader().getResourceAsStream(SOUND_DIR + SOUND_CONFIG);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		try {
			String line;
			while(!(line = br.readLine()).equals("#")) {
				if(line.startsWith("//"))
					continue;
				
				StringTokenizer stk = new StringTokenizer(line);
				if(stk.countTokens() != 2)  // blank line 
					continue;
				
				String prefix = stk.nextToken();
				String fileName = stk.nextToken();
				clipsMap.put(prefix, new ClipInfo(fileName, prefix));
			}
		} catch (Exception e) {
			
		}
		
	}
	
	public static void play(String prefix, float volOffset) {
		clipsMap.get(prefix).play(volOffset);
	}
	
	public static void stop(String prefix) {
		clipsMap.get(prefix).stop();
	}
	
	public static void pause() {
		// this is called when pause the game, all the clips are paused
	}
	
	public static void resume() {
		
	}
	
	public static boolean isStop(String prefix) {
		return clipsMap.get(prefix).isStop();
	}

}
