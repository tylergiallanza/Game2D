package core;

public class Item
{
  protected String type;
  protected int count;
  protected boolean stack;
  
  public Item()
  {
    this.type = "empty";
    this.count = 0;
    this.stack = false;
  }
  
  public Item(String t, int num)
  {
    this.type = t;
    this.count = num;
    this.stack = true;
  }
  
  public Item(Tile t)
  {
    this.type = t.getType();
    this.count = 1;
    this.stack = true;
  }
  
  public String toString()
  {
    return this.type + "-" + this.count;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void add()
  {
    this.count += 1;
  }
  
  public boolean getStackable()
  {
    return this.stack;
  }
  
  public void remove()
  {
    this.count -= 1;
    if (this.count <= 0) {
      this.type = "empty";
    }
  }
}
