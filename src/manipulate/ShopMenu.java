package manipulate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;

import collector.ClipLoader;
import collector.ImagesLoader;
import gamestate.PlayState;
import manager.Keys;
import manager.Shopping;

public class ShopMenu {

	// for the frame of menu
	private static final int MENU_X_OFFSET = PlayState.GAME_X_OFFSET
			+ (PlayState.GAME_X_END - PlayState.GAME_X_OFFSET) / 13 * 3;
	private static final int MENU_X_END = PlayState.GAME_X_OFFSET
			+ (PlayState.GAME_X_END - PlayState.GAME_X_OFFSET) / 13 * 10;
	private static final int MENU_Y_OFFSET = PlayState.GAME_Y_OFFSET
			+ (PlayState.GAME_Y_END - PlayState.GAME_Y_OFFSET) / 13 * 2;
	private static final int MENU_Y_END = PlayState.GAME_Y_OFFSET
			+ (PlayState.GAME_Y_END - PlayState.GAME_Y_OFFSET) / 13 * 11;

	// for the location of string
	private static final int INFO_X_OFFSET = MENU_X_OFFSET + (MENU_X_END - MENU_X_OFFSET) / 30;
	//private static final int INFO_X_END = MENU_X_END - 5;
	private static final int INFO_Y_OFFSET = MENU_Y_OFFSET + (MENU_Y_END - MENU_Y_OFFSET) / 3;
	//private static final int INFO_Y_END = MENU_Y_END - 10;

	private Braver braver;
	private PlayState ps;

	private int curIndex = 0;

	public ShopMenu(Braver braver, PlayState playState) {
		this.braver = braver;
		this.ps = playState;
	}

	private boolean isNumber(String str) {
		for (int i = 0; i < str.length(); i++)
			if (!('0' <= str.charAt(i) && str.charAt(i) <= '9'))
				return false;
		return true;

	}

	public void handleInput() {
		if (Keys.getCurCode() == KeyEvent.VK_DOWN) {
			curIndex = (curIndex + 1) % Shopping.getProductionInfo().size();
			ClipLoader.play("shift_selected_item", ClipLoader.FFF);
			
		} else if (Keys.getCurCode() == KeyEvent.VK_UP) {
			curIndex--;
			if (curIndex < 0)
				curIndex += Shopping.getProductionInfo().size();
			ClipLoader.play("shift_selected_item", ClipLoader.FFF);
			
		} else if (Keys.getCurCode() == KeyEvent.VK_ENTER) {

			
			if (Shopping.getProductionInfo().get(curIndex).toLowerCase().equals("quit shop")) {
				ClipLoader.play("quit", ClipLoader.FFF); // Stanley Arnav
				ps.setFocusState(PlayState.BRAVER);
				curIndex = 0;
				return;
			} 
				

			// analyze the product by extracting numbers from the production
			// info
			StringTokenizer stk = new StringTokenizer(Shopping.getProductionInfo().get(curIndex));

			// format: costs [price] to add [effects] [related-attr]
			// get the price
			String piece;
			while (!isNumber((piece = stk.nextToken()))) {}
			int price = Integer.parseInt(piece);

			// get cost type
			String type = stk.nextToken().toLowerCase();

			// get the effects
			while (!isNumber((piece = stk.nextToken()))) {}
			int effects = Integer.parseInt(piece);

			String relatedAttr = stk.nextToken().toLowerCase();

			if (type.equals("exp")) {
				
				// have enough money
				if (braver.getExp() >= price) {
					braver.setExp(braver.getExp() - price);
					if (relatedAttr.equals("level")) {
						braver.setHp(braver.getHp() + 1000 * effects);
						braver.setLevel(braver.getLevel() + effects);
						braver.setAttack(braver.getAttack() + 5 * effects);
						braver.setDefend(braver.getDefend() + 5 * effects);
					} else if (relatedAttr.equals("hp"))
						braver.setHp(braver.getHp() + effects);
					else if (relatedAttr.equals("attack"))
						braver.setAttack(braver.getAttack() + effects);
					else if (relatedAttr.equals("defend"))
						braver.setDefend(braver.getDefend() + effects);
					else if (relatedAttr.equals("redkey"))
						braver.setRedKeys(braver.getRedKeys() + effects);
					else if (relatedAttr.equals("bluekey"))
						braver.setBlueKeys(braver.getBlueKeys() + effects);
					else if (relatedAttr.equals("yellowkey"))
						braver.setYellowKeys(braver.getYellowKeys() + effects);
					
					ClipLoader.play("confirm", ClipLoader.FFF); // Stanley Arnav
				} else {
					ClipLoader.play("no_money", ClipLoader.FFF); 
				}
				
				
				
			} else if (type.equals("coins")) {

				if (braver.getCoins() >= price) {
					braver.setCoins(braver.getCoins() - price);
					if (relatedAttr.equals("level")) {
						braver.setHp(braver.getHp() + 1000 * effects );
						braver.setAttack(braver.getAttack() + 5 * effects);
						braver.setDefend(braver.getDefend() + 5 * effects);
						braver.setLevel(braver.getLevel() + effects);
					} else if (relatedAttr.equals("hp"))
						braver.setHp(braver.getHp() + effects);
					else if (relatedAttr.equals("attack"))
						braver.setAttack(braver.getAttack() + effects);
					else if (relatedAttr.equals("defend"))
						braver.setDefend(braver.getDefend() + effects);
					else if (relatedAttr.equals("redkey"))
						braver.setRedKeys(braver.getRedKeys() + effects);
					else if (relatedAttr.equals("bluekey"))
						braver.setBlueKeys(braver.getBlueKeys() + effects);
					else if (relatedAttr.equals("yellowkey"))
						braver.setYellowKeys(braver.getYellowKeys() + effects);
					
					ClipLoader.play("confirm", ClipLoader.FFF); 
				} else {
					ClipLoader.play("no_money", ClipLoader.FFF); 
				}
			}
		}

	}

