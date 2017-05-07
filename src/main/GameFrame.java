package main;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

import collector.ImagesLoader;
import manager.Content;


@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	public static int FPS = 20;  // 
	public static int PERIOD = 50;   // ms
	
	public GameFrame() {
		super("Magic Tower");
		
		// intialize global static class
		ImagesLoader.init(); 
		Content.init();
		// intialize game panel
		initGUI();
		// intialize location
		initLocation();
	
		
		this.setResizable(false);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void initGUI() {
		GamePanel gamePanel = new GamePanel();
		
		Container c = getContentPane();
		c.add(gamePanel);
		//debugFrame debugFrame = new debugFrame();
		//c.add(debugFrame);
	}
	
	private void initLocation() {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Rectangle screenRect = gc.getBounds();
		this.setLocation(screenRect.width / 4, screenRect.height / 12);
	}

	public static void main(String[] args) {
		
		new GameFrame();
	
	}
}
