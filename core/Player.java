package core;

import main.Driver;
import physics.Gravity;
import physics.Explosion;

import org.lwjgl.input.*;

import java.io.File;
import java.io.FileWriter;

import java.util.Scanner;

public class Player extends GameObject implements Gravity {

	private static final int REACH = 70;
	private double yVel = 0;
	private static final int XVELOCITY = 3;
	private final double GRAVITY = .4;
	private final double JUMPVELOCITY = 8;
	private boolean canJump = false;
	private boolean direction;
	private Inventory inventory;
	private int inventorySlot = 0;

	public Player(int x, int y, String type, String name) {
		inventory = new Inventory();
		inventory.loadDefault();
		texture = loadTexture(type,name);
		xPos = x;
		yPos = y;
		width = 36;
		height = 72;
		direction = true;
	}

	public Player(int x, int y, String type, String name, Inventory i) {
		inventory = i;
		texture = loadTexture(type,name);
		xPos = x;
		yPos = y;
		width = 36;
		height = 72;
		direction = true;
	}

	public void update() {
		checkTiles();
		if (Mouse.isButtonDown(0)) {
			breakAtMouse(Mouse.getX(),Mouse.getY());
		} else if (getSelectedSlot() instanceof Tool
				&&((Tool) getSelectedSlot()).getRotation()!=Tool
						.linkMaxRotation(((Tool) getSelectedSlot()).getType()))
			((Tool) getSelectedSlot()).resetRotation();
		if (Mouse.isButtonDown(1)) {
			if (placeAtMouse(Mouse.getX(),Mouse.getY()))
				getSelectedSlot().remove();
		}
		checkMotion();
		checkSlot();
		if (!Driver.getWorld().isPlayerOnMap(this)) {
			updateGravity();
		} else {
			yVel = 0;
			canJump = true;
		}
		while (isInGround()) {
			if (isHeadInGround()&&!areFeetInGround()) {
				yPos--;
			} else {
				yPos++;
			}
		}
		updateWindow();
	}

	private void breakAtMouse(int x, int y) {
		if (Block.getBlock(x,y)==null)
			return;
		if (!canBreak(Block.getBlock(x,y)))
			return;
		if (x>Driver.WIDTH/2-REACH&&x<Driver.WIDTH/2+REACH+width) {
			if (y>Driver.HEIGHT/2-REACH&&y<Driver.HEIGHT/2+REACH+height) {
				if (getBlockAtMouse().breakTime==Block
						.linkTime(getBlockAtMouse().getType()))
					Driver.addBlock(Block.getBlock(Mouse.getX(),Mouse.getY()));
				((Tool) getSelectedSlot()).rotate(getDirection());
				Block.getBlock(x,y).breakTime--;
				if (Block.getBlock(x,y).breakTime<=15) {
					deleteAtMouse(x,y);
					((Tool) getSelectedSlot()).resetRotation();
				}
			}
		}
	}

	private boolean deleteAtMouse(int x, int y) {
		try {
			String t = Driver.getWorld().getLoaded()[(int) ((x-Driver.getX())/30)][(int) ((y-Driver
					.getY())/30)].getType();
			if (Driver.getWorld().getLoaded()[(int) ((x-Driver.getX())/30)][(int) ((y-Driver
					.getY())/30)].getType().equals("tnt"))
				Explosion.explode(((int) ((x-Driver.getX())/30)),
						((int) ((y-Driver.getY())/30)));
			if (!Driver.getWorld().getLoaded()[(int) ((x-Driver.getX())/30)][(int) ((y-Driver
					.getY())/30)].getType().equals("indestructable")) {
				Driver.getWorld().getLoaded()[(int) ((x-Driver.getX())/30)][(int) ((y-Driver
						.getY())/30)] = null;
				Driver.getWorld().getTileInfo()
						.add(new Tile(x-Driver.getX(),y-Driver.getY(),t));
			}
			if (t.equals("wood")
					&&Block.getBlock(x,y+Block.HEIGHT).getType().equals("wood"))
				deleteAtMouse(x,y+Block.HEIGHT);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private boolean placeAtMouse(int x, int y) {
		if (getSelectedSlot().getType().equals("empty")
				||getSelectedSlot() instanceof Tool)
			return false;
		if (Block.getBlock(x+30,y)!=null||Block.getBlock(x,y+30)!=null
				||Block.getBlock(x,y-30)!=null||Block.getBlock(x-30,y)!=null) {
			if (x>Driver.WIDTH/2-REACH&&x<Driver.WIDTH/2+REACH+width) {
				if (y>Driver.HEIGHT/2-REACH&&y<Driver.HEIGHT/2+REACH+height) {
					if (Driver.getWorld().getLoaded()[(int) ((x-Driver.getX())/30)][(int) ((y-Driver
							.getY())/30)]==null) {
						Driver.getWorld().getLoaded()[(int) ((x-Driver.getX())/30)][(int) ((y-Driver
								.getY())/30)] = new Block(x-Driver.getX()
								-(x-Driver.getX())%30,y-Driver.getY()
								-(y-Driver.getY())%30,getSelectedSlot()
								.getType());
						if (Driver.getPlayer().isInGround()) {
							Driver.getWorld().getLoaded()[(int) ((x-Driver
									.getX())/30)][(int) ((y-Driver.getY())/30)] = null;
							return false;
						} else
							return true;
					}
				}
			}
		}
		return false;
	}

	private void checkMotion() {
		if (Keyboard.isKeyDown(Keyboard.KEY_D)&&canMoveRight()) {
			direction = true;
			this.setX(this.getX()+XVELOCITY);
			Driver.changeX(-XVELOCITY);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)&&canMoveLeft()) {
			direction = false;
			this.setX(this.getX()-XVELOCITY);
			Driver.changeX(XVELOCITY);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)&&canJump) {
			canJump = false;
			yVel = JUMPVELOCITY;
			updateGravity();
		}
	}

