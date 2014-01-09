package core;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import main.Driver;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import physics.Explosion;
import physics.Gravity;

public class Player
  extends GameObject
  implements Gravity
{
  private static final int REACH = 70;
  private double yVel = 0.0D;
  private static final int XVELOCITY = 3;
  private final double GRAVITY = 0.4D;
  private final double JUMPVELOCITY = 7.0D;
  private boolean canJump = false;
  private boolean direction;
  private Inventory inventory;
  private int inventorySlot = 0;
  
  public Player(int x, int y, String type, String name)
  {
    this.inventory = new Inventory();
    this.inventory.loadDefault();
    this.texture = loadTexture(type, name);
    this.xPos = x;
    this.yPos = y;
    this.width = 36;
    this.height = 72;
    this.direction = true;
  }
  
  public Player(int x, int y, String type, String name, Inventory i)
  {
    this.inventory = i;
    this.texture = loadTexture(type, name);
    this.xPos = x;
    this.yPos = y;
    this.width = 36;
    this.height = 72;
    this.direction = true;
  }
  
  public void update()
  {
    checkTiles();
    if (Mouse.isButtonDown(0)) {
      breakAtMouse(Mouse.getX(), Mouse.getY());
    }
    if ((Mouse.isButtonDown(1)) && 
      (placeAtMouse(Mouse.getX(), Mouse.getY()))) {
      getSelectedSlot().remove();
    }
    checkMotion();
    checkSlot();
    if (!Driver.getWorld().isPlayerOnMap(this))
    {
      updateGravity();
    }
    else
    {
      this.yVel = 0.0D;
      this.canJump = true;
    }
    updateWindow();
  }
  
  private void breakAtMouse(int x, int y)
  {
    if (Block.getBlock(x, y) == null) {
      return;
    }
    if (!canBreak(Block.getBlock(x, y))) {
      return;
    }
    if ((x > Driver.WIDTH / 2 - 70) && (x < Driver.WIDTH / 2 + 70 + this.width) && 
      (y > Driver.HEIGHT / 2 - 70) && (y < Driver.HEIGHT / 2 + 70 + this.height))
    {
      Block.getBlock(x, y).breakTime -= 1;
      if (Block.getBlock(x, y).breakTime <= 0) {
        deleteAtMouse(x, y);
      }
    }
  }
  
  private boolean deleteAtMouse(int x, int y)
  {
    try
    {
      if (Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)].getType().equals("tnt")) {
        Explosion.explode((x - Driver.getX()) / 30, (y - Driver.getY()) / 30);
      }
      if (!Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)].getType().equals("indestructable"))
      {
        String t = Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)].getType();
        Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)] = null;
        Driver.getWorld().getTileInfo().add(new Tile(x - Driver.getX(), y - Driver.getY(), t));
      }
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }
  
  private boolean placeAtMouse(int x, int y)
  {
    if ((getSelectedSlot().getType().equals("empty")) || ((getSelectedSlot() instanceof Tool))) {
      return false;
    }
    if (((Block.getBlock(x + 30, y) != null) || (Block.getBlock(x, y + 30) != null) || (Block.getBlock(x, y - 30) != null) || (Block.getBlock(x - 30, y) != null)) && 
      (x > Driver.WIDTH / 2 - 70) && (x < Driver.WIDTH / 2 + 70 + this.width) && 
      (y > Driver.HEIGHT / 2 - 70) && (y < Driver.HEIGHT / 2 + 70 + this.height) && 
      (Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)] == null))
    {
      Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)] = new Block(x - Driver.getX() - (x - Driver.getX()) % 30, y - Driver.getY() - (y - Driver.getY()) % 30, getSelectedSlot().getType());
      if (Driver.getPlayer().isInGround())
      {
        Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)] = null;
        return false;
      }
      return true;
    }
    return false;
  }
  
  private void checkMotion()
  {
    if ((Keyboard.isKeyDown(32)) && (canMoveRight()))
    {
      this.direction = true;
      setX(getX() + 3);
      Driver.changeX(-3);
    }
    if ((Keyboard.isKeyDown(30)) && (canMoveLeft()))
    {
      this.direction = false;
      setX(getX() - 3);
      Driver.changeX(3);
    }
    if ((Keyboard.isKeyDown(57)) && (this.canJump))
    {
      this.canJump = false;
      this.yVel = 7.0D;
      updateGravity();
    }
  }
  
  private boolean canMoveRight()
  {
    return (!Driver.getWorld().isOnMap(getX() + getWidth() + 1, getY())) && (!Driver.getWorld().isOnMap(getX() + getWidth() + 2, getY() + getHeight() / 2)) && (!Driver.getWorld().isOnMap(getX() + getWidth() + 2, getY() + getHeight() - 10));
  }
  
  private boolean canMoveLeft()
  {
    return (!Driver.getWorld().isOnMap(getX() - 1, getY())) && (!Driver.getWorld().isOnMap(getX() - 1, getY() + getHeight() / 2)) && (!Driver.getWorld().isOnMap(getX() - 1, getY() + getHeight() - 10));
  }
  
  private boolean canMoveUp(int vel)
  {
    return (!Driver.getWorld().isOnMap(getX() + 1, getY() - this.height - vel)) && (!Driver.getWorld().isOnMap(getX() + getWidth() - 1, getY() - this.height - vel)) && (!Driver.getWorld().isOnMap(getX() + this.width / 2, getY() - this.height - vel));
  }
  
  private boolean isInGround()
  {
    boolean l = (Driver.getWorld().isOnMap(this.xPos, this.yPos)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos)) || 
      (Driver.getWorld().isOnMap(this.xPos, this.yPos + this.height / 2)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos + this.height / 2)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos + this.height / 2)) || 
      (Driver.getWorld().isOnMap(this.xPos, this.yPos + this.height)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos + this.height)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos + this.height));
    System.out.println(l);
    System.out.println(isHeadInGround());
    System.out.println("F" + areFeetInGround());
    return l;
  }
  
  private boolean areFeetInGround()
  {
    return (Driver.getWorld().isOnMap(this.xPos, this.yPos)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos));
  }
  
  private boolean isHeadInGround()
  {
    return (Driver.getWorld().isOnMap(this.xPos, this.yPos + this.height)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos + this.height)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos + this.height));
  }
  
  public static File toFile(Player input, String fileName)
  {
    String write = "";
    write = write + input.getX() + " \n";
    write = write + input.getY() + " \n";
    for (Item i : input.inventory.getData()) {
      write = write + i.getType() + " " + i.count + " \n";
    }
    try
    {
      File file = new File(fileName);
      FileWriter writer = new FileWriter(file);
      writer.write(write);
      writer.close();
      return file;
    }
    catch (Exception e)
    {
      System.out.println("Failed to write player file");
    }
    return null;
  }
  
  public static Player fromFile(String fileName)
  {
    try
    {
      Scanner readFile = new Scanner(new File(fileName));
      int x = Integer.parseInt(readFile.nextLine().trim());
      int y = Integer.parseInt(readFile.nextLine().trim());
      Inventory i = new Inventory();
      for (int j = 0; j < 20; j++)
      {
        String line = readFile.nextLine();
        Scanner readLine = new Scanner(line);
        String type = readLine.next();
        if (Tool.isTool(type)) {
          i.getData()[j] = new Tool(type);
        } else {
          i.getData()[j] = new Item(type, Integer.parseInt(readLine.next()));
        }
      }
      System.out.println(i);
      return new Player(x, y, "PNG", "person3.png", i);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("failed to read player file");
    }
    return null;
  }
  
  public void updateGravity()
  {
    if (this.yVel > 0.0D) {
      if (canMoveUp((int)this.yVel))
      {
        this.yPos = ((int)(this.yPos - this.yVel));
      }
      else
      {
        int i = 0;
        for (i = (int)this.yVel; i >= 0; i--) {
          if (canMoveUp(i)) {
            break;
          }
        }
        this.yPos -= i + 1;
        this.yVel = 0.0D;
      }
    }
    if (this.yVel <= 0.0D)
    {
      this.canJump = false;
      this.yPos = ((int)(this.yPos + this.yVel));
    }
    this.yVel += 0.4D;
  }
  
  private boolean isPlayer(int x, int y, int w, int h)
  {
    int xCenter = Driver.WIDTH / 2 - this.width / 2;
    int yCenter = Driver.HEIGHT / 2;
    if ((x > xCenter) && (x < xCenter + this.width) && (y > yCenter) && (y < yCenter + this.height)) {
      return true;
    }
    if ((x > xCenter + w) && (x < xCenter + w + this.width) && (y > yCenter) && (y < yCenter + this.height)) {
      return true;
    }
    if ((x > xCenter + w) && (x < xCenter + this.width + w) && (y > yCenter + h) && (y < yCenter + this.height + h)) {
      return true;
    }
    if ((x > xCenter) && (x < xCenter + this.width) && (y > yCenter + h) && (y < yCenter + this.height + h)) {
      return true;
    }
    return false;
  }
  
  private void updateWindow()
  {
    Driver.setY(Driver.getY() - (Driver.getY() + (this.yPos - Driver.HEIGHT / 2)));
  }
  
  private void checkTiles()
  {
    for (int i = 0; i < Driver.getWorld().getTileInfo().size(); i++)
    {
      Tile t = (Tile)Driver.getWorld().getTileInfo().get(i);
      if ((Math.abs(t.xPos - this.xPos) < 40) && (Math.abs(t.yPos - this.yPos) < 40) && 
        (this.inventory.add(new Item(t)))) {
        Driver.getWorld().getTileInfo().remove(i);
      }
    }
  }
  
  public Item getSelectedSlot()
  {
    return this.inventory.getData()[this.inventorySlot];
  }
  
  private boolean canBreak(Block b)
  {
    return b.getTool().equals(getSelectedSlot().getType());
  }
  
  private void checkSlot()
  {
    for (int i = 1; i < 10; i++) {
      if (Keyboard.isKeyDown(i + 1))
      {
        this.inventorySlot = (i - 1);
        return;
      }
    }
  }
  
  public boolean getDirection()
  {
    return this.direction;
  }
}
