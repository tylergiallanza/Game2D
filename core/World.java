package core;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class World {

	public static final int WIDTH = 50;
	public static final int HEIGHT = 40;
	private Block[][] data;
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private String folderName;

	public World(String name) {
		folderName = name;
		data = new Block[WIDTH][HEIGHT];
		initWorld(HEIGHT/2);
	}

	public World() {
		folderName = "";
		data = new Block[WIDTH][HEIGHT];
	}

	public void load(String name){
		folderName = name;
		data = fromFile(name);
	}

	public Block[][] getLoaded(){
		return data;
	}

	public void save(){
		toFile(data, folderName);
	}

	public static Block[][] fromFile(String fileName){
		Block[][] map = new Block[WIDTH][HEIGHT];
		try{
			Scanner readMap = new Scanner(new File(fileName));
			int countX = 0;
			while(readMap.hasNext()){
				String line = readMap.nextLine();
				//System.out.println("Reading text: " + line + " for xValue " + countX);
				Scanner readLine = new Scanner(line);
				readLine.useDelimiter("/");
				int countY = 0;
				while(readLine.hasNext()){
					String token = readLine.next();
					if(token.contains(".")){
						map[countX][countY] = null;
					} else {
						map[countX][countY] = new Block(countX * 30, countY * 30, token);
						//System.out.println("creating block at x" + countX + " and y " + countY);
					}
					countY++;
				}
				countX++;
			}
		}catch(Exception e){System.out.println("Failed to load map"); e.printStackTrace(); return null; }
		return map;
	}

	public static File toFile(Block[][] arr, String fileName){
		String r = "";
		for(Block[] B : arr){
			for(Block b : B){
				if(b == null){
					r += ("/.");
				} else {
					r += ("/" + b.getType());
				}
			}
			r += "\n";
		}
		try{
			File file = new File(fileName);
			FileWriter writer = new FileWriter(file);
			writer.write(r, 0, r.length());
			writer.close();
			return file;
		} catch (Exception e){System.out.println("Failed to write chunk data"); return null;}

	}

	private void initWorld(int start){
		Block[][] chunk = new Block[WIDTH][HEIGHT];
		boolean treeLeft = false;
		for(int i = 0; i < WIDTH; i++){
			start += (int)(Math.random()*3 - 1.5);
			if(start >= WIDTH) start = WIDTH - 1;
			if(start <= 0) start = 1;
			chunk[i][start] = new Block(i*Block.WIDTH, start*Block.HEIGHT, "grass");
			chunk[i][0] = new Block(i*Block.WIDTH,0, "indestructable"); 
			for(int j = 1 ; j < start; j++){
				chunk[i][j] = new Block(i*Block.WIDTH, j*Block.HEIGHT, "stone");
			}
			if(Math.random() < .1 && !treeLeft){
				System.out.println("Planting tree");
				for(int k = start + 1; k < start + 7; k++){
					chunk[i][k] = new Block(i*Block.WIDTH, k*Block.HEIGHT, "wood");
				}
				treeLeft = true;
			} else {
				treeLeft = false;
			}
		}
		data = chunk;
	}

	public ArrayList<Tile> getTileInfo(){
		return tiles;
	}

	public int getHighestY(int x, int y){
		while(isOnMap(x, y)){
			y-=2;
		}	
		return y+2;
	}

	public boolean isOnMap(int x, int y){
		try{
			Block[][] b = getLoaded();
			if(b[(int)((x)/30)][(int)((y)/30)] != null) return true;
		} catch(Exception e) {return false;}
		return false;
	}

	public boolean isOnMap(GameObject obj){
		try{ 

			if(data[(int)((obj.getX() + obj.getWidth()/2)/30)][(int)((obj.getY()-1)/30)] != null) return true;

		} catch(Exception e) {return false;}
		return false;
	}

	public boolean isPlayerOnMap(GameObject obj){
		try{ 

			if(data[(int)((obj.getX() + obj.getWidth()-2)/30)][(int)((obj.getY()-2)/30)] != null) return true;
			if(data[(int)((obj.getX() + obj.getWidth()/2)/30)][(int)((obj.getY()-2)/30)] != null) return true;
			if(data[(int)((obj.getX())/30)][(int)((obj.getY()-2)/30)] != null) return true;

			if(data[(int)((obj.getX() + obj.getWidth()-2)/30)][(int)((obj.getY()-2 + obj.getHeight())/30)] != null) return true;
			if(data[(int)((obj.getX() + obj.getWidth()/2)/30)][(int)((obj.getY()-2 + obj.getHeight())/30)] != null) return true;
			if(data[(int)((obj.getX())/30)][(int)((obj.getY()-2 + obj.getHeight())/30)] != null) return true;

		} catch(Exception e) {return false;}
		return false;
	}


}