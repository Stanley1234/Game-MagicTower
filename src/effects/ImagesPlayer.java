package effects;

import java.awt.image.BufferedImage;

import collector.ImagesLoader;
import main.GameFrame;

public class ImagesPlayer {
	
	/*
	 * For imagePrefix, there are two cases:
	 * 1. if the imagesPlayer is used for a single character, then imagePrefix is
	 *    the prefix of that character
	 * 2. if the imagesPlayer is used for a collective purpose, then imagePrefix = "collective"
	 * 
	 * In case one, use getCurImage() to obtain current image of a sequence
	 * In case two, use getCurIndex() instead
	 * */
	
	public static String COLLECTIVE = "collective";
	
	
	private String imagePrefix = "null";
	private long totalTime = 0;  // ms

	private double seqDuration;
	private int showPeriod;   // period that each image is shown (ms)
	private int numImages; 
	private int curIndex = 0;
	private boolean isLooping = false;
	private boolean ticksIgnored = true;
	
	public ImagesPlayer() {}
	
	public void startSeq(String prefix, double seqDuration, boolean isLooping) { // for single
		// intialize
		this.totalTime = 0;
		
		this.imagePrefix = prefix;
		this.numImages = ImagesLoader.getNumOfImages(imagePrefix);
		this.seqDuration = seqDuration;
		
		ticksIgnored = false;
		this.isLooping = isLooping;
		this.showPeriod = (int) ((1000 * seqDuration) / numImages);
	}
	
	public void startCollectiveSeq(double seqDuration, boolean isLooping, int numImages) {
		// intialize
		this.totalTime = 0;
		
		this.imagePrefix = COLLECTIVE;
		this.seqDuration = seqDuration;
		this.numImages = numImages;
		
		ticksIgnored = false;
		this.isLooping = isLooping;
		this.showPeriod = (int) ((1000 * seqDuration) / numImages);
	}
	
	public void endSeq() {   // completely stop the animation at any time
		this.imagePrefix = "null";
		this.numImages = 0;
		ticksIgnored = true;
	}
	
	public void updateTick() {
		if(!ticksIgnored) {
			totalTime = (totalTime + GameFrame.PERIOD) % ((int)(1000 * seqDuration));
			
			// we should play the last strip image instead of skipping it
			int newCurIndex = (int)(totalTime / showPeriod);
			if(curIndex == numImages - 1 && newCurIndex == 0 && !isLooping) {
				curIndex = 0;
				ticksIgnored = true;
			} else {
				curIndex = (int)(totalTime / showPeriod);
			}
		}
	}
	
	public boolean isRunning() {
		return !ticksIgnored;
	}
	
	public boolean isAboutToStop() {
		// determines whether the imsplayer will stop animation right away
		// that is to say, in the next update, an animation will stop
		// this is useful when some attributes should update in the last anime updates
		// to avoid repeated updates
		return curIndex == ImagesLoader.getNumOfImages(imagePrefix) - 1 
				&& ((int)((totalTime + GameFrame.PERIOD) % ((int)(1000 * seqDuration)) / showPeriod) == 0);
	}
	
	public String getImagePrefix() {
		return imagePrefix;
	}
	
	public int getCurIndex() {   // mostly for collective
		return curIndex;
	}
	
	public BufferedImage getCurImage() {   // only for single
		if(!imagePrefix.equals(COLLECTIVE))
			return ImagesLoader.getImage(imagePrefix, curIndex);
		return null;
	}
	
	
}
