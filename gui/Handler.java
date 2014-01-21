package gui;


import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import main.Driver;

public class Handler {
	static boolean done = false;
	static boolean first = true;
	static Button clicked = null;
	static boolean hasLoadedMaps = false;
	static ArrayList<Button> buttons = new ArrayList<Button>();
	static Button[] worldButtons;
	private static long lastClick = 0;

	public static void update() throws IOException {
		checkMouse();
	}

	public static void checkMouse() {
		if (Mouse.isButtonDown(0) && System.currentTimeMillis() - lastClick > 100) {
			lastClick = System.currentTimeMillis();
			if(Driver.gameState.equals("start")){
				for (Button b : buttons) {
					if (b.isClicked(Mouse.getX(),Mouse.getY()))
						clicked = b;

				}
			} else {
				for(Button b : worldButtons){
					if(b.isClicked(Mouse.getX(), Mouse.getY()))
						clicked = b;
				}
			}
		}
	}

	public static void register(Button b) {
		buttons.add(b);
	}

	public static void drawMenu(String type) throws IOException {
		switch (type) {
		case "Start":
			new Button(Driver.WIDTH/2-250,(Driver.HEIGHT/10)*6+50,500,100,
					"Continue Game","continue",first).draw();
			new Button(Driver.WIDTH/2-250,(Driver.HEIGHT/10)*4+50,500,100,
					"Load...","load",first).draw();
			new Button(Driver.WIDTH/2-250,(Driver.HEIGHT/10)*2+50,500,100,
					"New Game","new",first).draw();
			if (first)
				first = false;
			break;
		case "Load":
			if(!hasLoadedMaps){
				String[] maps = Driver.getMaps();
				int numMaps = maps.length;
				worldButtons = new Button[numMaps];
				int buttonY = Driver.HEIGHT/2 - 55 * numMaps;
				int buttonX = Driver.WIDTH/2 - 250;
				for(int i = 0; i < numMaps; i++){
					worldButtons[i] = new Button(buttonX, buttonY, 500, 100, maps[i], "MAP_" + maps[i], false);
					buttonY += 110;
				}
				hasLoadedMaps = true;
			} else {
				for(Button b : worldButtons)
					b.draw();
			}
			break;
		}
	}

	public static Button getChoice() {
		return clicked;
	}

}
