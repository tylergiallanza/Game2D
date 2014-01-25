package npc;

import physics.*;
import core.*;
import main.Driver;

public class Character extends GameObject implements Gravity {

	public static final double GRAVITY = .4;
	public static double yVel;

	public Character() {

	}

	public Character(int x, int y, String type, String name) {
		super(x,y,type,name);
		width = 36;
		height = 72;
		yVel = 0;
	}

	public void update() {
		if (!Driver.getWorld().isPlayerOnMap(this)) {
			updateGravity();
		} else
			yVel = 0;

	}

	public void updateGravity() {
		if (yVel<=0) {
			yPos += yVel;
		}
		yVel -= GRAVITY;
	}

}
