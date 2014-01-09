/*   1:    */ package core;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileWriter;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Scanner;
/*   8:    */ 
/*   9:    */ public class World
/*  10:    */ {
/*  11:    */   public static final int WIDTH = 50;
/*  12:    */   public static final int HEIGHT = 40;
/*  13:    */   private Block[][] data;
/*  14: 13 */   private ArrayList<Tile> tiles = new ArrayList();
/*  15:    */   private String folderName;
/*  16:    */   
/*  17:    */   public World(String name)
/*  18:    */   {
/*  19: 17 */     this.folderName = name;
/*  20: 18 */     this.data = new Block[50][40];
/*  21: 19 */     initWorld(20);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public World()
/*  25:    */   {
/*  26: 23 */     this.folderName = "";
/*  27: 24 */     this.data = new Block[50][40];
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void load(String name)
/*  31:    */   {
/*  32: 28 */     this.folderName = name;
/*  33: 29 */     this.data = fromFile(name);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public Block[][] getLoaded()
/*  37:    */   {
/*  38: 33 */     return this.data;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void save()
/*  42:    */   {
/*  43: 37 */     toFile(this.data, this.folderName);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static Block[][] fromFile(String fileName)
/*  47:    */   {
/*  48: 41 */     Block[][] map = new Block[50][40];
/*  49:    */     try
/*  50:    */     {
/*  51: 43 */       Scanner readMap = new Scanner(new File(fileName));
/*  52: 44 */       int countX = 0;
/*  53: 45 */       while (readMap.hasNext())
/*  54:    */       {
/*  55: 46 */         String line = readMap.nextLine();
/*  56:    */         
/*  57: 48 */         Scanner readLine = new Scanner(line);
/*  58: 49 */         readLine.useDelimiter("/");
/*  59: 50 */         int countY = 0;
/*  60: 51 */         while (readLine.hasNext())
/*  61:    */         {
/*  62: 52 */           String token = readLine.next();
/*  63: 53 */           if (token.contains(".")) {
/*  64: 54 */             map[countX][countY] = null;
/*  65:    */           } else {
/*  66: 56 */             map[countX][countY] = new Block(countX * 30, countY * 30, token);
/*  67:    */           }
/*  68: 59 */           countY++;
/*  69:    */         }
/*  70: 61 */         countX++;
/*  71:    */       }
/*  72:    */     }
/*  73:    */     catch (Exception e)
/*  74:    */     {
/*  75: 63 */       System.out.println("Failed to load map");e.printStackTrace();return null;
/*  76:    */     }
/*  77: 64 */     return map;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static File toFile(Block[][] arr, String fileName)
/*  81:    */   {
/*  82: 68 */     String r = "";
/*  83: 69 */     Block[][] arrayOfBlock = arr;int j = arr.length;
/*  84: 69 */     for (int i = 0; i < j; i++)
/*  85:    */     {
/*  86: 69 */       Block[] B = arrayOfBlock[i];
/*  87: 70 */       for (Block b : B) {
/*  88: 71 */         if (b == null) {
/*  89: 72 */           r = r + "/.";
/*  90:    */         } else {
/*  91: 74 */           r = r + "/" + b.getType();
/*  92:    */         }
/*  93:    */       }
/*  94: 77 */       r = r + "\n";
/*  95:    */     }
/*  96:    */     try
/*  97:    */     {
/*  98: 80 */       File file = new File(fileName);
/*  99: 81 */       FileWriter writer = new FileWriter(file);
/* 100: 82 */       writer.write(r, 0, r.length());
/* 101: 83 */       writer.close();
/* 102: 84 */       return file;
/* 103:    */     }
/* 104:    */     catch (Exception e)
/* 105:    */     {
/* 106: 85 */       System.out.println("Failed to write chunk data");
/* 107:    */     }
/* 108: 85 */     return null;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private void initWorld(int start)
/* 112:    */   {
/* 113: 90 */     Block[][] chunk = new Block[50][40];
/* 114: 91 */     boolean treeLeft = false;
/* 115: 92 */     for (int i = 0; i < 50; i++)
/* 116:    */     {
/* 117: 93 */       start += (int)(Math.random() * 3.0D - 1.5D);
/* 118: 94 */       if (start >= 50) {
/* 119: 94 */         start = 49;
/* 120:    */       }
/* 121: 95 */       if (start <= 0) {
/* 122: 95 */         start = 1;
/* 123:    */       }
/* 124: 96 */       chunk[i][start] = new Block(i * 30, start * 30, "grass");
/* 125: 97 */       chunk[i][0] = new Block(i * 30, 0, "indestructable");
/* 126: 98 */       for (int j = 1; j < start; j++) {
/* 127: 99 */         chunk[i][j] = new Block(i * 30, j * 30, "stone");
/* 128:    */       }
/* 129:101 */       if ((Math.random() < 0.1D) && (!treeLeft))
/* 130:    */       {
/* 131:102 */         System.out.println("Planting tree");
/* 132:103 */         for (int k = start + 1; k < start + 7; k++) {
/* 133:104 */           chunk[i][k] = new Block(i * 30, k * 30, "wood");
/* 134:    */         }
/* 135:106 */         treeLeft = true;
/* 136:    */       }
/* 137:    */       else
/* 138:    */       {
/* 139:108 */         treeLeft = false;
/* 140:    */       }
/* 141:    */     }
/* 142:111 */     this.data = chunk;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public ArrayList<Tile> getTileInfo()
/* 146:    */   {
/* 147:115 */     return this.tiles;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public int getHighestY(int x, int y)
/* 151:    */   {
/* 152:120 */     while (isOnMap(x, y)) {
/* 153:121 */       y += 2;
/* 154:    */     }
/* 155:123 */     return y - 2;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public boolean isOnMap(int x, int y)
/* 159:    */   {
/* 160:    */     try
/* 161:    */     {
/* 162:128 */       Block[][] b = getLoaded();
/* 163:130 */       if (b[(x / 30)][(y / 30)] != null) {
/* 164:130 */         return true;
/* 165:    */       }
/* 166:    */     }
/* 167:    */     catch (Exception e)
/* 168:    */     {
/* 169:131 */       return false;
/* 170:    */     }
/* 171:132 */     return false;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public boolean isOnMap(GameObject obj)
/* 175:    */   {
/* 176:    */     try
/* 177:    */     {
/* 178:138 */       if (this.data[((obj.getX() + obj.getWidth() / 2) / 30)][((obj.getY() - 1) / 30)] != null) {
/* 179:138 */         return true;
/* 180:    */       }
/* 181:    */     }
/* 182:    */     catch (Exception e)
/* 183:    */     {
/* 184:140 */       return false;
/* 185:    */     }
/* 186:141 */     return false;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public boolean isPlayerOnMap(GameObject obj)
/* 190:    */   {
/* 191:    */     try
/* 192:    */     {
/* 193:147 */       if (this.data[((obj.getX() + obj.getWidth() - 2) / 30)][((obj.getY() - 2) / 30)] != null) {
/* 194:147 */         return true;
/* 195:    */       }
/* 196:148 */       if (this.data[((obj.getX() + obj.getWidth() / 2) / 30)][((obj.getY() - 2) / 30)] != null) {
/* 197:148 */         return true;
/* 198:    */       }
/* 199:149 */       if (this.data[(obj.getX() / 30)][((obj.getY() - 2) / 30)] != null) {
/* 200:149 */         return true;
/* 201:    */       }
/* 202:151 */       if (this.data[((obj.getX() + obj.getWidth() - 2) / 30)][((obj.getY() - 2 + obj.getHeight()) / 30)] != null) {
/* 203:151 */         return true;
/* 204:    */       }
/* 205:152 */       if (this.data[((obj.getX() + obj.getWidth() / 2) / 30)][((obj.getY() - 2 + obj.getHeight()) / 30)] != null) {
/* 206:152 */         return true;
/* 207:    */       }
/* 208:153 */       if (this.data[(obj.getX() / 30)][((obj.getY() - 2 + obj.getHeight()) / 30)] != null) {
/* 209:153 */         return true;
/* 210:    */       }
/* 211:    */     }
/* 212:    */     catch (Exception e)
/* 213:    */     {
/* 214:155 */       return false;
/* 215:    */     }
/* 216:156 */     return false;
/* 217:    */   }
/* 218:    */ }


/* Location:           C:\Users\Tgiallanza\Desktop\A\
 * Qualified Name:     core.World
 * JD-Core Version:    0.7.0.1
 */