	private boolean canMoveRight() {
		return !(Driver.getWorld().isOnMap(this.getX()+this.getWidth()+1,
				this.getY())
				||Driver.getWorld().isOnMap(this.getX()+this.getWidth()+2,
						this.getY()+this.getHeight()/2)||Driver.getWorld()
				.isOnMap(this.getX()+this.getWidth()+2,
						this.getY()+this.getHeight()-10));
	}

	private boolean canMoveLeft() {
		return !(Driver.getWorld().isOnMap(this.getX()-1,this.getY())
				||Driver.getWorld().isOnMap(this.getX()-1,
						this.getY()+this.getHeight()/2)||Driver.getWorld()
				.isOnMap(this.getX()-1,this.getY()+this.getHeight()-10));
	}

	private boolean canMoveUp(int vel) {
		return !(Driver.getWorld().isOnMap(this.getX()+1,
				this.getY()+this.height+vel)
				||Driver.getWorld().isOnMap(this.getX()+this.getWidth()-1,
						this.getY()+this.height+vel)||Driver.getWorld()
				.isOnMap(this.getX()+width/2,this.getY()+this.height+vel));
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

	public static File toFile(Player input, String fileName) {
		String write = "";
		write += input.getX()+" \n";
		write += input.getY()+" \n";
		for (Item i : input.inventory.getData()) {
			write += i.getType()+" "+i.count+" \n";
		}
		try {
			File file = new File(fileName);
			FileWriter writer = new FileWriter(file);
			writer.write(write);
			writer.close();
			return file;
		} catch (Exception e) {
			System.out.println("Failed to write player file");
			return null;
		}
	}

	public static Player fromFile(String fileName) {
		try {
			Scanner readFile = new Scanner(new File(fileName));
			int x = Integer.parseInt(readFile.nextLine().trim());
			int y = Integer.parseInt(readFile.nextLine().trim());
			Inventory i = new Inventory();
			for (int j = 0; j<Inventory.INVENTORYSIZE; j++) {
				String line = readFile.nextLine();
				Scanner readLine = new Scanner(line);
				String type = readLine.next();
				if (Tool.isTool(type))
					i.getData()[j] = new Tool(type);
				else
					i.getData()[j] = new Item(type,Integer.parseInt(readLine
							.next()));
				readLine.close();
			}
			readFile.close();
			System.out.println(i);
			return new Player(x,y,"PNG","person3.png",i);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("failed to read player file");
			return null;
		}
	}

	public void updateGravity() {
		if (yVel>0) {
			if (canMoveUp((int) yVel)) {
				yPos += yVel;
			} else {
				int i = 0;
				for (i = (int) yVel; i>=0; i--)
					if (canMoveUp(i))
						break;
				yPos += i-1;
				yVel = 0;
			}
		}
		if (yVel<=0) {
			canJump = false;
			yPos += yVel;
		}
		yVel -= GRAVITY;
	}

	/*private boolean isPlayer(int x, int y, int w, int h) {
		int xCenter = Driver.WIDTH/2-width/2;
		int yCenter = Driver.HEIGHT/2;
		if (x>xCenter&&x<xCenter+width&&y>yCenter&&y<yCenter+height)
			return true;
		if (x>xCenter+w&&x<xCenter+w+width&&y>yCenter&&y<yCenter+height)
			return true;
		if (x>xCenter+w&&x<xCenter+width+w&&y>yCenter+h&&y<yCenter+height+h)
			return true;
		if (x>xCenter&&x<xCenter+width&&y>yCenter+h&&y<yCenter+height+h)
			return true;
		return false;
	}*/

	private void updateWindow() {
		Driver.setY(Driver.getY()-(Driver.getY()+(yPos-Driver.HEIGHT/2)));
	}

	private void checkTiles() {
		for (int i = 0; i<Driver.getWorld().getTileInfo().size(); i++) {
			Tile t = Driver.getWorld().getTileInfo().get(i);
			if (Math.abs(t.xPos-width/2-xPos)<60
					&&Math.abs(t.yPos-height/2-yPos)<60) {
				if (inventory.add(new Item(t))) {
					Driver.getWorld().getTileInfo().remove(i);
				}
			}
		}
	}

	public Item getSelectedSlot() {
		return inventory.getData()[inventorySlot];
	}

	private boolean canBreak(Block b) {
		return b.getTool().equals(getSelectedSlot().getType());
	}

	private void checkSlot() {
		for (int i = 1; i<10; i++) {
			if (Keyboard.isKeyDown(i+1)) {
				inventorySlot = i-1;
				return;
			}
		}
	}

	public boolean getDirection() {
		return direction;
	}

	public Block getBlockAtMouse() {
		return Block.getBlock(Mouse.getX(),Mouse.getY());
	}

	public Inventory getInventory() {
		return inventory;
	}

}
