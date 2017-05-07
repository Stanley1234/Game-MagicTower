package gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import collector.ClipLoader;
import collector.ImagesLoader;
import effects.Boom;
import main.GamePanel;
import manager.Dialogue;
import manager.GameStatesManager;
import manager.Keys;
import manipulate.Braver;
import manipulate.BraverAttr;
import manipulate.MapLoader;
import manipulate.MonsterDetector;
import manipulate.ShopMenu;
import manipulate.Teleporter;
import manipulate.TimeCounter;

public class PlayState extends GameState {
	
	// draw splitting graphs: info for bravers and 
	public static final int INSET = ImagesLoader.ICON_SIZE;   // the inset for up, down, left and right
	public static final int ATTR_X_OFFSET = ImagesLoader.ICON_SIZE;
	public static final int ATTR_X_END = ATTR_X_OFFSET + ImagesLoader.ICON_SIZE * 5;
	public static final int GAME_X_OFFSET = ImagesLoader.ICON_SIZE * 7;  // the starting point of game frame
	public static final int GAME_X_END = GAME_X_OFFSET + ImagesLoader.ICON_SIZE * 13;
	public static final int ATTR_Y_OFFSET = INSET;
	public static final int ATTR_Y_END = ATTR_Y_OFFSET + 13 * ImagesLoader.ICON_SIZE;
	public static final int GAME_Y_OFFSET = INSET;
	public static final int GAME_Y_END = GAME_Y_OFFSET + 13 * ImagesLoader.ICON_SIZE;
	
	// offset for the beginning of the dialogue
	public static final int DIALOGUE_X_OFFSET = GAME_X_OFFSET + ImagesLoader.ICON_SIZE / 2;
	public static final int DIALOGUE_Y_OFFSET = GAME_Y_END - ImagesLoader.ICON_SIZE * 7 / 2;
	public static final int DIALOGUE_X_END = DIALOGUE_X_OFFSET + ImagesLoader.ICON_SIZE * 12;
	public static final int DIALOGUE_Y_END = DIALOGUE_Y_OFFSET + ImagesLoader.ICON_SIZE * 3;
	
	// focus state
	public static final int BRAVER = 0;
	public static final int SHOP = 1;
	public static final int DIALOGUE = 2;
	public static final int ONLOOKER = 3;
	public static final int MONSTER_DETECTOR = 5;
	public static final int TELEPORTER = 6;
	
	private int focusState = BRAVER;
	
	// elements for game
	private TimeCounter timeCounter = new TimeCounter();
	private Braver braver;
	private ShopMenu shopMenu;
	private MapLoader mapLoader;
	private BraverAttr braverAttr;
	private MonsterDetector monsterDetector;
	private Teleporter teleporter;
	private Boom boom;
	
	// for music
	private final String[] musicPrefix = {"play_bgm_1", "play_bgm_2"};
	private int curMusicIndex = 1;
	
	
	
	public PlayState(GameStatesManager gsm) {
		super(gsm);
		boom = new Boom();
		mapLoader = new MapLoader(this, boom);
		braver = new Braver(this, mapLoader, 6, 11);
		shopMenu = new ShopMenu(braver, this);
		monsterDetector = new MonsterDetector(this, mapLoader, braver);
		teleporter = new Teleporter(this, braver, mapLoader);
		braverAttr = new BraverAttr(braver);
	}
	
	public void setFocusState(int focusState) {
		this.focusState = focusState;
		if(focusState == MONSTER_DETECTOR)
			monsterDetector.init();
	}
	
	public void update() {
		// update the bgm
		if(ClipLoader.isStop(musicPrefix[curMusicIndex])) {
			curMusicIndex = (curMusicIndex + 1) % (musicPrefix.length);
			ClipLoader.play(musicPrefix[curMusicIndex], ClipLoader.MP);
		}
		
		// update the clock
		timeCounter.updateTick();
		
		// update the game components
		if(focusState == BRAVER || focusState == SHOP || focusState == ONLOOKER || focusState == DIALOGUE) {
			braver.updateSeq();
			mapLoader.update();
			
			if(!boom.isEndOfUse() && boom.isFinish()) {
				mapLoader.setMonsterDisappear();
				boom.dispose();
			}
			
			if(!boom.isEndOfUse()) {
				braver.updateAttrForFighting(boom.getBoomTimes(), boom.isLastAnime());
				boom.updateSeq();
			}
					
		} else if(focusState == MONSTER_DETECTOR){
			monsterDetector.updateAnimSeq();
		}
	}
	
	public void handleInput() {
		if(focusState == BRAVER) {
			
			braver.handleInput();
			
		} else if(focusState == DIALOGUE && Keys.getCurCode() == KeyEvent.VK_ENTER) {
			if(Dialogue.isHasNext()) 
				Dialogue.nextDialogue();
			else 
				setFocusState(BRAVER);
			ClipLoader.play("confirm", ClipLoader.FFF);
			
		} else if(focusState == SHOP) {
			shopMenu.handleInput();
		} else if (focusState == TELEPORTER) {
			teleporter.handleInput();
		} else if(focusState == MONSTER_DETECTOR) {
			monsterDetector.handleInput();
		}
	}
	
