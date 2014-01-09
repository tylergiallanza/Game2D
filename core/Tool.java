/*  1:   */ package core;
/*  2:   */ 
/*  3:   */ import org.newdawn.slick.opengl.Texture;
/*  4:   */ 
/*  5:   */ public class Tool
/*  6:   */   extends Item
/*  7:   */ {
/*  8:   */   private static Texture axe1;
/*  9:   */   private static Texture pick1;
/* 10:   */   private Texture texture;
/* 11:   */   private int rotation;
/* 12:   */   
/* 13:   */   public Tool()
/* 14:   */   {
/* 15:13 */     this.type = "axe";
/* 16:14 */     this.stack = false;
/* 17:15 */     this.texture = axe1;
/* 18:16 */     this.rotation = 45;
/* 19:17 */     this.count = 1;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Tool(String type)
/* 23:   */   {
/* 24:21 */     this.type = type;
/* 25:22 */     this.stack = false;
/* 26:23 */     this.texture = linkTexture(type);
/* 27:24 */     this.rotation = 45;
/* 28:25 */     this.count = 1;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static void init()
/* 32:   */   {
/* 33:29 */     axe1 = GameObject.loadTexture("PNG", "axe1.png");
/* 34:30 */     pick1 = GameObject.loadTexture("PNG", "pick1.png");
/* 35:   */   }
/* 36:   */   
/* 37:   */   public Texture getTexture()
/* 38:   */   {
/* 39:34 */     return this.texture;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public int getRotation()
/* 43:   */   {
/* 44:38 */     return this.rotation;
/* 45:   */   }
/* 46:   */   
/* 47:   */   private static Texture linkTexture(String input)
/* 48:   */   {
/* 49:42 */     if (input.equals("axe")) {
/* 50:42 */       return axe1;
/* 51:   */     }
/* 52:43 */     return pick1;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public static boolean isTool(String input)
/* 56:   */   {
/* 57:47 */     return (input.equals("pick")) || (input.equals("axe"));
/* 58:   */   }
/* 59:   */ }


/* Location:           C:\Users\Tgiallanza\Desktop\A\
 * Qualified Name:     core.Tool
 * JD-Core Version:    0.7.0.1
 */