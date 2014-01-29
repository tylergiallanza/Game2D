package main;

import gui.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import core.*;
import npc.*;
import npc.Character;

public class Driver {

	/**
	 * @TODO
	 *       - Add swag
	 *       - Water?
	 *       - Add world-specific player files
	 *       - Add new tools (weapon?)
	 *       - Add enemies
	 *       - Update swing animation
	 *       - Fix lag
	 *       - Add Don Quixote mode
	 *       - Add multiplayer
	 *       - Expand ore generation
	 *       - Save/Load NPCs
	 */

	/**
	 * @BUGFIXES
	 *           - Fix GUI image stretch error
	 *           - Fix treegen over caves
	 *           - Spawning should be on surface
	 *     		 - The lag is back- and this time on Windows...
	 *     		 - Fix teleportation error when player moves left into an NPC
	 */

	private static double x;
	private static double y;
	private static Player player;
	private static Friendly friendly;
	public static String gameState = "start";
	public static final int WORLDWIDTH = 3;
	public static int WIDTH = 1200;
	public static int HEIGHT = 900;
	public static final int TOOLWIDTH = 40;
	public static final int TOOLHEIGHT = 40;
	private static World map;
	public static final boolean fullscreen = false;
	private static ArrayList<Block> active = new ArrayList<Block>();
	public static final int INVENTORY_ICON_WIDTH = 40;

