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
			if(!isTypeInInventory(input.getType())){
				if(item.getType().equals("empty")){
					data[i] = input;
					return true;
				}
			} else {
				if(item.getType().equals(input.getType())){
					item.add();
					return true;
				}
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
		data[2] = new Tool("sword");
	}

	private boolean isTypeInInventory(String type){
		for(Item i : data){
			if(i == null) continue;
			if(i.getType().equals(type)) return true;
		}
		return false;
	}
	
	public int getNumThingsInBar(){
		int numThings = 0;
		for(Item i : data)
			if(i != null)
				numThings++;
		return numThings;
	}

}
