package npc;
import physics.*;
import core.*;
import main.Driver;

public class Friendly extends Character {

	public Friendly(int x, int y, String type, String name) {
		super(x, y, type, name);
	}
	
	public Friendly(int x, int y) {
		super(x, y, "PNG", "friendly.png");
	}
	

}
