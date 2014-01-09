package core;

import physics.Gravity;
import main.Driver;

public class Tile extends GameObject implements Gravity{

	public static final int WIDTH = 15;
	public static final int HEIGHT = 15;
	private String type;

	public Tile(int x, int y, String type ) {
		this.type = type;
		texture = Block.linkTexture(type);
		xPos = x;
		yPos = y;
	}
	
	public Tile(Item input, int x, int y){
		this.type = input.getType();
		if(input instanceof Tool) texture = ((Tool) input).getTexture();
		else texture = Block.linkTexture(type);
		xPos = x;
		yPos = y;
	}
	
	public int getWidth(){
		return WIDTH;
	}
	
	public int getHeight(){
		return HEIGHT;
	}
	
	public void updateGravity(){
		if(!Driver.getWorld().isOnMap(this)) yPos -=2;
	}
	
	public String getType(){
		return type;
	}

}
