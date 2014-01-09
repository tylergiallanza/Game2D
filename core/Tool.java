package core;

import org.newdawn.slick.opengl.Texture;

public class Tool
  extends Item
{
  private static Texture axe1;
  private static Texture pick1;
  private Texture texture;
  private int rotation;
  
  public Tool()
  {
    this.type = "axe";
    this.stack = false;
    this.texture = axe1;
    this.rotation = 45;
    this.count = 1;
  }
  
  public Tool(String type)
  {
    this.type = type;
    this.stack = false;
    this.texture = linkTexture(type);
    this.rotation = 45;
    this.count = 1;
  }
  
  public static void init()
  {
    axe1 = GameObject.loadTexture("PNG", "axe1.png");
    pick1 = GameObject.loadTexture("PNG", "pick1.png");
  }
  
  public Texture getTexture()
  {
    return this.texture;
  }
  
  public int getRotation()
  {
    return this.rotation;
  }
  
  private static Texture linkTexture(String input)
  {
    if (input.equals("axe")) {
      return axe1;
    }
    return pick1;
  }
  
  public static boolean isTool(String input)
  {
    return (input.equals("pick")) || (input.equals("axe"));
  }
}
