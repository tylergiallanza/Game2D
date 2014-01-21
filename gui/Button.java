package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

public class Button {
	int x, y, width, height;
	String text, token;
	public Button(int xIn, int yIn, int widthIn, int heightIn, String textIn, String tokenIn, boolean first) throws IOException {
		x = xIn;y = yIn; width = widthIn;height = heightIn;
		text = textIn;token = tokenIn;
		if(first) Handler.register(this);
		if(getID().contains("MAP")) TextureLoader.getTexture("PNG", new FileInputStream(new File("worldButton.png")));
		else TextureLoader.getTexture("PNG", new FileInputStream(new File(getID()+".png"))).bind();
	}
	public void draw() {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x,y);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x+width,y);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex2f(x+width, y + height);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x,y+height);
		GL11.glEnd();
		
	}

	public boolean isClicked(int xIn, int yIn) {
		//System.out.println(((xIn>=x && xIn<=x+width) && (yIn>=y && yIn<=y+height)));
		return ((xIn>=x && xIn<=x+width) && (yIn>=y && yIn<=y+height));
	}
	
	public void act() {
		System.out.println(text + " was clicked.");
	}
	
	public String getID() {
		return token;
	}
	
	public String getText(){
		return text;
	}
}
