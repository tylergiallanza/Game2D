package core;

import java.io.File;
import java.io.FileInputStream;

import main.Driver;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class GameObject {

        protected Texture texture;
        protected int xPos, yPos;
        protected int width, height;
        
        public GameObject() {
                texture = loadTexture("PNG", "wood.png");
                xPos = (int)Driver.getWidth()/2;
                yPos = (int)Driver.getHeight()/2;
                width = texture.getImageWidth();
                height = texture.getImageHeight();
        }
        
        public GameObject(int x, int y, String type, String name){
                texture = loadTexture(type, name);
                xPos = x;
                yPos = y;
                width = texture.getImageWidth();
                height = texture.getImageHeight();
        }
        
        public Texture getTexture(){
                return texture;
        }
        
        public int getX(){
                return xPos;
        }
        
        public int getY(){
                return yPos;
        }
        
        public void setY(int y){
                yPos = y;
        }
        
        public void setX(int x){
                xPos = x;
        }
        
        protected static Texture loadTexture(String type, String location){
                try{
                        return TextureLoader.getTexture(type, new FileInputStream(new File(location)));
                }catch(Exception e){e.printStackTrace();}
                return null;
        }
        
        public int getWidth(){
                return width;
        }
        
        public int getHeight(){
                return height;
        }
}

