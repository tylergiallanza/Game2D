package core;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class World
{
  public static final int WIDTH = 50;
  public static final int HEIGHT = 40;
  private Block[][] data;
  private ArrayList<Tile> tiles = new ArrayList();
  private String folderName;
  
  public World(String name)
  {
    this.folderName = name;
    this.data = new Block[50][40];
    initWorld(20);
  }
  
  public World()
  {
    this.folderName = "";
    this.data = new Block[50][40];
  }
  
  public void load(String name)
  {
    this.folderName = name;
    this.data = fromFile(name);
  }
  
  public Block[][] getLoaded()
  {
    return this.data;
  }
  
  public void save()
  {
    toFile(this.data, this.folderName);
  }
  
  public static Block[][] fromFile(String fileName)
  {
    Block[][] map = new Block[50][40];
    try
    {
      Scanner readMap = new Scanner(new File(fileName));
      int countX = 0;
      while (readMap.hasNext())
      {
        String line = readMap.nextLine();
        
        Scanner readLine = new Scanner(line);
        readLine.useDelimiter("/");
        int countY = 0;
        while (readLine.hasNext())
        {
          String token = readLine.next();
          if (token.contains(".")) {
            map[countX][countY] = null;
          } else {
            map[countX][countY] = new Block(countX * 30, countY * 30, token);
          }
          countY++;
        }
        countX++;
      }
    }
    catch (Exception e)
    {
      System.out.println("Failed to load map");e.printStackTrace();return null;
    }
    return map;
  }
  
  public static File toFile(Block[][] arr, String fileName)
  {
    String r = "";
    Block[][] arrayOfBlock = arr;int j = arr.length;
    for (int i = 0; i < j; i++)
    {
      Block[] B = arrayOfBlock[i];
      for (Block b : B) {
        if (b == null) {
          r = r + "/.";
        } else {
          r = r + "/" + b.getType();
        }
      }
      r = r + "\n";
    }
    try
    {
      File file = new File(fileName);
      FileWriter writer = new FileWriter(file);
      writer.write(r, 0, r.length());
      writer.close();
      return file;
    }
    catch (Exception e)
    {
      System.out.println("Failed to write chunk data");
    }
    return null;
  }
  
  private void initWorld(int start)
  {
    Block[][] chunk = new Block[50][40];
    boolean treeLeft = false;
    for (int i = 0; i < 50; i++)
    {
      start += (int)(Math.random() * 3.0D - 1.5D);
      if (start >= 50) {
        start = 49;
      }
      if (start <= 0) {
        start = 1;
      }
      chunk[i][start] = new Block(i * 30, start * 30, "grass");
      chunk[i][0] = new Block(i * 30, 0, "indestructable");
      for (int j = 1; j < start; j++) {
        chunk[i][j] = new Block(i * 30, j * 30, "stone");
      }
      if ((Math.random() < 0.1D) && (!treeLeft))
      {
        System.out.println("Planting tree");
        for (int k = start + 1; k < start + 7; k++) {
          chunk[i][k] = new Block(i * 30, k * 30, "wood");
        }
        treeLeft = true;
      }
      else
      {
        treeLeft = false;
      }
    }
    this.data = chunk;
  }
  
  public ArrayList<Tile> getTileInfo()
  {
    return this.tiles;
  }
  
  public int getHighestY(int x, int y)
  {
    while (isOnMap(x, y)) {
      y += 2;
    }
    return y - 2;
  }
  
  public boolean isOnMap(int x, int y)
  {
    try
    {
      Block[][] b = getLoaded();
      if (b[(x / 30)][(y / 30)] != null) {
        return true;
      }
    }
    catch (Exception e)
    {
      return false;
    }
    return false;
  }
  
  public boolean isOnMap(GameObject obj)
  {
    try
    {
      if (this.data[((obj.getX() + obj.getWidth() / 2) / 30)][((obj.getY() - 1) / 30)] != null) {
        return true;
      }
    }
    catch (Exception e)
    {
      return false;
    }
    return false;
  }
  
  public boolean isPlayerOnMap(GameObject obj)
  {
    try
    {
      if (this.data[((obj.getX() + obj.getWidth() - 2) / 30)][((obj.getY() - 2) / 30)] != null) {
        return true;
      }
      if (this.data[((obj.getX() + obj.getWidth() / 2) / 30)][((obj.getY() - 2) / 30)] != null) {
        return true;
      }
      if (this.data[(obj.getX() / 30)][((obj.getY() - 2) / 30)] != null) {
        return true;
      }
      if (this.data[((obj.getX() + obj.getWidth() - 2) / 30)][((obj.getY() - 2 + obj.getHeight()) / 30)] != null) {
        return true;
      }
      if (this.data[((obj.getX() + obj.getWidth() / 2) / 30)][((obj.getY() - 2 + obj.getHeight()) / 30)] != null) {
        return true;
      }
      if (this.data[(obj.getX() / 30)][((obj.getY() - 2 + obj.getHeight()) / 30)] != null) {
        return true;
      }
    }
    catch (Exception e)
    {
      return false;
    }
    return false;
  }
}
