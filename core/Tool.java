package core;

import org.newdawn.slick.opengl.Texture;

public class Tool extends Item{

	private static Texture axe1;
	private static Texture pick1;
	private static Texture sword1;
	private Texture texture;
	private int rotation;
	private int maxRotation;
	public static final int DEFAULT_ROTATION = 30;
	public static final int PICK_ROTATION = 90;
	public static final int AXE_ROTATION = 90;
	public static final int SWORD_ROTATION = 90;
	public static final int SWING_VELOCITY = 4;
	public static final int SWORD_SWING_VELOCITY = 5;
	
	public Tool() {
		type = "axe";
		stack = false;
		texture = axe1;
		rotation = DEFAULT_ROTATION;
		maxRotation = linkMaxRotation(type);
		count = 1;
	}
	
	public Tool(String type){
		this.type = type;
		stack = false;
		texture = linkTexture(type);
		rotation = DEFAULT_ROTATION;
		count = 1;
		maxRotation = linkMaxRotation(type);
	}
	
	public static void init(){
		axe1 = GameObject.loadTexture("PNG", "axe1.png");
		pick1 = GameObject.loadTexture("PNG", "pick1.png");
		sword1 = GameObject.loadTexture("PNG", "sword1.png");
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public int getRotation(){
		return rotation;
	}
	
	private static Texture linkTexture(String input){
		if(input.equals("axe")) return axe1;
		if(input.equals("sword")) return sword1;
		return pick1;
	}
	
	public static boolean isTool(String input){
		return input.equals("pick") || input.equals("axe") || input.equals("sword");
	}
	
	public boolean rotate(boolean right){
		rotation += (type.equals("sword"))? SWORD_SWING_VELOCITY : SWING_VELOCITY;
		if(Math.abs(rotation) > maxRotation){
			rotation = DEFAULT_ROTATION;
			return true;
		}
		return false;
	}
	
	public void resetRotation(){
		rotation = DEFAULT_ROTATION;
	}
	
	public static int linkMaxRotation(String type){
		if(type.equals("pick")) return PICK_ROTATION;
		if(type.equals("sword")) return SWORD_ROTATION;
		return AXE_ROTATION;
	}

}
