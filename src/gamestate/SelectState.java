package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import collector.ClipLoader;
import main.GameFrame;
import main.GamePanel;
import manager.Content;
import manager.GameStatesManager;
import manager.Keys;

public class SelectState extends GameState{
	
	// menu
	private static final int START_GAME = 0;
	private static final int LOAD_GAME = 1;
	private static final int QUIT_GAME = 2;
	private int curSelected = START_GAME;
	
	// image effects
	private static final int FADE_OUT = 1000;
	private boolean isFading = false;
	private int alpha = 0;
	
	// sound effects
	
	private static final String bgm = "select_bgm";
	

	public SelectState(GameStatesManager gsm) {
		super(gsm);
		
	}
	
	@Override
	public void update() {
		if(ClipLoader.isStop(bgm)) 
			ClipLoader.play(bgm, ClipLoader.MP);
			
		
		
		if(isFading) {
			alpha += (int)(255 * GameFrame.PERIOD / FADE_OUT);
			if(alpha > 255) {
				alpha = 255;
				gsm.setFocusState(GameStatesManager.GAMEPLAY);
				ClipLoader.stop(bgm);
			}
		}
		
	}
	
	@Override
	public void handleInput() {
		if(Keys.getCurCode() == KeyEvent.VK_DOWN) {
			curSelected = (curSelected + 1) % 3;
			
			ClipLoader.play("shift_selected_item", ClipLoader.FFF);
			
		} else if(Keys.getCurCode() == KeyEvent.VK_UP) {
			curSelected --;
			if(curSelected < 0)
				curSelected += 3;
			
			ClipLoader.play("shift_selected_item", ClipLoader.FFF);
			
		} else if (Keys.getCurCode() == KeyEvent.VK_ENTER) {
			
			ClipLoader.play("confirm", ClipLoader.FFF);
			if(curSelected == START_GAME) {
				//gsm.setFocusState(GameStatesManager.GAMEPLAY);
				isFading = true;
				
			} else if(curSelected == LOAD_GAME) {
				
			} else if(curSelected == QUIT_GAME) {
				ClipLoader.play("quit", ClipLoader.FFF);
				System.exit(0);
			}
		}
		
	}
	
	@Override
	public void draw(Graphics dbg) {
		dbg.setColor(Color.BLACK);
		dbg.fillRect(0, 0, GamePanel.PWIDTH, GamePanel.PHEIGHT);
		
		dbg.setFont(Content.AladdinRegular_Plain_80);
		dbg.setColor(Color.WHITE);
		dbg.drawString("Magic Tower", GamePanel.PWIDTH / 5, GamePanel.PHEIGHT / 5);

		dbg.setFont(Content.Airacobra);
		
		dbg.setColor(Color.WHITE);
		if(curSelected == START_GAME) 
			dbg.setColor(Color.GREEN);
		dbg.drawString("START GAME", 200, 250);
		
		dbg.setColor(Color.WHITE);
		if(curSelected == LOAD_GAME) 
			dbg.setColor(Color.GREEN);
		dbg.drawString("LOAD   GAME", 200, 290);
		
		dbg.setColor(Color.WHITE);
		if(curSelected == QUIT_GAME) 
			dbg.setColor(Color.GREEN);
		dbg.drawString("QUIT    GAME", 200, 330);
		
		if(isFading) {
			dbg.setColor(new Color(0, 0, 0, alpha));
			dbg.fillRect(0, 0, GamePanel.PWIDTH, GamePanel.PHEIGHT);
		}
	}
}
