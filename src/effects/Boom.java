package effects;

import java.awt.Graphics;
import java.awt.Point;

import collector.ClipLoader;
import collector.ImagesLoader;
import gamestate.PlayState;
import main.GameFrame;

public class Boom {
	private static final double BOOM_TIME = 0.5; // frequency(s)
	public static final double UPDATE_TIMES_PER_OBJ = BOOM_TIME * 1000.0 / GameFrame.PERIOD;   // how many animation loop is gone through before on boom finishes
	private int x1, y1, x2, y2;
	private int boomTimes = 0;
	private int curObject = 0; // 0 and 1. if curObject equals 1, then the boom
								// happens at (x1, y1)
	private boolean endOfUse = true;   // flag whether the monster is removed
	int counter = 0;

	private ImagesPlayer imsplayer = new ImagesPlayer();

	public Boom() {}
	
	public void init(int x1, int y1, int x2, int y2, int boomTimes) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.boomTimes = boomTimes;
		this.imsplayer.startSeq("boom", BOOM_TIME, false);
		endOfUse = false;
		ClipLoader.play("fight", 66);
	}
	
	public int getBoomTimes() {
		return boomTimes;
	}
	
	public Point getObj1Pos() {   
		// temporary, all the monster will be hit by braver first
		// ob1 represents monster
		return new Point(x1, y1);
	}
	
	public void dispose() {
		endOfUse = true;
	}
	
	public boolean isEndOfUse() {
		return endOfUse;
	}

	public boolean isFinish() { 
		return boomTimes <= 0 && !endOfUse;
	}
	
	public boolean isLastAnime() {
		return boomTimes == 1 && imsplayer.isAboutToStop();
	}

	public void updateSeq() {
		if (boomTimes > 0) {
			counter++;
			imsplayer.updateTick();

			if (!imsplayer.isRunning()) 
				boomTimes--;
			if (!imsplayer.isRunning() && boomTimes > 0) {
				ClipLoader.play("fight", 66);
				imsplayer.startSeq("boom", BOOM_TIME, false);

				if (curObject == 0)
					curObject = 1;
				else
					curObject = 0;
			}
		}

	}

	public void draw(Graphics dbg) {
		if (curObject == 0)
			dbg.drawImage(imsplayer.getCurImage(), PlayState.GAME_X_OFFSET + x1 * ImagesLoader.ICON_SIZE,
					PlayState.GAME_Y_OFFSET + y1 * ImagesLoader.ICON_SIZE, null);

		else
			dbg.drawImage(imsplayer.getCurImage(), PlayState.GAME_X_OFFSET + x2 * ImagesLoader.ICON_SIZE,
					PlayState.GAME_Y_OFFSET + y2 * ImagesLoader.ICON_SIZE, null);

	}

}
