package info;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class ClipInfo implements LineListener {

	private final static String SOUND_DIR = "Sounds/";

	private String fileName, prefix;
	private Clip clip = null;
	
	private boolean isRunning = false; // fix the bug

	public ClipInfo(String fileName, String prefix) {
		this.fileName = fileName;
		this.prefix = prefix;
		loadClip();
	}

	private void loadClip() {
		try {
			
			AudioInputStream stream = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource(SOUND_DIR + fileName));
			AudioFormat format = stream.getFormat();

			// Convert ALAW/ULAW to PCM
			if (format.getEncoding() == Encoding.ALAW || format.getEncoding() == Encoding.ULAW) {
				AudioFormat newFormat = new AudioFormat(Encoding.PCM_SIGNED, format.getSampleRate(),
						format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2,
						format.getFrameRate(), true);

				stream = AudioSystem.getAudioInputStream(newFormat, stream);
				format = newFormat;
			}

			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.addLineListener(this);
			clip.open(stream);
			stream.close();

			//System.out.println("Successfully load " + prefix + " ,duration:" + clip.getMicrosecondLength() / 1000000.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play(float volOffset) {
		if(clip != null) {  
			//clip.stop();
			//clip.flush();
			//clip.setMicrosecondPosition(0);
			// The only way here is to reload the clip
			loadClip();
			
			//System.out.println("playing " + prefix + " " + volOffset);
			
			FloatControl gainCtrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			
			if(volOffset >= gainCtrl.getMaximum())
				gainCtrl.setValue(gainCtrl.getMaximum());
			else if(gainCtrl.getMinimum() < volOffset && volOffset < gainCtrl.getMaximum())
				gainCtrl.setValue(volOffset);
			else if(volOffset < gainCtrl.getMinimum())
				gainCtrl.setValue(gainCtrl.getMinimum());
			clip.start();
			isRunning = true;
		}
	}
	
	public void stop() {
		// stop and reset clip to its start
		if(clip != null) {
			clip.stop();
			clip.setFramePosition(0);
		}
	}
	
	public void pause() {
		if(clip != null) 
			clip.stop();
		
	}
	
	public void resume() {
		if(clip != null)
			clip.start();
	}
	
	public boolean isStop(){
		if(clip != null)
			return !isRunning;
		return true;
	}
	

	
	public String getPrefix() {
		return prefix;
	}

	@Override
	public void update(LineEvent arg0) {
		/*
		 * Called when the clip's line detects open, close, start, or stop
		 * events. The watcher (if one exists) is notified.
		 */
		System.out.println(arg0 + prefix);
		if(arg0.getType() == LineEvent.Type.STOP) {
			//arg0.getLine().close();
			isRunning = false;
		}
	}
}
