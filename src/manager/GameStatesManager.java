package manager;

import java.awt.Graphics;

import gamestate.GameState;
import gamestate.IntroState;
import gamestate.PlayState;
import gamestate.SelectState;

public class GameStatesManager {
	
	public static final int INTRO = 0;
	public static final int SELECT = 5;
	public static final int GAMEPLAY = 20;
	public static final int GAMEOVER = 30;
	private static final int FRAME_NUM = 30;
	
	private int focusState = INTRO;
	private GameState[] gameStates = new GameState[FRAME_NUM];
	
	public GameStatesManager() {
		gameStates[INTRO] = new IntroState(this);
		gameStates[SELECT] = new SelectState(this);
		gameStates[GAMEPLAY] = new PlayState(this);
	}
	
	public void setFocusState(int focusState) {
		this.focusState = focusState;
	}
	
	public void update() {
		gameStates[focusState].update();
	}
	
	public void handleInput() {
		if(Keys.getCurCode() != Keys.NO_INPUT) {
			gameStates[focusState].handleInput();
			Keys.clear();
		}
	}
	
	public void draw(Graphics dbg) {
		gameStates[focusState].draw(dbg);
	}
}
