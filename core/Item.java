/*  1:   */ package core;
/*  2:   */ 
/*  3:   */ public class Item
/*  4:   */ {
/*  5:   */   protected String type;
/*  6:   */   protected int count;
/*  7:   */   protected boolean stack;
/*  8:   */   
/*  9:   */   public Item()
/* 10:   */   {
/* 11:12 */     this.type = "empty";
/* 12:13 */     this.count = 0;
/* 13:14 */     this.stack = false;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Item(String t, int num)
/* 17:   */   {
/* 18:18 */     this.type = t;
/* 19:19 */     this.count = num;
/* 20:20 */     this.stack = true;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Item(Tile t)
/* 24:   */   {
/* 25:24 */     this.type = t.getType();
/* 26:25 */     this.count = 1;
/* 27:26 */     this.stack = true;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String toString()
/* 31:   */   {
/* 32:30 */     return this.type + "-" + this.count;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getType()
/* 36:   */   {
/* 37:34 */     return this.type;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void add()
/* 41:   */   {
/* 42:38 */     this.count += 1;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean getStackable()
/* 46:   */   {
/* 47:42 */     return this.stack;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void remove()
/* 51:   */   {
/* 52:46 */     this.count -= 1;
/* 53:47 */     if (this.count <= 0) {
/* 54:48 */       this.type = "empty";
/* 55:   */     }
/* 56:   */   }
/* 57:   */ }


/* Location:           C:\Users\Tgiallanza\Desktop\A\
 * Qualified Name:     core.Item
 * JD-Core Version:    0.7.0.1
 */