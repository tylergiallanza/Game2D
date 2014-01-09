package core;

public class Inventory
{
  public static final int INVENTORYSIZE = 20;
  private Item[] data = new Item[20];
  
  public Inventory()
  {
    for (int i = 0; i < 20; i++) {
      this.data[i] = new Item();
    }
  }
  
  public String toString()
  {
    String r = "";
    for (Item i : this.data) {
      r = r + i + ",";
    }
    return r;
  }
  
  public boolean add(Item input)
  {
    for (int i = 0; i < 20; i++)
    {
      Item item = this.data[i];
      if (item.getType().equals("empty"))
      {
        this.data[i] = input;
        return true;
      }
      if ((item.getType().equals(input.getType())) && (item.getStackable()))
      {
        item.add();
        return true;
      }
    }
    return false;
  }
  
  public Item[] getData()
  {
    return this.data;
  }
  
  public void loadDefault()
  {
    this.data[0] = new Tool();
    this.data[1] = new Tool("pick");
  }
}
