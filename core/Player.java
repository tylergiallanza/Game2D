/*   1:    */ package core;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileWriter;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Scanner;
/*   8:    */ import main.Driver;
/*   9:    */ import org.lwjgl.input.Keyboard;
/*  10:    */ import org.lwjgl.input.Mouse;
/*  11:    */ import physics.Explosion;
/*  12:    */ import physics.Gravity;
/*  13:    */ 
/*  14:    */ public class Player
/*  15:    */   extends GameObject
/*  16:    */   implements Gravity
/*  17:    */ {
/*  18:    */   private static final int REACH = 70;
/*  19: 17 */   private double yVel = 0.0D;
/*  20:    */   private static final int XVELOCITY = 3;
/*  21: 19 */   private final double GRAVITY = 0.4D;
/*  22: 20 */   private final double JUMPVELOCITY = 7.0D;
/*  23: 21 */   private boolean canJump = false;
/*  24:    */   private boolean direction;
/*  25:    */   private Inventory inventory;
/*  26: 24 */   private int inventorySlot = 0;
/*  27:    */   
/*  28:    */   public Player(int x, int y, String type, String name)
/*  29:    */   {
/*  30: 27 */     this.inventory = new Inventory();
/*  31: 28 */     this.inventory.loadDefault();
/*  32: 29 */     this.texture = loadTexture(type, name);
/*  33: 30 */     this.xPos = x;
/*  34: 31 */     this.yPos = y;
/*  35: 32 */     this.width = 36;
/*  36: 33 */     this.height = 72;
/*  37: 34 */     this.direction = true;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Player(int x, int y, String type, String name, Inventory i)
/*  41:    */   {
/*  42: 38 */     this.inventory = i;
/*  43: 39 */     this.texture = loadTexture(type, name);
/*  44: 40 */     this.xPos = x;
/*  45: 41 */     this.yPos = y;
/*  46: 42 */     this.width = 36;
/*  47: 43 */     this.height = 72;
/*  48: 44 */     this.direction = true;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void update()
/*  52:    */   {
/*  53: 48 */     checkTiles();
/*  54: 49 */     if (Mouse.isButtonDown(0)) {
/*  55: 50 */       breakAtMouse(Mouse.getX(), Mouse.getY());
/*  56:    */     }
/*  57: 52 */     if ((Mouse.isButtonDown(1)) && 
/*  58: 53 */       (placeAtMouse(Mouse.getX(), Mouse.getY()))) {
/*  59: 54 */       getSelectedSlot().remove();
/*  60:    */     }
/*  61: 57 */     checkMotion();
/*  62: 58 */     checkSlot();
/*  63: 59 */     if (!Driver.getWorld().isPlayerOnMap(this))
/*  64:    */     {
/*  65: 60 */       updateGravity();
/*  66:    */     }
/*  67:    */     else
/*  68:    */     {
/*  69: 62 */       this.yVel = 0.0D;
/*  70: 63 */       this.canJump = true;
/*  71:    */     }
/*  72: 76 */     updateWindow();
/*  73:    */   }
/*  74:    */   
/*  75:    */   private void breakAtMouse(int x, int y)
/*  76:    */   {
/*  77: 80 */     if (Block.getBlock(x, y) == null) {
/*  78: 80 */       return;
/*  79:    */     }
/*  80: 81 */     if (!canBreak(Block.getBlock(x, y))) {
/*  81: 81 */       return;
/*  82:    */     }
/*  83: 82 */     if ((x > Driver.WIDTH / 2 - 70) && (x < Driver.WIDTH / 2 + 70 + this.width) && 
/*  84: 83 */       (y > Driver.HEIGHT / 2 - 70) && (y < Driver.HEIGHT / 2 + 70 + this.height))
/*  85:    */     {
/*  86: 84 */       Block.getBlock(x, y).breakTime -= 1;
/*  87: 85 */       if (Block.getBlock(x, y).breakTime <= 0) {
/*  88: 86 */         deleteAtMouse(x, y);
/*  89:    */       }
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   private boolean deleteAtMouse(int x, int y)
/*  94:    */   {
/*  95:    */     try
/*  96:    */     {
/*  97: 93 */       if (Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)].getType().equals("tnt")) {
/*  98: 94 */         Explosion.explode((x - Driver.getX()) / 30, (y - Driver.getY()) / 30);
/*  99:    */       }
/* 100: 95 */       if (!Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)].getType().equals("indestructable"))
/* 101:    */       {
/* 102: 96 */         String t = Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)].getType();
/* 103: 97 */         Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)] = null;
/* 104: 98 */         Driver.getWorld().getTileInfo().add(new Tile(x - Driver.getX(), y - Driver.getY(), t));
/* 105:    */       }
/* 106:    */     }
/* 107:    */     catch (Exception e)
/* 108:    */     {
/* 109:101 */       return false;
/* 110:    */     }
/* 111:103 */     return true;
/* 112:    */   }
/* 113:    */   
/* 114:    */   private boolean placeAtMouse(int x, int y)
/* 115:    */   {
/* 116:107 */     if ((getSelectedSlot().getType().equals("empty")) || ((getSelectedSlot() instanceof Tool))) {
/* 117:107 */       return false;
/* 118:    */     }
/* 119:108 */     if (((Block.getBlock(x + 30, y) != null) || (Block.getBlock(x, y + 30) != null) || (Block.getBlock(x, y - 30) != null) || (Block.getBlock(x - 30, y) != null)) && 
/* 120:109 */       (x > Driver.WIDTH / 2 - 70) && (x < Driver.WIDTH / 2 + 70 + this.width) && 
/* 121:110 */       (y > Driver.HEIGHT / 2 - 70) && (y < Driver.HEIGHT / 2 + 70 + this.height) && 
/* 122:111 */       (Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)] == null))
/* 123:    */     {
/* 124:112 */       Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)] = new Block(x - Driver.getX() - (x - Driver.getX()) % 30, y - Driver.getY() - (y - Driver.getY()) % 30, getSelectedSlot().getType());
/* 125:113 */       if (Driver.getPlayer().isInGround())
/* 126:    */       {
/* 127:114 */         Driver.getWorld().getLoaded()[((x - Driver.getX()) / 30)][((y - Driver.getY()) / 30)] = null;
/* 128:115 */         return false;
/* 129:    */       }
/* 130:116 */       return true;
/* 131:    */     }
/* 132:121 */     return false;
/* 133:    */   }
/* 134:    */   
/* 135:    */   private void checkMotion()
/* 136:    */   {
/* 137:125 */     if ((Keyboard.isKeyDown(32)) && (canMoveRight()))
/* 138:    */     {
/* 139:126 */       this.direction = true;
/* 140:127 */       setX(getX() + 3);
/* 141:128 */       Driver.changeX(-3);
/* 142:    */     }
/* 143:130 */     if ((Keyboard.isKeyDown(30)) && (canMoveLeft()))
/* 144:    */     {
/* 145:131 */       this.direction = false;
/* 146:132 */       setX(getX() - 3);
/* 147:133 */       Driver.changeX(3);
/* 148:    */     }
/* 149:135 */     if ((Keyboard.isKeyDown(57)) && (this.canJump))
/* 150:    */     {
/* 151:136 */       this.canJump = false;
/* 152:137 */       this.yVel = 7.0D;
/* 153:138 */       updateGravity();
/* 154:    */     }
/* 155:    */   }
/* 156:    */   
/* 157:    */   private boolean canMoveRight()
/* 158:    */   {
/* 159:143 */     return (!Driver.getWorld().isOnMap(getX() + getWidth() + 1, getY())) && (!Driver.getWorld().isOnMap(getX() + getWidth() + 2, getY() + getHeight() / 2)) && (!Driver.getWorld().isOnMap(getX() + getWidth() + 2, getY() + getHeight() - 10));
/* 160:    */   }
/* 161:    */   
/* 162:    */   private boolean canMoveLeft()
/* 163:    */   {
/* 164:147 */     return (!Driver.getWorld().isOnMap(getX() - 1, getY())) && (!Driver.getWorld().isOnMap(getX() - 1, getY() + getHeight() / 2)) && (!Driver.getWorld().isOnMap(getX() - 1, getY() + getHeight() - 10));
/* 165:    */   }
/* 166:    */   
/* 167:    */   private boolean canMoveUp(int vel)
/* 168:    */   {
/* 169:152 */     return (!Driver.getWorld().isOnMap(getX() + 1, getY() - this.height - vel)) && (!Driver.getWorld().isOnMap(getX() + getWidth() - 1, getY() - this.height - vel)) && (!Driver.getWorld().isOnMap(getX() + this.width / 2, getY() - this.height - vel));
/* 170:    */   }
/* 171:    */   
/* 172:    */   private boolean isInGround()
/* 173:    */   {
/* 174:156 */     boolean l = (Driver.getWorld().isOnMap(this.xPos, this.yPos)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos)) || 
/* 175:157 */       (Driver.getWorld().isOnMap(this.xPos, this.yPos + this.height / 2)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos + this.height / 2)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos + this.height / 2)) || 
/* 176:158 */       (Driver.getWorld().isOnMap(this.xPos, this.yPos + this.height)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos + this.height)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos + this.height));
/* 177:159 */     System.out.println(l);
/* 178:160 */     System.out.println(isHeadInGround());
/* 179:161 */     System.out.println("F" + areFeetInGround());
/* 180:162 */     return l;
/* 181:    */   }
/* 182:    */   
/* 183:    */   private boolean areFeetInGround()
/* 184:    */   {
/* 185:166 */     return (Driver.getWorld().isOnMap(this.xPos, this.yPos)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos));
/* 186:    */   }
/* 187:    */   
/* 188:    */   private boolean isHeadInGround()
/* 189:    */   {
/* 190:170 */     return (Driver.getWorld().isOnMap(this.xPos, this.yPos + this.height)) || (Driver.getWorld().isOnMap(this.xPos + this.width / 2, this.yPos + this.height)) || (Driver.getWorld().isOnMap(this.xPos + this.width - 1, this.yPos + this.height));
/* 191:    */   }
/* 192:    */   
/* 193:    */   public static File toFile(Player input, String fileName)
/* 194:    */   {
/* 195:174 */     String write = "";
/* 196:175 */     write = write + input.getX() + " \n";
/* 197:176 */     write = write + input.getY() + " \n";
/* 198:177 */     for (Item i : input.inventory.getData()) {
/* 199:178 */       write = write + i.getType() + " " + i.count + " \n";
/* 200:    */     }
/* 201:    */     try
/* 202:    */     {
/* 203:181 */       File file = new File(fileName);
/* 204:182 */       FileWriter writer = new FileWriter(file);
/* 205:183 */       writer.write(write);
/* 206:184 */       writer.close();
/* 207:185 */       return file;
/* 208:    */     }
/* 209:    */     catch (Exception e)
/* 210:    */     {
/* 211:187 */       System.out.println("Failed to write player file");
/* 212:    */     }
/* 213:188 */     return null;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public static Player fromFile(String fileName)
/* 217:    */   {
/* 218:    */     try
/* 219:    */     {
/* 220:194 */       Scanner readFile = new Scanner(new File(fileName));
/* 221:195 */       int x = Integer.parseInt(readFile.nextLine().trim());
/* 222:196 */       int y = Integer.parseInt(readFile.nextLine().trim());
/* 223:197 */       Inventory i = new Inventory();
/* 224:198 */       for (int j = 0; j < 20; j++)
/* 225:    */       {
/* 226:199 */         String line = readFile.nextLine();
/* 227:200 */         Scanner readLine = new Scanner(line);
/* 228:201 */         String type = readLine.next();
/* 229:202 */         if (Tool.isTool(type)) {
/* 230:203 */           i.getData()[j] = new Tool(type);
/* 231:    */         } else {
/* 232:205 */           i.getData()[j] = new Item(type, Integer.parseInt(readLine.next()));
/* 233:    */         }
/* 234:    */       }
/* 235:207 */       System.out.println(i);
/* 236:208 */       return new Player(x, y, "PNG", "person3.png", i);
/* 237:    */     }
/* 238:    */     catch (Exception e)
/* 239:    */     {
/* 240:210 */       e.printStackTrace();
/* 241:211 */       System.out.println("failed to read player file");
/* 242:    */     }
/* 243:212 */     return null;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void updateGravity()
/* 247:    */   {
/* 248:218 */     if (this.yVel > 0.0D) {
/* 249:219 */       if (canMoveUp((int)this.yVel))
/* 250:    */       {
/* 251:220 */         this.yPos = ((int)(this.yPos - this.yVel));
/* 252:    */       }
/* 253:    */       else
/* 254:    */       {
/* 255:222 */         int i = 0;
/* 256:223 */         for (i = (int)this.yVel; i >= 0; i--) {
/* 257:224 */           if (canMoveUp(i)) {
/* 258:    */             break;
/* 259:    */           }
/* 260:    */         }
/* 261:225 */         this.yPos -= i + 1;
/* 262:226 */         this.yVel = 0.0D;
/* 263:    */       }
/* 264:    */     }
/* 265:229 */     if (this.yVel <= 0.0D)
/* 266:    */     {
/* 267:230 */       this.canJump = false;
/* 268:231 */       this.yPos = ((int)(this.yPos + this.yVel));
/* 269:    */     }
/* 270:233 */     this.yVel += 0.4D;
/* 271:    */   }
/* 272:    */   
/* 273:    */   private boolean isPlayer(int x, int y, int w, int h)
/* 274:    */   {
/* 275:237 */     int xCenter = Driver.WIDTH / 2 - this.width / 2;
/* 276:238 */     int yCenter = Driver.HEIGHT / 2;
/* 277:239 */     if ((x > xCenter) && (x < xCenter + this.width) && (y > yCenter) && (y < yCenter + this.height)) {
/* 278:239 */       return true;
/* 279:    */     }
/* 280:240 */     if ((x > xCenter + w) && (x < xCenter + w + this.width) && (y > yCenter) && (y < yCenter + this.height)) {
/* 281:240 */       return true;
/* 282:    */     }
/* 283:241 */     if ((x > xCenter + w) && (x < xCenter + this.width + w) && (y > yCenter + h) && (y < yCenter + this.height + h)) {
/* 284:241 */       return true;
/* 285:    */     }
/* 286:242 */     if ((x > xCenter) && (x < xCenter + this.width) && (y > yCenter + h) && (y < yCenter + this.height + h)) {
/* 287:242 */       return true;
/* 288:    */     }
/* 289:243 */     return false;
/* 290:    */   }
/* 291:    */   
/* 292:    */   private void updateWindow()
/* 293:    */   {
/* 294:247 */     Driver.setY(Driver.getY() - (Driver.getY() + (this.yPos - Driver.HEIGHT / 2)));
/* 295:    */   }
/* 296:    */   
/* 297:    */   private void checkTiles()
/* 298:    */   {
/* 299:251 */     for (int i = 0; i < Driver.getWorld().getTileInfo().size(); i++)
/* 300:    */     {
/* 301:252 */       Tile t = (Tile)Driver.getWorld().getTileInfo().get(i);
/* 302:253 */       if ((Math.abs(t.xPos - this.xPos) < 40) && (Math.abs(t.yPos - this.yPos) < 40) && 
/* 303:254 */         (this.inventory.add(new Item(t)))) {
/* 304:255 */         Driver.getWorld().getTileInfo().remove(i);
/* 305:    */       }
/* 306:    */     }
/* 307:    */   }
/* 308:    */   
/* 309:    */   public Item getSelectedSlot()
/* 310:    */   {
/* 311:262 */     return this.inventory.getData()[this.inventorySlot];
/* 312:    */   }
/* 313:    */   
/* 314:    */   private boolean canBreak(Block b)
/* 315:    */   {
/* 316:266 */     return b.getTool().equals(getSelectedSlot().getType());
/* 317:    */   }
/* 318:    */   
/* 319:    */   private void checkSlot()
/* 320:    */   {
/* 321:270 */     for (int i = 1; i < 10; i++) {
/* 322:271 */       if (Keyboard.isKeyDown(i + 1))
/* 323:    */       {
/* 324:272 */         this.inventorySlot = (i - 1);
/* 325:273 */         return;
/* 326:    */       }
/* 327:    */     }
/* 328:    */   }
/* 329:    */   
/* 330:    */   public boolean getDirection()
/* 331:    */   {
/* 332:279 */     return this.direction;
/* 333:    */   }
/* 334:    */ }


/* Location:           C:\Users\Tgiallanza\Desktop\A\
 * Qualified Name:     core.Player
 * JD-Core Version:    0.7.0.1
 */