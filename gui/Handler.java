package gui;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureLoader;

import core.GameObject;
import main.Driver;

@SuppressWarnings("deprecation")
public class Handler {
	static boolean done = false;
	static boolean first = true;
	static Button clicked = null;
	static ArrayList<Button> buttons = new ArrayList<Button>();

	public static void update() throws IOException {
		checkMouse();
	}

	public static void checkMouse() {
		if (Mouse.isButtonDown(0)) {
			for (Button b : buttons) {
				if (b.isClicked(Mouse.getX(),Mouse.getY()))
					clicked = b;
				
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
		}
	}

	public static Button getChoice() {
		return clicked;
	}

}
