package npc;

import physics.*;
import core.*;
import main.Driver;

public class Friendly extends Character {

	public Friendly(int x, int y, String type, String name) {
		super(x,y,type,name);
	}

	public Friendly(int x, int y) {
		super(x,y,"PNG","friendly.png");
	}

	public void defaultBehavior() {
		Double rand = Math.random();
		if (rand<.4) {
			if (Math.random()<.5) {
				moveRandomLeft();
			} else {
				moveRandomRight();
			}
		} else if (rand<.6) {

		}

	}

	protected void moveRandomLeft() {
		int rand = (int) (Math.random()*5);
		for (int i = 0; i<rand; i++) {
			if (!canMoveLeft())
				continue;
			moveLeft();
		}
	}

	protected void moveRandomRight() {
		int rand = (int) (Math.random()*5);
		for (int i = 0; i<rand; i++) {
			if (!canMoveRight())
				continue;
			moveRight();
		}
	}

}
