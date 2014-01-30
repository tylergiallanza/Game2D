package npc;

import java.util.ArrayList;

import physics.*;
import core.*;
import main.Driver;

public class Character extends GameObject implements Gravity, AI {

	public static final double GRAVITY = .4;
	public static double yVel;
	public static ArrayList<Character> NPCs = new ArrayList<Character>();
	private static int act;

	public Character(int x, int y, String type, String name) {
		super(x,y,type,name);
		width = 36;
		height = 72;
		yVel = 0;
		NPCs.add(this);
		act = 0;
	}

	public void update() {
		if (!Driver.getWorld().isPlayerOnMap(this)) {
			updateGravity();
		} else
			yVel = 0;
		while (isInGround()) {
			if (isHeadInGround()&&!areFeetInGround()) {
				yPos--;
			} else {
				yPos++;
			}
		}
		if (act%15==0)
			defaultBehavior();
		act++;

	}

	private boolean isInGround() {
		return (Driver.getWorld().isOnMap(xPos,yPos)
				||Driver.getWorld().isOnMap(xPos+width/2,yPos)||Driver
				.getWorld().isOnMap(xPos+width-1,yPos))
				||(Driver.getWorld().isOnMap(xPos,yPos+height/2)
						||Driver.getWorld().isOnMap(xPos+width/2,yPos+height/2)||Driver
						.getWorld().isOnMap(xPos+width-1,yPos+height/2))
				||(Driver.getWorld().isOnMap(xPos,yPos+height)
						||Driver.getWorld().isOnMap(xPos+width/2,yPos+height)||Driver
						.getWorld().isOnMap(xPos+width-1,yPos+height));
	}

	private boolean areFeetInGround() {
		return (Driver.getWorld().isOnMap(xPos,yPos)
				||Driver.getWorld().isOnMap(xPos+width/2,yPos)||Driver
				.getWorld().isOnMap(xPos+width-1,yPos));
	}

	private boolean isHeadInGround() {
		return (Driver.getWorld().isOnMap(xPos,yPos+height)
				||Driver.getWorld().isOnMap(xPos+width/2,yPos+height)||Driver
				.getWorld().isOnMap(xPos+width-1,yPos+height));
	}

	protected boolean canMoveRight() {
		return !(Driver.getWorld().isAnyButMeOnMap(
				this.getX()+this.getWidth()+1,this.getY(),this)
				||Driver.getWorld().isAnyButMeOnMap(
						this.getX()+this.getWidth()+2,
						this.getY()+this.getHeight()/2,this)||Driver.getWorld()
				.isAnyButMeOnMap(this.getX()+this.getWidth()+2,
						this.getY()+this.getHeight()-10,this));
	}

	protected boolean canMoveLeft() {
		return !(Driver.getWorld().isAnyButMeOnMap(this.getX()-1,this.getY(),
				this)
				||Driver.getWorld().isAnyButMeOnMap(this.getX()-1,
						this.getY()+this.getHeight()/2,this)||Driver.getWorld()
				.isAnyButMeOnMap(this.getX()-1,this.getY()+this.getHeight()-10,
						this));
	}

	protected boolean moveLeft() {
		try {
			setX(xPos-30);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected boolean moveRight() {
		try {
			setX(xPos+30);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

	public void updateGravity() {
		if (yVel<=0) {
			yPos += yVel;
		}
		yVel -= GRAVITY;
	}

	public void defaultBehavior() {

	}

}