	public void draw(Graphics dbg) {

		// background tile
		for (int i = MENU_Y_OFFSET / ImagesLoader.ICON_SIZE; i < MENU_Y_END / ImagesLoader.ICON_SIZE; i++)
			for (int j = MENU_X_OFFSET / ImagesLoader.ICON_SIZE; j < MENU_X_END / ImagesLoader.ICON_SIZE; j++) {
				dbg.drawImage(ImagesLoader.getImage("road"), j * ImagesLoader.ICON_SIZE, i * ImagesLoader.ICON_SIZE,
						null);
			}
		// item info
		// normally there are three or four items in a shop
		dbg.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		int line_height = 40;

		for (int i = 0; i < Shopping.getProductionInfo().size(); i++) {
			String proInfo = Shopping.getProductionInfo().get(i);

			if (curIndex == i) // highlight
				dbg.setColor(Color.GREEN);
			else
				dbg.setColor(Color.WHITE);
			dbg.drawString(proInfo, INFO_X_OFFSET, i * line_height + INFO_Y_OFFSET);
		}

		// decorated frame
		dbg.setColor(new Color(204, 102, 0));
		dbg.fillRect(MENU_X_OFFSET - 5, MENU_Y_OFFSET - 5, MENU_X_END - MENU_X_OFFSET, 5); // menu
																							// up
		dbg.fillRect(MENU_X_OFFSET - 5, MENU_Y_END, MENU_X_END - MENU_X_OFFSET + 5, 5); // menu
																						// down
																						// dbg.fillRect(ATTR_X_OFFSET
																						// -
																						// 5,
																						// ATTR_Y_OFFSET,
																						// 5,
																						// ATTR_Y_END
																						// -
																						// ATTR_Y_OFFSET);
																						// //
																						// attr
																						// left
		dbg.fillRect(MENU_X_END - 5, MENU_Y_OFFSET - 5, 5, MENU_Y_END - MENU_Y_OFFSET + 5); // menu
																							// right
		dbg.fillRect(MENU_X_OFFSET - 5, MENU_Y_OFFSET, 5, MENU_Y_END - MENU_Y_OFFSET); // attr
																						// left
	}

}
