package core;

import main.Driver;

import org.newdawn.slick.opengl.Texture;


public class Block extends GameObject {

	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public static final String[] BLOCK_TYPES = { "tnt", "crate", "wood" };
	public static final int[] FREQUENCY = { 3, 6, 91 };
	private String type;
	private String breakType;
	public int breakTime;
	private static Texture stone;
	private static Texture wood;
	private static Texture indestructable;
	private static Texture grass;
	private static Texture crate;
	private static Texture tnt;
	public static final int WOODBREAKTIME = 200;
	public static final int STONEBREAKTIME = 100;
	public static final int GRASSBREAKTIME = 40;

	public static void init(){
		try{
			indestructable = loadTexture("PNG", "indestructable.png");
			wood = loadTexture("PNG", "wood.png");
			stone = loadTexture("PNG", "stone.png");
			grass = loadTexture("PNG", "grass.png");
			crate = loadTexture("PNG", "crate.png");
			tnt = loadTexture("PNG", "tnt.png");
		}catch(Exception e){System.out.println("Failed to init block textures");}
	}

	public Block(int x, int y) {
		xPos = x;
		yPos = y;
		String key = genRandBlock();
		texture = loadTexture(type,key+".png");
		this.type = key;
		breakTime = linkTime(key);
	}
	
	public Block(int x, int y, String key) {
		xPos = x;
		yPos = y;
		texture = linkTexture(key);
		breakType = linkTool(key);
		this.type = key;
		breakTime = linkTime(key);
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public static int getHeightAtX(Block[][] arr, int x) {
		//int r = 0;
		Block[] B = arr[x];
		for (int i = 0; i<B.length; i++) {
			if (B[i]==null)
				return i*WIDTH;
		}
		return -1;
	}

	public String getType() {
		return type;
	}
	
	public String getTool(){
		return breakType;
	}

	public static int getLastHeight(Block[][] arr) {
		return getHeightAtX(arr,arr.length - 1);
	}

	private String genRandBlock() {
		int rand = (int) (Math.random()*100)+1;
		for (int i = 0; i<FREQUENCY.length; i++) {
			if (i==0) {
				if (rand<=FREQUENCY[i])
					return BLOCK_TYPES[i];
			} else if (i<FREQUENCY.length-1) {
				if (rand>sumPrev(FREQUENCY,i)&&rand<=sumPrev(FREQUENCY,i)+FREQUENCY[i])
					return BLOCK_TYPES[i];
			} else {
					return BLOCK_TYPES[i];
			}
		}
		return null;
	}
	
	private int sumPrev(int[] arr, int index) {
		int count = 0;
		for(int i=0;i<index;i++)
			count+=arr[i];
		return count;
	}
	
	public static Block getBlock(int x, int y){
		return Driver.getWorld().getLoaded()[(int) ((x-Driver.getX())/30)][(int) ((y-Driver.getY())/30)];
	}
	
	public static Texture linkTexture(String type){
		if(type.equals("stone")) return stone;
		if(type.equals("wood")) return wood;
		if(type.equals("indestructable")) return indestructable;
		if(type.equals("grass")) return grass;
		if(type.equals("crate")) return crate;
		if(type.equals("tnt")) return tnt;
		return null;
	}
	
	private static String linkTool(String type){
		if(type.equals("wood")) return "axe";
		if(type.equals("stone") || type.equals("grass")) return "pick";
		return "any";
	}
	
	public static int linkTime(String type){
		if(type.equals("wood")) return WOODBREAKTIME;
		if(type.equals("stone")) return STONEBREAKTIME;
		if(type.equals("grass")) return GRASSBREAKTIME;
		return 1;
	}

	public boolean hasAnimation() {
		if(type.equals("stone")) return true;
		return false;
	}

	public void breakAnimation() {
		int finalTime = linkTime(getType());
		if(breakTime<finalTime*0.2) texture = loadTexture("PNG",getType()+"5.png");
		else if(breakTime<finalTime*0.4) texture = loadTexture("PNG",getType()+"4.png");
		else if(breakTime<finalTime*0.6) texture = loadTexture("PNG",getType()+"3.png");
		else if(breakTime<finalTime*0.8) texture = loadTexture("PNG",getType()+"2.png");
		else if(breakTime<finalTime)loadTexture("PNG",getType()+"1.png");
		
	}

}