	public void draw(Graphics dbg) {
		// draw the background tile
		BufferedImage bgImage = ImagesLoader.getImage("ice_wall");

		for (int i = 0; i < GamePanel.PHEIGHT / ImagesLoader.ICON_SIZE; i++)
			for (int j = 0; j < GamePanel.PWIDTH / ImagesLoader.ICON_SIZE; j++)
				dbg.drawImage(bgImage, j * ImagesLoader.ICON_SIZE, i * ImagesLoader.ICON_SIZE, null);
					
		// draw the braver attr info panel
		braverAttr.draw(dbg);
		if(focusState == BRAVER || focusState == ONLOOKER || focusState == DIALOGUE || focusState == SHOP
				|| focusState == TELEPORTER) {
			
			mapLoader.draw(dbg);
			braver.draw(dbg);

			if(focusState == DIALOGUE) {
				
				// draw the tile as the background(3 * 12)
				for(int i = 0; i < (DIALOGUE_Y_END - DIALOGUE_Y_OFFSET) / ImagesLoader.ICON_SIZE;i ++)
					for(int j = 0; j < (DIALOGUE_X_END - DIALOGUE_X_OFFSET) / ImagesLoader.ICON_SIZE;j ++) {
						dbg.drawImage(ImagesLoader.getImage("dialogue_frame"), 
								PlayState.DIALOGUE_X_OFFSET + j * ImagesLoader.ICON_SIZE,
								PlayState.DIALOGUE_Y_OFFSET + i * ImagesLoader.ICON_SIZE, null);
					}
		
				
				// draw the string in dialogue events
				// if the text is too long, then split it
				dbg.setColor(Color.WHITE);
				dbg.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
				
				String line = Dialogue.getDialogue();
				FontMetrics fMetrics = dbg.getFontMetrics();
				int y = DIALOGUE_Y_OFFSET + 5 + ImagesLoader.ICON_SIZE / 2;
				if(fMetrics.stringWidth(line) < (DIALOGUE_X_END - DIALOGUE_X_OFFSET)) 
					dbg.drawString(line,
							DIALOGUE_X_OFFSET + ImagesLoader.ICON_SIZE / 2, 
							DIALOGUE_Y_OFFSET + 5 + ImagesLoader.ICON_SIZE / 2);
				else {
					String[] words = line.split(" ");
					String currentLine = "";
					for(int i = 0;i < words.length;i ++) {
						if(fMetrics.stringWidth(currentLine + " " + words[i]) 
								< (DIALOGUE_X_END - DIALOGUE_X_OFFSET - ImagesLoader.ICON_SIZE / 2)) {
							currentLine += (" " + words[i]);
						} else {
							dbg.drawString(currentLine,
									DIALOGUE_X_OFFSET + ImagesLoader.ICON_SIZE / 2, y);
							y += fMetrics.getHeight();
							currentLine = " " + words[i];
						}
					}
					if(currentLine.length() > 0) 
						dbg.drawString(currentLine,
								DIALOGUE_X_OFFSET + ImagesLoader.ICON_SIZE / 2, y);
				}
	
				// draw the ornamented frame surrounding the dialogue 
				//dbg.setColor(new Color(arg0, arg1, arg2));
				dbg.fillRect(DIALOGUE_X_OFFSET - 5, DIALOGUE_Y_OFFSET - 5, DIALOGUE_X_END - DIALOGUE_X_OFFSET, 5); // dialogue up
				dbg.fillRect(DIALOGUE_X_OFFSET - 5, DIALOGUE_Y_END, DIALOGUE_X_END - DIALOGUE_X_OFFSET + 5, 5);  // dialogue down
				dbg.fillRect(DIALOGUE_X_OFFSET - 5, DIALOGUE_Y_OFFSET, 5, DIALOGUE_Y_END - DIALOGUE_Y_OFFSET); // dialogue left
				dbg.fillRect(DIALOGUE_X_END - 5, DIALOGUE_Y_OFFSET - 5, 5, DIALOGUE_Y_END - DIALOGUE_Y_OFFSET + 5);  // dialogue right
				
				
				
				
			} else if(focusState == SHOP) {
				shopMenu.draw(dbg);
			} else if(focusState == TELEPORTER) {
				teleporter.draw(dbg);
			} else if(focusState == ONLOOKER) {
				if(!boom.isEndOfUse() && !boom.isFinish()) {
					boom.draw(dbg);
				
				}
			}
			
			
			
		} else if (focusState == MONSTER_DETECTOR) {
			monsterDetector.draw(dbg);
		}	
		
		dbg.setColor(new Color(204, 102, 0));			
		dbg.fillRect(GAME_X_OFFSET - 5, GAME_Y_OFFSET - 5, GAME_X_END - GAME_X_OFFSET, 5); // game up
		dbg.fillRect(GAME_X_OFFSET - 5, GAME_Y_END, GAME_X_END - GAME_X_OFFSET + 5, 5);  // game down
		dbg.fillRect(GAME_X_OFFSET - 5, GAME_Y_OFFSET, 5, GAME_Y_END - GAME_Y_OFFSET); // game left
		dbg.fillRect(GAME_X_END - 5, GAME_Y_OFFSET - 5, 5, GAME_Y_END - GAME_Y_OFFSET + 5);  // game right 
		
	}
	
	

}
