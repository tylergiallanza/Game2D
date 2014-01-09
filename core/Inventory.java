/*  1:   */ package core;
/*  2:   */ 
/*  3:   */ public class Inventory
/*  4:   */ {
/*  5:   */   public static final int INVENTORYSIZE = 20;
/*  6: 6 */   private Item[] data = new Item[20];
/*  7:   */   
/*  8:   */   public Inventory()
/*  9:   */   {
/* 10: 9 */     for (int i = 0; i < 20; i++) {
/* 11:10 */       this.data[i] = new Item();
/* 12:   */     }
/* 13:   */   }
/* 14:   */   
/* 15:   */   public String toString()
/* 16:   */   {
/* 17:14 */     String r = "";
/* 18:15 */     for (Item i : this.data) {
/* 19:16 */       r = r + i + ",";
/* 20:   */     }
/* 21:17 */     return r;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public boolean add(Item input)
/* 25:   */   {
/* 26:21 */     for (int i = 0; i < 20; i++)
/* 27:   */     {
/* 28:22 */       Item item = this.data[i];
/* 29:23 */       if (item.getType().equals("empty"))
/* 30:   */       {
/* 31:24 */         this.data[i] = input;
/* 32:25 */         return true;
/* 33:   */       }
/* 34:27 */       if ((item.getType().equals(input.getType())) && (item.getStackable()))
/* 35:   */       {
/* 36:28 */         item.add();
/* 37:29 */         return true;
/* 38:   */       }
/* 39:   */     }
/* 40:32 */     return false;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public Item[] getData()
/* 44:   */   {
/* 45:36 */     return this.data;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void loadDefault()
/* 49:   */   {
/* 50:40 */     this.data[0] = new Tool();
/* 51:41 */     this.data[1] = new Tool("pick");
/* 52:   */   }
/* 53:   */ }


/* Location:           C:\Users\Tgiallanza\Desktop\A\
 * Qualified Name:     core.Inventory
 * JD-Core Version:    0.7.0.1
 */