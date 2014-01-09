package core;

import org.newdawn.slick.opengl.Texture;

public class Tool extends Item{

	private static Texture axe1;
	private static Texture pick1;
	private Texture texture;
	private int rotation;
	
	public Tool() {
		type = "axe";
		stack = false;
		texture = axe1;
		rotation = 45;
		count = 1;
	}
	
	public Tool(String type){
		this.type = type;
		stack = false;
		texture = linkTexture(type);
		rotation = 45;
		count = 1;
	}
	
	public static void init(){
		axe1 = GameObject.loadTexture("PNG", "axe1.png");
		pick1 = GameObject.loadTexture("PNG", "pick1.png");
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public int getRotation(){
		return rotation;
	}
	
	private static Texture linkTexture(String input){
		if(input.equals("axe")) return axe1;
		return pick1;
	}
	
	public static boolean isTool(String input){
		return input.equals("pick") || input.equals("axe");
	}

}
