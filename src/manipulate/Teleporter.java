package manipulate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import collector.ClipLoader;
import collector.ImagesLoader;
import gamestate.PlayState;
import manager.Floor;
import manager.Keys;

public class Teleporter {
	private PlayState ps;
	private Braver braver;
	private MapLoader mapLoader;
	private int curIndex = 1;  // from 1 - Floor.MaxFloorNum
	
	private static final int MENU_X_OFFSET = PlayState.GAME_X_OFFSET + (PlayState.GAME_X_END - PlayState.GAME_X_OFFSET) / 13 * 2;
	private static final int MENU_X_END = MENU_X_OFFSET + ImagesLoader.ICON_SIZE * 9;
	
	private static final int MENU_Y_OFFSET = PlayState.GAME_Y_OFFSET + (PlayState.GAME_Y_END - PlayState.GAME_Y_OFFSET) / 13 * 2;
	private static final int MENU_Y_END = MENU_Y_OFFSET + ImagesLoader.ICON_SIZE * 9;
	
	public Teleporter(PlayState ps, Braver braver, MapLoader mapLoader) {
		this.ps = ps;
		this.braver = braver;
		this.mapLoader = mapLoader;
	}
	
	public void handleInput() {
		if(Keys.getCurCode() == KeyEvent.VK_UP) {
			ClipLoader.play("shift_selected_item", ClipLoader.FFF);
			curIndex -= 3;
			if(curIndex <= 0) {
				curIndex += 3;
				if(curIndex == 1)
					curIndex = Floor.FLOOR_NUM;
				else if(curIndex == 2)
					curIndex = Floor.FLOOR_NUM - 2;
				else if(curIndex == 3)
					curIndex = Floor.FLOOR_NUM - 1;
				
				
			}
				
		} else if(Keys.getCurCode() == KeyEvent.VK_DOWN) {
			ClipLoader.play("shift_selected_item", ClipLoader.FFF);
			
			curIndex += 3;
			if(curIndex > Floor.FLOOR_NUM) {
				// here after reaching the bottom, the focus should go to the next coloum
				curIndex %= 3;
				curIndex = (curIndex + 1) % 3;
				
				if(curIndex == 0)
					curIndex = 3;
			}
			
			
		} else if(Keys.getCurCode() == KeyEvent.VK_LEFT) {
			ClipLoader.play("shift_selected_item", ClipLoader.FFF);
			
			curIndex --;
			if(curIndex <= 0)
				curIndex += Floor.FLOOR_NUM;
			
			
		} else if(Keys.getCurCode() == KeyEvent.VK_RIGHT) {
			ClipLoader.play("shift_selected_item", ClipLoader.FFF);
			
			curIndex ++ ;
			if(curIndex > Floor.FLOOR_NUM) 
				curIndex -= Floor.FLOOR_NUM;
			
		} else if(Keys.getCurCode() == KeyEvent.VK_ENTER) {
			ClipLoader.play("confirm", ClipLoader.FFF);
			
			// can only reach the floor that has been before
			if(Floor.isBeenFloor(curIndex)) {
				Floor.setCurFloor(curIndex);
				braver.setLocation(mapLoader.getExitPos(curIndex));
				
				ps.setFocusState(PlayState.BRAVER);
				curIndex = 1;
			}
		} else if(Keys.getCurCode() == KeyEvent.VK_M) {
			ClipLoader.play("quit", ClipLoader.FFF);
			curIndex = 1;
			ps.setFocusState(PlayState.BRAVER);
		}

	}
	
	public void draw(Graphics dbg) {
		// draw the background tile
		for(int i = MENU_Y_OFFSET / ImagesLoader.ICON_SIZE;i < MENU_Y_END / ImagesLoader.ICON_SIZE;i ++)
			for(int j = MENU_X_OFFSET / ImagesLoader.ICON_SIZE;j < MENU_X_END / ImagesLoader.ICON_SIZE;j ++) {
				dbg.drawImage(ImagesLoader.getImage("road"), 
						j * ImagesLoader.ICON_SIZE, 
						i * ImagesLoader.ICON_SIZE, null);
			}
		// draw the string
		// from 1 - 21
		int counter = 1;
		
		// !!!!!!!! setFont !!!!!!!!
		dbg.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		int gapX = (MENU_X_END - MENU_X_OFFSET) / 3;
		int gapY = (int) ((MENU_Y_END - MENU_Y_OFFSET - 85) / Math.ceil(Floor.FLOOR_NUM / 3));
		for(int i = 1;i <= Math.ceil(Floor.FLOOR_NUM / 3);i ++) 
			for(int j = 1;j <= 3;j ++) {
				if(counter == curIndex)
					dbg.setColor(Color.GREEN);
				else if(Floor.isBeenFloor(counter)) 
					dbg.setColor(Color.WHITE);
				else 
					dbg.setColor(Color.GRAY);
				
				dbg.drawString("Floor " + counter, 
						MENU_X_OFFSET + 10 + (j - 1) * gapX,
						MENU_Y_OFFSET + 80 + (i - 1) * gapY );
				counter ++;
			}
		
		
		
		// draw the ornamented frame
		dbg.setColor(new Color(204, 102, 0));
		dbg.fillRect(MENU_X_OFFSET - 5, MENU_Y_OFFSET - 5, MENU_X_END - MENU_X_OFFSET, 5); // dialogue up
		dbg.fillRect(MENU_X_OFFSET - 5, MENU_Y_END, MENU_X_END - MENU_X_OFFSET + 5, 5);  // dialogue down
		dbg.fillRect(MENU_X_OFFSET - 5, MENU_Y_OFFSET, 5, MENU_Y_END - MENU_Y_OFFSET); // dialogue left
		dbg.fillRect(MENU_X_END - 5, MENU_Y_OFFSET - 5, 5, MENU_Y_END - MENU_Y_OFFSET + 5);  // dialogue right
	}
}
