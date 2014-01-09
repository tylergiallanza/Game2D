/*  1:   */ package core;
/*  2:   */ 
/*  3:   */ import main.Driver;
/*  4:   */ import physics.Gravity;
/*  5:   */ 
/*  6:   */ public class Tile
/*  7:   */   extends GameObject
/*  8:   */   implements Gravity
/*  9:   */ {
/* 10:   */   public static final int WIDTH = 15;
/* 11:   */   public static final int HEIGHT = 15;
/* 12:   */   private String type;
/* 13:   */   
/* 14:   */   public Tile(int x, int y, String type)
/* 15:   */   {
/* 16:13 */     this.type = type;
/* 17:14 */     this.texture = Block.linkTexture(type);
/* 18:15 */     this.xPos = x;
/* 19:16 */     this.yPos = y;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Tile(Item input, int x, int y)
/* 23:   */   {
/* 24:20 */     this.type = input.getType();
/* 25:21 */     if ((input instanceof Tool)) {
/* 26:21 */       this.texture = ((Tool)input).getTexture();
/* 27:   */     } else {
/* 28:22 */       this.texture = Block.linkTexture(this.type);
/* 29:   */     }
/* 30:23 */     this.xPos = x;
/* 31:24 */     this.yPos = y;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public int getWidth()
/* 35:   */   {
/* 36:28 */     return 15;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public int getHeight()
/* 40:   */   {
/* 41:32 */     return 15;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void updateGravity()
/* 45:   */   {
/* 46:36 */     if (!Driver.getWorld().isOnMap(this)) {
/* 47:36 */       this.yPos -= 2;
/* 48:   */     }
/* 49:   */   }
/* 50:   */   
/* 51:   */   public String getType()
/* 52:   */   {
/* 53:40 */     return this.type;
/* 54:   */   }
/* 55:   */ }


/* Location:           C:\Users\Tgiallanza\Desktop\A\
 * Qualified Name:     core.Tile
 * JD-Core Version:    0.7.0.1
 */