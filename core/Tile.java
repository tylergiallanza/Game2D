package core;

import main.Driver;
import physics.Gravity;

public class Tile
  extends GameObject
  implements Gravity
{
  public static final int WIDTH = 15;
  public static final int HEIGHT = 15;
  private String type;
  
  public Tile(int x, int y, String type)
  {
    this.type = type;
    this.texture = Block.linkTexture(type);
    this.xPos = x;
    this.yPos = y;
  }
  
  public Tile(Item input, int x, int y)
  {
    this.type = input.getType();
    if ((input instanceof Tool)) {
      this.texture = ((Tool)input).getTexture();
    } else {
      this.texture = Block.linkTexture(this.type);
    }
    this.xPos = x;
    this.yPos = y;
  }
  
  public int getWidth()
  {
    return 15;
  }
  
  public int getHeight()
  {
    return 15;
  }
  
  public void updateGravity()
  {
    if (!Driver.getWorld().isOnMap(this)) {
      this.yPos -= 2;
    }
  }
  
  public String getType()
  {
    return this.type;
  }
}
