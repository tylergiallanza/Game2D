/*   1:    */ 
/*   2:    */ 
/*   3:    */ java.io.PrintStream
/*   4:    */ org.newdawn.slick.opengl.Texture
/*   5:    */ 
/*   6:    */ Block
/*   7:    */   
/*   8:    */ 
/*   9:    */   WIDTH = 30
/*  10:    */   HEIGHT = 30
/*  11: 18 */   []BLOCK_TYPES = "tnt", "crate", "wood" }
/*  12: 19 */   []FREQUENCY = 3, 6, 91 }
/*  13:    */   type
/*  14:    */   breakType
/*  15:    */   breakTime
/*  16:    */   stone
/*  17:    */   wood
/*  18:    */   indestructable
/*  19:    */   grass
/*  20:    */   crate
/*  21:    */   tnt
/*  22:    */   WOODBREAKTIME = 50
/*  23:    */   STONEBREAKTIME = 100
/*  24:    */   GRASSBREAKTIME = 40
/*  25:    */   
/*  26:    */   init
/*  27:    */   
/*  28:    */     
/*  29:    */     
/*  30: 35 */       indestructable = loadTexture"PNG", "indestructable.png"
/*  31: 36 */       wood = loadTexture"PNG", "wood.png"
/*  32: 37 */       stone = loadTexture"PNG", "stone.png"
/*  33: 38 */       grass = loadTexture"PNG", "grass.png"
/*  34: 39 */       crate = loadTexture"PNG", "crate.png"
/*  35: 40 */       tnt = loadTexture"PNG", "tnt.png"
/*  36:    */     
/*  37:    */      (
/*  38:    */     
/*  39: 41 */       outprintln"Failed to init block textures"
/*  40:    */     
/*  41:    */   
/*  42:    */   
/*  43:    */   Block, 
/*  44:    */   
/*  45: 45 */     xPos = x;
/*  46:    */     
/*  47: 47 */     this.yPos = (30 - y);
/*  48: 48 */     String key = genRandBlock();
/*  49: 49 */     this.texture = loadTexture(this.type, key + ".png");
/*  50: 50 */     this.type = key;
/*  51: 51 */     this.breakTime = linkTime(key);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Block(int x, int y, String key)
/*  55:    */   {
/*  56: 55 */     this.xPos = x;
/*  57:    */     
/*  58: 57 */     this.yPos = (30 + (y == 0 ? -y : y));
/*  59: 58 */     this.texture = linkTexture(key);
/*  60: 59 */     this.breakType = linkTool(key);
/*  61: 60 */     this.type = key;
/*  62: 61 */     this.breakTime = linkTime(key);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getWidth()
/*  66:    */   {
/*  67: 65 */     return 30;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getHeight()
/*  71:    */   {
/*  72: 69 */     return 30;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static int getHeightAtX(Block[][] arr, int x)
/*  76:    */   {
/*  77: 74 */     Block[] B = arr[x];
/*  78: 75 */     for (int i = 0; i < B.length; i++) {
/*  79: 76 */       if (B[i] == null) {
/*  80: 77 */         return i * 30;
/*  81:    */       }
/*  82:    */     }
/*  83: 79 */     return -1;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String getType()
/*  87:    */   {
/*  88: 83 */     return this.type;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public String getTool()
/*  92:    */   {
/*  93: 87 */     return this.breakType;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static int getLastHeight(Block[][] arr)
/*  97:    */   {
/*  98: 91 */     return getHeightAtX(arr, arr.length - 1);
/*  99:    */   }
/* 100:    */   
/* 101:    */   private String genRandBlock()
/* 102:    */   {
/* 103: 95 */     int rand = (int)(Math.random() * 100.0D) + 1;
/* 104: 96 */     for (int i = 0; i < FREQUENCY.length; i++) {
/* 105: 97 */       if (i == 0)
/* 106:    */       {
/* 107: 98 */         if (rand <= FREQUENCY[i]) {
/* 108: 99 */           return BLOCK_TYPES[i];
/* 109:    */         }
/* 110:    */       }
/* 111:100 */       else if (i < FREQUENCY.length - 1)
/* 112:    */       {
/* 113:101 */         if ((rand > sumPrev(FREQUENCY, i)) && (rand <= sumPrev(FREQUENCY, i) + FREQUENCY[i])) {
/* 114:102 */           return BLOCK_TYPES[i];
/* 115:    */         }
/* 116:    */       }
/* 117:    */       else {
/* 118:104 */         return BLOCK_TYPES[i];
/* 119:    */       }
/* 120:    */     }
/* 121:107 */     return null;
/* 122:    */   }
/* 123:    */   
/* 124:    */   private int sumPrev(int[] arr, int index)
/* 125:    */   {
/* 126:111 */     int count = 0;
/* 127:112 */     for (int i = 0; i < index; i++) {
/* 128:113 */       count += arr[i];
/* 129:    */     }
/* 130:114 */     return count;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static Block getBlock(int x, int y)
/* 134:    */   {
/* 135:118 */     return main.Driver.getWorld().getLoaded()[((x - main.Driver.getX()) / 30)][((y - main.Driver.getY()) / 30)];
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static Texture linkTexture(String type)
/* 139:    */   {
/* 140:122 */     if (type.equals("stone")) {
/* 141:122 */       return stone;
/* 142:    */     }
/* 143:123 */     if (type.equals("wood")) {
/* 144:123 */       return wood;
/* 145:    */     }
/* 146:124 */     if (type.equals("indestructable")) {
/* 147:124 */       return indestructable;
/* 148:    */     }
/* 149:125 */     if (type.equals("grass")) {
/* 150:125 */       return grass;
/* 151:    */     }
/* 152:126 */     if (type.equals("crate")) {
/* 153:126 */       return crate;
/* 154:    */     }
/* 155:127 */     if (type.equals("tnt")) {
/* 156:127 */       return tnt;
/* 157:    */     }
/* 158:128 */     return null;
/* 159:    */   }
/* 160:    */   
/* 161:    */   private static String linkTool(String type)
/* 162:    */   {
/* 163:132 */     if (type.equals("wood")) {
/* 164:132 */       return "axe";
/* 165:    */     }
/* 166:133 */     if ((type.equals("stone")) || (type.equals("grass"))) {
/* 167:133 */       return "pick";
/* 168:    */     }
/* 169:134 */     return "any";
/* 170:    */   }
/* 171:    */   
/* 172:    */   private static int linkTime(String type)
/* 173:    */   {
/* 174:138 */     if (type.equals("wood")) {
/* 175:138 */       return 50;
/* 176:    */     }
/* 177:139 */     if (type.equals("stone")) {
/* 178:139 */       return 100;
/* 179:    */     }
/* 180:140 */     if (type.equals("grass")) {
/* 181:140 */       return 40;
/* 182:    */     }
/* 183:141 */     return 1;
/* 184:    */   }
/* 185:    */ }


/* Location:           C:\Users\Tgiallanza\Desktop\A\
 * Qualified Name:     core.Block
 * JD-Core Version:    0.7.0.1
 */