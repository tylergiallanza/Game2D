package gui;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureLoader;

@SuppressWarnings("deprecation")
public class Button {
	int x, y, width, height;
	String text, token;
	public Button(int xIn, int yIn, int widthIn, int heightIn, String textIn, String tokenIn, boolean first) throws IOException {
		x = xIn;y = yIn; width = widthIn;height = heightIn;
		text = textIn;token = tokenIn;
		if(first) Handler.register(this);
		TextureLoader.getTexture("PNG", new FileInputStream(new File("button.png"))).bind();
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
		drawFont(x+(width/2)-(text.length()*4),y+(height/2)-15,text);
	}
	private static void drawFont(int x, int y, String text) {
		TrueTypeFont font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 24),false);
		font.drawString(x,y,text,Color.white);
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
}
