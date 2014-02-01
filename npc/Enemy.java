package npc;
import physics.*;
import core.*;
import main.Driver;

public class Enemy extends Character implements AI {
	
	public static final int MOVE_SPEED = 2;
	private static final int JUMPVELOCITY = 8;
	
	public Enemy(int x, int y, String type, String name) {
		super(x, y, type, name);
	}
	
	public void defaultBehavior(){
		if(Driver.getPlayer().getX() + Driver.getPlayer().getWidth()/2 > xPos + width/2)
			if(canMoveRight())
				xPos += MOVE_SPEED;
			else{
				System.out.println("swag");
				jump(JUMPVELOCITY);
			}
		else if(canMoveLeft())
			xPos -= MOVE_SPEED;
		else{
			jump(JUMPVELOCITY);
		}
	}

}
