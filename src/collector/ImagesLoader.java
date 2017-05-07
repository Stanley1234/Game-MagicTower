package collector;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class ImagesLoader {

	private static String IMAGES_CONFIG = "Images/imsInfo.txt";
	private static String IMAGES_DIR = "Images/";
	private static HashMap<String, ArrayList<BufferedImage>> imagesMap = new HashMap<String, ArrayList<BufferedImage>>();
	
	public static int ICON_SIZE;
	
	public static void init() {
		try {
			// obtain inputstream of "imsInfo.txt"
			InputStream in = ImagesLoader.class.getClassLoader().getResourceAsStream(IMAGES_CONFIG);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			// Analyze the file
			String line;
			while((line = br.readLine()) != null) {
				if(line.equals("") || line.startsWith("//"))
					continue;
				
				StringTokenizer stk = new StringTokenizer(line, "=");
				StringTokenizer stk_space = null;
				
				// get the file name
				stk.nextToken();
				String fnm = stk.nextToken();
				
				// get the row number
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int row = Integer.parseInt(stk.nextToken());
				
				// get the col number
				line = br.readLine();
				stk = new StringTokenizer(line, "=");
				stk.nextToken();
				int col = Integer.parseInt(stk.nextToken());
				
				////System.out.println("fnm:" + fnm + " row:" + row + " col:" + col);
				
				// get the source image 
				BufferedImage source = ImageIO
						.read(new URL(ImagesLoader.class.getProtectionDomain().getCodeSource().getLocation(), IMAGES_DIR + fnm));
				
				int imWidth = source.getWidth() / col;
				int imHeight = source.getHeight() / row;
				int transparency = source.getTransparency();
				
				for(int i = 0;i < row;i ++) {
					for(int j = 0;j < col;j ++) {
						
						while(true) {  // ignore the blank lines
							line = br.readLine();
							if(!(line.startsWith("//") || line.equals("")))
								break;
						}
						
						// get the prefix
						stk_space = new StringTokenizer(line);
						stk = new StringTokenizer(stk_space.nextToken(), "=");
						stk.nextToken();
						String prefix = stk.nextToken();
						
				
						if(prefix.equals("null"))   // ignore the picture 
							continue;
					
						// get the corresponding part of image
						BufferedImage copy = new BufferedImage(imWidth, imHeight, transparency);
						Graphics g = copy.getGraphics();
						g.drawImage(source, 0, 0, imWidth, imHeight, j * imWidth, i * imHeight, (j+1)*imWidth, (i+1)*imHeight, null);
						g.dispose();
							
						// put into hashmap
						if(imagesMap.containsKey(prefix)) {
							ArrayList<BufferedImage> temp = imagesMap.get(prefix);
							temp.add(copy);
							imagesMap.replace(prefix, temp);
						} else {
							ArrayList<BufferedImage> temp = new ArrayList<BufferedImage>();
							temp.add(copy);
							imagesMap.put(prefix, temp);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// since all the icons have the same size(width and height are also the same)
		// only need one icon to calculate the ICON_WIDTH and ICON_HEIGHT
		BufferedImage br = imagesMap.get("road").get(0);
		ICON_SIZE = br.getWidth();
		
	}
	
	public static BufferedImage getImage(String prefix) {
		if(!imagesMap.containsKey(prefix)) {
			System.out.println("#0:no element");
			return null;
		}
		return imagesMap.get(prefix).get(0);
	}
	
	public static BufferedImage getImage(String prefix, int index) {
		if(!imagesMap.containsKey(prefix)) {
			System.out.println("#1:no element");
			return null;
		}
		return imagesMap.get(prefix).get(index);
	}
	
	public static int getNumOfImages(String prefix) {
		if(!imagesMap.containsKey(prefix)) {
			System.out.println("num: no element");
			return -1;
		}
		return imagesMap.get(prefix).size();
	}
	
	/*public static void debug(Graphics g) {
		Iterator<Entry<String, ArrayList<BufferedImage>>> it = imagesMap.entrySet().iterator();
		
		System.out.println(imagesMap.entrySet().size());
		while(it.hasNext()) {
			Entry entry = it.next();
			String prefix = (String) entry.getKey();
			ArrayList<BufferedImage> iconArray = (ArrayList<BufferedImage>) entry.getValue();
			
			System.out.println(prefix + " size: " + iconArray.size());
			
			Iterator<BufferedImage> iterator = iconArray.iterator();
			while(iterator.hasNext()) {
				BufferedImage icon = iterator.next();
				g.drawImage(icon, 100, 100, null);
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				g.clearRect(0, 0, 200, 200);
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}*/
}
