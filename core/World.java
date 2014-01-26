package core;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import main.Driver;
import npc.Character;

public class World {

	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	private Block[][] data;
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private String folderName;
	public static final double IRON_GENERATION_COEFFICIENT = .018; //higher means more iron veins
	public static final double IRON_SPREAD_COEFFICIENT = 7.5; //higher means less spreading
	public static final int IRON_MAX_HEIGHT = (int)(HEIGHT * .5); //the max height at which iron will spawn
	public static final int CAVES_QUOTIENT = 30; //higher means less caves

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
		data = fromFile("maps/" + name);
	}

	public Block[][] getLoaded(){
		return data;
	}

	public void save(){
		toFile(data, "maps/" + folderName);
	}

	public static Block[][] fromFile(String fileName){
		System.out.println("loading block data from: " + fileName);
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
				readLine.close();
			}
			readMap.close();
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
			File saveFile = new File(fileName);
			FileWriter writer = new FileWriter(saveFile);
			writer.write(r, 0, r.length());
			writer.close();
			return saveFile;
		} catch (Exception e){e.printStackTrace(); System.out.println("Failed to write chunk data"); return null;}

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
				int height = 4 + (int)(Math.random()*4);
				for(int k = start + 1; k < start + height + 1; k++){
					chunk[i][k] = new Block(i*Block.WIDTH, k*Block.HEIGHT, "wood");
				}
				treeLeft = true;
			} else {
				treeLeft = false;
			}
		}
		int numCaves = (int)(Math.random() * WIDTH / CAVES_QUOTIENT);
		for(int q = 0; q <= numCaves; q++)
			chunk = this.createCave(chunk, (int)(Math.random() * WIDTH));
		chunk = this.addOres(chunk);
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

	public boolean isAnyOnMap(int x, int y){
		try{
			Block[][] b = getLoaded();
			if(b[(int)((x)/30)][(int)((y)/30)] != null) return true;
			for(Character c : Character.NPCs) {
				if(x > c.getX() && x < c.getX()+c.getWidth() && y > c.getY() && y < c.getY()+c.getHeight()) return true;
			}
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
	
	private Block[][] createCave(Block[][] map, int start){
		boolean direction = Math.random() > .5;
		int yVal = getHighestY(Block.WIDTH * start - 5, Driver.HEIGHT) / 30;
		while(yVal > 5){
			for(int i = -1; i <= 1; i++){
				if(i + start < 0 || i + start >= WIDTH) continue;
				for(int j = -1; j <= 1; j++){
					if(j + yVal < 0 || j + yVal >= HEIGHT) continue;
					map[i + start][j + yVal] = null;
				}
			}
			yVal--;
			start += (direction)? (int)(3 * Math.random()) : -(int)(3 * Math.random());
			if(Math.random() < .2) direction = !direction;
		}
		return map;
	}
	
	private Block[][] addOres (Block[][] input){
		double height = 0;
		for(Block[] B : input){
			height = 0;
			for(Block b : B){
				height++;
				if(height > IRON_MAX_HEIGHT) continue;
				if(b == null) continue;
				if(Math.random() < IRON_GENERATION_COEFFICIENT * ((HEIGHT-height)/(double)HEIGHT)){
					int numBlocks = (int)(IRON_SPREAD_COEFFICIENT * Math.random());
					for(int i = 0; i < numBlocks; i++){
						if(b == null) continue;
						if(b.getType().equals("stone"))
							b.changeTo("iron");
						b = getRandomAdjacentBlock(b, input);
					}
				}
			}
		}
		return input;
	}
	
	private Block getRandomAdjacentBlock(Block input, Block[][] chunk){
		double randomVal = Math.random();
		if(input.getX() <= 0 || input.getX() >= (WIDTH -1) * Block.WIDTH) return null;
		if(input.getY() <= 0 || input.getY() >= (HEIGHT-1) * Block.HEIGHT) return null;
		if(randomVal < .25) return chunk[input.getX()/Block.WIDTH][input.getY()/Block.HEIGHT + 1];
		if(randomVal < .50) return chunk[input.getX()/Block.WIDTH][input.getY()/Block.HEIGHT - 1];
		if(randomVal < .75) return chunk[input.getX()/Block.WIDTH + 1][input.getY()/Block.HEIGHT];
		return chunk[input.getX()/Block.WIDTH - 1][input.getY()/Block.HEIGHT];
	}


}