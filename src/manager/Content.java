package manager;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class Content {
	
	public static Font Airacobra;
	public static Font AladdinRegular_Plain_80;
	public static Font Ancient_Greek_25;
	public static Font Gathora_30, Gathora_20;

	public static void init() {
		try {
			Airacobra = Font.createFont(Font.TRUETYPE_FONT, Content.class.getClassLoader().getResourceAsStream("Fonts/Airacobra.ttf"));
			Airacobra = Airacobra.deriveFont(Font.BOLD, 35);
			
			AladdinRegular_Plain_80 = Font.createFont(Font.TRUETYPE_FONT, Content.class.getClassLoader().getResourceAsStream("Fonts/Aladdin Regular.ttf"));
			AladdinRegular_Plain_80 = AladdinRegular_Plain_80.deriveFont(Font.PLAIN, 80);
			
			Ancient_Greek_25 = Font.createFont(Font.TRUETYPE_FONT, Content.class.getClassLoader().getResourceAsStream("Fonts/Ancient Geek.ttf"));
			Ancient_Greek_25 = Ancient_Greek_25.deriveFont(Font.BOLD, 25);
			
			Gathora_30 = Font.createFont(Font.TRUETYPE_FONT, Content.class.getClassLoader().getResourceAsStream("Fonts/Basset Bold.ttf"));
			Gathora_30 = Gathora_30.deriveFont(Font.BOLD, 30);
			Gathora_20 = Gathora_30.deriveFont(Font.PLAIN, 25);
			
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Airacobra);
			ge.registerFont(AladdinRegular_Plain_80);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
	}
}
