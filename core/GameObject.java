/*  1:   */ package core;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.FileInputStream;
/*  5:   */ import main.Driver;
/*  6:   */ import org.newdawn.slick.opengl.Texture;
/*  7:   */ import org.newdawn.slick.opengl.TextureLoader;
/*  8:   */ 
/*  9:   */ public class GameObject
/* 10:   */ {
/* 11:   */   protected Texture texture;
/* 12:   */   protected int xPos;
/* 13:   */   protected int yPos;
/* 14:   */   protected int width;
/* 15:   */   protected int height;
/* 16:   */   
/* 17:   */   public GameObject()
/* 18:   */   {
/* 19:19 */     this.texture = loadTexture("PNG", "wood.png");
/* 20:20 */     this.xPos = (Driver.getWidth() / 2);
/* 21:21 */     this.yPos = (Driver.getHeight() / 2);
/* 22:22 */     this.width = this.texture.getImageWidth();
/* 23:23 */     this.height = this.texture.getImageHeight();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public GameObject(int x, int y, String type, String name)
/* 27:   */   {
/* 28:27 */     this.texture = loadTexture(type, name);
/* 29:28 */     this.xPos = x;
/* 30:29 */     this.yPos = y;
/* 31:30 */     this.width = this.texture.getImageWidth();
/* 32:31 */     this.height = this.texture.getImageHeight();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Texture getTexture()
/* 36:   */   {
/* 37:35 */     return this.texture;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public int getX()
/* 41:   */   {
/* 42:39 */     return this.xPos;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public int getY()
/* 46:   */   {
/* 47:43 */     return this.yPos;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void setY(int y)
/* 51:   */   {
/* 52:47 */     this.yPos = y;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void setX(int x)
/* 56:   */   {
/* 57:51 */     this.xPos = x;
/* 58:   */   }
/* 59:   */   
/* 60:   */   protected static Texture loadTexture(String type, String location)
/* 61:   */   {
/* 62:   */     try
/* 63:   */     {
/* 64:56 */       return TextureLoader.getTexture(type, new FileInputStream(new File(location)));
/* 65:   */     }
/* 66:   */     catch (Exception e)
/* 67:   */     {
/* 68:57 */       e.printStackTrace();
/* 69:   */     }
/* 70:58 */     return null;
/* 71:   */   }
/* 72:   */   
/* 73:   */   public int getWidth()
/* 74:   */   {
/* 75:62 */     return this.width;
/* 76:   */   }
/* 77:   */   
/* 78:   */   public int getHeight()
/* 79:   */   {
/* 80:66 */     return this.height;
/* 81:   */   }
/* 82:   */ }


/* Location:           C:\Users\Tgiallanza\Desktop\A\
 * Qualified Name:     core.GameObject
 * JD-Core Version:    0.7.0.1
 */