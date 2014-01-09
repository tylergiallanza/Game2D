package core;

import java.io.File;
import java.io.FileInputStream;
import main.Driver;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class GameObject
{
  protected Texture texture;
  protected int xPos;
  protected int yPos;
  protected int width;
  protected int height;
  
  public GameObject()
  {
    this.texture = loadTexture("PNG", "wood.png");
    this.xPos = (Driver.getWidth() / 2);
    this.yPos = (Driver.getHeight() / 2);
    this.width = this.texture.getImageWidth();
    this.height = this.texture.getImageHeight();
  }
  
  public GameObject(int x, int y, String type, String name)
  {
    this.texture = loadTexture(type, name);
    this.xPos = x;
    this.yPos = y;
    this.width = this.texture.getImageWidth();
    this.height = this.texture.getImageHeight();
  }
  
  public Texture getTexture()
  {
    return this.texture;
  }
  
  public int getX()
  {
    return this.xPos;
  }
  
  public int getY()
  {
    return this.yPos;
  }
  
  public void setY(int y)
  {
    this.yPos = y;
  }
  
  public void setX(int x)
  {
    this.xPos = x;
  }
  
  protected static Texture loadTexture(String type, String location)
  {
    try
    {
      return TextureLoader.getTexture(type, new FileInputStream(new File(location)));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public int getWidth()
  {
    return this.width;
  }
  
  public int getHeight()
  {
    return this.height;
  }
}
