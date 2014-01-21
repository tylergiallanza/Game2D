package core;

public class Item {

	protected String type;
	protected int count;
	protected boolean stack;

	public Item() {
		type = "empty";
		count = 0;
		stack = false;
	}

	public Item(String t, int num) {
		type = t;
		count = num;
		stack = true;
	}

	public Item(Tile t) {
		type = t.getType();
		count = 1;
		stack = true;
	}

	public String toString() {
		return type+"-"+count;
	}

	public String getType() {
		return type;
	}

	public void add() {
		count++;
	}

	public boolean getStackable() {
		return stack;
	}

	public void remove() {
		count--;
		if (count<=0)
			type = "empty";
	}

}