	public static void main(String[] args) throws IOException {

		initGL(); //must come before any other graphics stuff
		Block.init();
		Tool.init();
		while (!Display.isCloseRequested()) {
			try {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);  //wipe the screen        
				update();
			} catch (Exception e) {
				e.printStackTrace();
				end();
			}
		}
		Player.toFile(player,"player");
		map.save();
		end();

	}

	public static void initGL() {

		try {
			if (fullscreen)
				Display.setFullscreen(fullscreen);
			else
				Display.setDisplayMode(new DisplayMode(1200,800));
			Display.create();
			WIDTH = Display.getWidth();
			HEIGHT = Display.getHeight();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0,WIDTH,0,HEIGHT,1,-1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

	}

	private static void update() throws IOException {
		GL11.glPushMatrix();
		if (gameState.equals("start")) {
			Handler.update();
			Handler.drawMenu("Start");
			if (Handler.getChoice()!=null) {
				switch (Handler.getChoice().getID()) {
				case "continue":
					System.out.println("continue");
					gameState = "inGame";
					map = new World();
					map.load("NewMap0");
					player = Player.fromFile("player");
					changeX(-player.getX()+WIDTH/2);
					break;
				case "load":
					gameState = "loadMaps";
					break;
				case "new":
					System.out.println("new");
					player = new Player(WIDTH/2,HEIGHT/2,"PNG","person3.png");
					friendly = new Friendly(WIDTH/3,HEIGHT+500);
					map = new World("NewMap"+getMaps().length);
					gameState = "inGame";
					break;
				}
			}
		} else if (gameState.equals("loadMaps")) {
			Handler.drawMenu("Load");
			Handler.update();
			if (Handler.getChoice()!=null) {
				for (String s : getMaps()) {
					System.out.println(Handler.getChoice().getText()+","+s);
					if (Handler.getChoice().getText().equals(s)) {
						map = new World();
						map.load(s);
						player = Player.fromFile("player");
						gameState = "inGame";
					}
				}
			}
		} else {

			GL11.glTranslated(x,y,0);
			doGameLoop();
		}
		Display.sync(60);
		Display.update();
		GL11.glPopMatrix();
		GL11.glFinish();
	}

	private static void doGameLoop(){
		doLogic();
		doRender();
	}
	
	private static void doLogic(){
		updateActiveBlocks();
		player.update();
		friendly.update();
	}
	
	private static void doRender(){
		drawWorld(map);
		drawInventory(player.getInventory());
		drawCharacter(friendly);
		drawPlayer(player);
		player.updateWindow();
	}
	
	private static void end() {
		Display.destroy();
		System.exit(0);
	}

	private static boolean drawCharacter(Character obj) {
		try {
			obj.getTexture().bind();
			rect(obj.getX(),obj.getY(),obj.getWidth(),obj.getHeight(),1);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean drawPlayer(Player obj) {
		try {
			obj.getTexture().bind();
			if (obj.getDirection())
				rect(obj.getX(),obj.getY(),obj.getWidth(),obj.getHeight(),1);
			else
				rect(obj.getX()+obj.getWidth(),obj.getY(),-obj.getWidth(),
						obj.getHeight(),1);
			if (((Player) obj).getSelectedSlot() instanceof Tool)
				draw(((Tool) ((Player) obj).getSelectedSlot()),
						obj.getDirection());
			else
				draw(new Tile(((Player) obj).getSelectedSlot(),player.getX()
						+player.getWidth()/2,player.getY()+player.getHeight()/2));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean draw(GameObject obj) {
		try {
			obj.getTexture().bind();
			if (obj instanceof Block)
				rect(obj.getX(),
						obj.getY(),
						obj.getWidth(),
						obj.getHeight(),
						(double) ((Block) obj).breakTime
								/(Block.linkTime(((Block) obj).getType())));
			else
				rect(obj.getX(),obj.getY(),obj.getWidth(),obj.getHeight(),1);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	private static boolean draw(Tool obj, boolean right) {
		try {
			obj.getTexture().bind();
			if (right)
				tiltedrect(player.getX()+player.getWidth()-18+TOOLWIDTH,
						player.getY()+player.getHeight()/4,-TOOLWIDTH,
						TOOLHEIGHT,obj.getRotation());
			else
				tiltedrect(player.getX()+18-player.getWidth(),player.getY()
						+player.getHeight()/4,TOOLWIDTH,TOOLHEIGHT,
						obj.getRotation());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static World getWorld() {
		return map;
	}

	private static void rect(int x, int y, int width, int height,
			double transparency) {
		// draw the actual rectangle
		if (transparency<.15)
			transparency = .15;
		GL11.glColor4f(1f,1f,1f,(float) transparency);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(x,y);
		GL11.glTexCoord2f(1,1);
		GL11.glVertex2f(x+width,y);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex2f(x+width,y+height);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2f(x,y+height);
		GL11.glEnd();
	}

	private static void tiltedrect(int x, int y, int width, int height,
			int rotation) {
		// draw the actual rectangle
		GL11.glTranslatef(x+width/2,y,0.0f);
		GL11.glRotatef(rotation,0.0f,0.0f,1.0f);
		GL11.glTranslatef(-x-width/2,-y,0.0f);
		GL11.glColor4f(1f,1f,1f,1f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(x+width/2,y);
		GL11.glTexCoord2f(1,1);
		GL11.glVertex2f(x+width,y+height/2);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex2f(x+width/2,y+height);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2f(x,y+height/2);
		GL11.glEnd();
	}

	public static int getX() {
		return (int) x;
	}

	public static void changeX(int change) {
		x += change;
	}

	public static int getY() {
		return (int) y;
	}

	public static void setY(double yVal) {
		y = yVal;
	}

	public static void drawWorld(World world) {
		for (int i = ((player.getX()-WIDTH/2)/Block.WIDTH)-5; i<((player.getX()+WIDTH/2)/Block.WIDTH)+5; i++) {
			if (i<0)
				continue;
			if (i>=World.WIDTH) {
				break;
			}
			Block[] B = world.getLoaded()[i];
			for (Block b : B) {
				if (b!=null)
					draw(b);
			}
		}
		for (Tile t : world.getTileInfo()) {
			draw(t);
			t.updateGravity();
		}
	}

	public static Player getPlayer() {
		return player;
	}

	public static void addBlock(Block b) {
		active.add(b);
	}

	public static void updateActiveBlocks() {
		if (active.size()==0)
			return;
		for (int i = active.size()-1; i>=0; i--) {
			try {
				if (active.get(i).breakTime<Block.linkTime(active.get(i)
						.getType())) {
					if (!active.get(i).equals(player.getBlockAtMouse())
							||!Mouse.isButtonDown(0))
						active.get(i).breakTime += 2;
				} else
					active.remove(i);
			} catch (Exception e) {
				active.remove(i);
			}
		}
	}

	public static void drawInventory(Inventory inv) {
		int spot = (WIDTH/2)-((INVENTORY_ICON_WIDTH+5)*5);
		for (int i = 0; i<10; i++) {
			try {
				Item item = inv.getData()[i];
				new Tile(item,0,0).getTexture().bind();
				rect(spot-(int) x,HEIGHT-INVENTORY_ICON_WIDTH-10-(int) y,
						INVENTORY_ICON_WIDTH,INVENTORY_ICON_WIDTH,1);
			} catch (Exception e) {
			}
			spot += INVENTORY_ICON_WIDTH+5;
		}
	}

	public static String[] getMaps() {
		File[] files = new File("maps").listFiles();
		String[] s = new String[files.length];
		for (int i = 0; i<files.length; i++)
			s[i] = files[i].getName();
		return s;
	}

}