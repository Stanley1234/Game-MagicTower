package manipulate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import collector.ImagesLoader;
import gamestate.PlayState;
import manager.Content;
import manager.Floor;

public class BraverAttr {
	Braver braver;

	public BraverAttr(Braver braver) {
		this.braver = braver;
	}

	public void update() {

	}

	public void draw(Graphics dbg) {
		// draw the background
		BufferedImage bgImage = ImagesLoader.getImage("road");
		for (int i = PlayState.ATTR_Y_OFFSET / ImagesLoader.ICON_SIZE; i < PlayState.ATTR_Y_END
				/ ImagesLoader.ICON_SIZE; i++) {
			for (int j = PlayState.ATTR_X_OFFSET / ImagesLoader.ICON_SIZE; j < PlayState.ATTR_X_END
					/ ImagesLoader.ICON_SIZE; j++) {
				dbg.drawImage(bgImage, j * ImagesLoader.ICON_SIZE, i * ImagesLoader.ICON_SIZE, null);
			}
		}
		
		
		// draw the attrs of braver
		ArrayList<Integer> braverInfo = braver.getBraverInfo();
	
		// braver icon
		dbg.drawImage(ImagesLoader.getImage("braver_down"), 
				PlayState.ATTR_X_OFFSET + 5, 
				PlayState.ATTR_Y_OFFSET + 5, 
				PlayState.ATTR_X_OFFSET + 55, 
				PlayState.ATTR_Y_OFFSET + 55, 
				0, 0, ImagesLoader.getImage("braver_down").getWidth(), 
				ImagesLoader.getImage("braver_down").getHeight(), null);
		
		
		// attri
		dbg.setColor(Color.WHITE);
		// draw level
		dbg.setFont(Content.Gathora_20);
		dbg.drawString("Level ", PlayState.ATTR_X_OFFSET + 55, PlayState.ATTR_Y_OFFSET + 50);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		dbg.drawString(braverInfo.get(0).toString(), PlayState.ATTR_X_OFFSET + 125, PlayState.ATTR_Y_OFFSET + 50);
		
		// draw hp
		dbg.setFont(Content.Gathora_20);
		dbg.drawString("H ", PlayState.ATTR_X_OFFSET + 10, PlayState.ATTR_Y_OFFSET + 90);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 20));
		dbg.drawString(braverInfo.get(1).toString(), PlayState.ATTR_X_OFFSET + 45, PlayState.ATTR_Y_OFFSET + 90);
		
		// draw attack
		dbg.setFont(Content.Gathora_20);
		dbg.drawString("A ", PlayState.ATTR_X_OFFSET + 10, PlayState.ATTR_Y_OFFSET + 120);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 20));
		dbg.drawString(braverInfo.get(2).toString(), PlayState.ATTR_X_OFFSET + 45, PlayState.ATTR_Y_OFFSET + 120);
		
		// draw defend
		dbg.setFont(Content.Gathora_20);
		dbg.drawString("D", PlayState.ATTR_X_OFFSET + 10, PlayState.ATTR_Y_OFFSET + 150);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 20));
		dbg.drawString(braverInfo.get(3).toString(), PlayState.ATTR_X_OFFSET + 45, PlayState.ATTR_Y_OFFSET + 150);
		
		// draw coins
		dbg.setFont(Content.Gathora_20);
		dbg.drawString("C", PlayState.ATTR_X_OFFSET + 10, PlayState.ATTR_Y_OFFSET + 180);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 20));
		dbg.drawString(braverInfo.get(4).toString(), PlayState.ATTR_X_OFFSET + 45, PlayState.ATTR_Y_OFFSET + 180);
		
		// draw exp
		dbg.setFont(Content.Gathora_20);
		dbg.drawString("E", PlayState.ATTR_X_OFFSET + 10, PlayState.ATTR_Y_OFFSET + 210);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 20));
		dbg.drawString(braverInfo.get(5).toString(), PlayState.ATTR_X_OFFSET + 45, PlayState.ATTR_Y_OFFSET + 210);
		
		// draw yellow keys
		dbg.drawImage(ImagesLoader.getImage("red_key"), 
				PlayState.ATTR_X_OFFSET + 5,
				PlayState.ATTR_Y_OFFSET + 230, 
				PlayState.ATTR_X_OFFSET + 45, 
				PlayState.ATTR_Y_OFFSET + 270, 
				0, 0, ImagesLoader.getImage("red_key").getWidth(), 
				ImagesLoader.getImage("red_key").getHeight(), null);
		dbg.setColor(new Color(255, 138, 140));
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 30));
		dbg.drawString("X", PlayState.ATTR_X_OFFSET + 65, PlayState.ATTR_Y_OFFSET + 265);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 40));
		dbg.drawString(braverInfo.get(6).toString(), PlayState.ATTR_X_OFFSET + 105, PlayState.ATTR_Y_OFFSET + 265);
		
		// draw blue keys
		dbg.drawImage(ImagesLoader.getImage("blue_key"), 
				PlayState.ATTR_X_OFFSET + 5,
				PlayState.ATTR_Y_OFFSET + 280, 
				PlayState.ATTR_X_OFFSET + 45, 
				PlayState.ATTR_Y_OFFSET + 320, 
				0, 0, ImagesLoader.getImage("blue_key").getWidth(), 
				ImagesLoader.getImage("blue_key").getHeight(), null);
		dbg.setColor(new Color(206, 207, 255));
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 30));
		dbg.drawString("X", PlayState.ATTR_X_OFFSET + 65, PlayState.ATTR_Y_OFFSET + 315);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 40));
		dbg.drawString(braverInfo.get(7).toString(), PlayState.ATTR_X_OFFSET + 105, PlayState.ATTR_Y_OFFSET + 315);
		
		// draw red keys
		dbg.drawImage(ImagesLoader.getImage("yellow_key"), 
				PlayState.ATTR_X_OFFSET + 5,
				PlayState.ATTR_Y_OFFSET + 330, 
				PlayState.ATTR_X_OFFSET + 45, 
				PlayState.ATTR_Y_OFFSET + 370, 
				0, 0, ImagesLoader.getImage("yellow_key").getWidth(), 
				ImagesLoader.getImage("yellow_key").getHeight(), null);
		dbg.setColor(new Color(255, 207, 173));
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 30));
		dbg.drawString("X", PlayState.ATTR_X_OFFSET + 65, PlayState.ATTR_Y_OFFSET + 365);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 40));
		dbg.drawString(braverInfo.get(8).toString(), PlayState.ATTR_X_OFFSET + 105, PlayState.ATTR_Y_OFFSET + 365);
		
		// draw Floor
		dbg.setColor(Color.WHITE);
		dbg.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		dbg.drawString("FLOOR  " + Floor.getCurFloor(), 
				PlayState.ATTR_X_OFFSET + 25, PlayState.ATTR_Y_OFFSET + 410);
		
		
		// draw the ornamented frame
		dbg.setColor(new Color(204, 102, 0));
		dbg.setColor(new Color(204, 102, 0));
		dbg.fillRect(PlayState.ATTR_X_OFFSET - 5, PlayState.ATTR_Y_OFFSET - 5, PlayState.ATTR_X_END - PlayState.ATTR_X_OFFSET, 5); // attr up
		dbg.fillRect(PlayState.ATTR_X_OFFSET - 5, PlayState.ATTR_Y_END, PlayState.ATTR_X_END - PlayState.ATTR_X_OFFSET + 5, 5);  // attr down		
		dbg.fillRect(PlayState.ATTR_X_OFFSET - 5, PlayState.ATTR_Y_OFFSET, 5, PlayState.ATTR_Y_END - PlayState.ATTR_Y_OFFSET); // attr left
		dbg.fillRect(PlayState.ATTR_X_END - 5, PlayState.ATTR_Y_OFFSET - 5, 5, PlayState.ATTR_Y_END - PlayState.ATTR_Y_OFFSET + 5);  // attr right
		
		// extra ornamented frame
		dbg.fillRect(PlayState.ATTR_X_OFFSET - 5, PlayState.ATTR_Y_OFFSET + 220, PlayState.ATTR_X_END - PlayState.ATTR_X_OFFSET + 5, 5);  // attr down		
		dbg.fillRect(PlayState.ATTR_X_OFFSET - 5, PlayState.ATTR_Y_OFFSET + 385, PlayState.ATTR_X_END - PlayState.ATTR_X_OFFSET + 5, 5);  // attr down		
	}
}
