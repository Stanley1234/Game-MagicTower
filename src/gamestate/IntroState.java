package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import main.GameFrame;
import main.GamePanel;
import manager.GameStatesManager;
import manager.Keys;

public class IntroState extends GameState{
	
	private static final int FADE_IN = 500;  // ms
	private static final int STILL = 300; // ms
	private static final int FADE_OUT = 500; // ms
	
	private int totalTime = 0;
	private int alpha = 255;  // 0 - 255
	
	public IntroState(GameStatesManager gsm) {
		super(gsm);
	}
	
	@Override
	public void update() {
		totalTime += GameFrame.PERIOD;
		
		if(totalTime < FADE_IN) {
			alpha -= (int)(255 * GameFrame.PERIOD / FADE_IN);
			if(alpha < 0)
				alpha = 0;
		} else if(totalTime < FADE_IN + STILL) ;
			// alpha does not change
		else if(totalTime < FADE_IN + STILL + FADE_OUT) {
			alpha += (int)(255 * GameFrame.PERIOD / FADE_OUT);
			if(alpha > 255)
				alpha = 255;
		} else 
			gsm.setFocusState(GameStatesManager.SELECT);
		
	}
	
	@Override
	public void handleInput() {
		if(Keys.getCurCode() == KeyEvent.VK_ENTER) 
			gsm.setFocusState(GameStatesManager.SELECT);
		
	}
	
	@Override
	public void draw(Graphics dbg) {
		dbg.setColor(Color.GREEN);
		dbg.fillRect(0, 0, GamePanel.PWIDTH, GamePanel.PHEIGHT);
		
		dbg.setColor(new Color(0, 0, 0, alpha));
		dbg.fillRect(0, 0, GamePanel.PWIDTH, GamePanel.PHEIGHT);
		
		
	}
}
