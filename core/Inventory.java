package core;

public class Inventory {
	
	public static final int INVENTORYSIZE = 20;
	private Item[] data = new Item[INVENTORYSIZE];
	
	public Inventory() {
		for(int i = 0; i < INVENTORYSIZE; i++)
			data[i] = new Item();
	}
	
	public String toString(){
		String r = "";
		for(Item i : data)
			r += i + ",";
		return r;
	}
	
	public boolean add(Item input){
		for(int i = 0; i < INVENTORYSIZE; i++){
			Item item = data[i];
			if(item.getType().equals("empty")){
				data[i] = input;
				return true;
			}
			if(item.getType().equals(input.getType()) && item.getStackable()){
				item.add();
				return true;
			}
		}
		return false;
	}
	
	public Item[] getData(){
		return data;
	}
	
	public void loadDefault(){
		data[0] = new Tool();
		data[1] = new Tool("pick");
	}

}
