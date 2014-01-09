package physics;

import core.Tile;
import core.Block;
import main.Driver;

public class Explosion {

	
	public static void explode(int x, int y) {
		System.out.println("\n\n\n\n\n\n\n\n YAY \n" + x+"\n"+(y+""));
		//for(int i=-1;i<=1;i++)
			//for(int j=-1;j<=1;j++)
				//delete(x+i,y+j);
		delete(x,y,true);
		delete(x+1,y,false);
		delete(x,y+1,false);
		delete(x+1,y+1,false);
		delete(x+1,y-1,false);
		delete(x-1,y,false);
		delete(x,y-1,false);
		delete(x-1,y-1,false);
		delete(x-1,y+1,false);
		
	}
	
	private static boolean delete(int x, int y, boolean first) {
		try {
			System.out.println("DEL");
			System.out.println(Driver.getWorld().getLoaded()[x][y].getType());
			if (!first&&Driver.getWorld().getLoaded()[x][y].getType().equals("tnt"))
				Explosion.explode(x,y);
			if (!Driver.getWorld().getLoaded()[x][y].getType().equals("indestructable")){
				String t = Driver.getWorld().getLoaded()[x][y].getType();
				Driver.getWorld().getLoaded()[x][y] = null;
				Driver.getWorld().getTileInfo().add(new Tile(x*Block.WIDTH, y*Block.HEIGHT, t));
			}
		} catch (Exception e) {
			System.out.println("Failed to break block");
			return false;
		}
		return true;
	}

}
