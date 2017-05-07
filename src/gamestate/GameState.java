package gamestate;

import java.awt.Graphics;

import manager.GameStatesManager;

public abstract class GameState {
	
	GameStatesManager gsm;
	public GameState(GameStatesManager gsm) {
		this.gsm = gsm;
	}
	
	public void update() {}
	
	public void handleInput() {}
	
	public void draw(Graphics dbg) {}
}
