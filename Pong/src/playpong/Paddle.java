package playpong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Paddle {
	private Dimension size;
	private Point location;
	private Rectangle hitBox;
	private int score;
	private String name;
	
	public Paddle(int xLocation, int yLocation, String name){
		size = new Dimension(10,120);
		location = new Point(xLocation,yLocation);
		hitBox = new Rectangle(location.x, location.y + 1, 2, size.height - 1);
		score = 0;
		this.name = name;
	}
	
	
	
	public void drawPaddle(Graphics2D g){
		Color temp = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(location.x, location.y, size.width, size.height);
		g.setColor(temp);
		updateHitBox();
	}
	
	public void drawPaddle(Graphics2D g, int xLocation, int yLocation){
		Color temp = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(xLocation, yLocation, size.width, size.height);
		g.setColor(temp);
		location.x = xLocation;
		location.y = yLocation;
		updateHitBox();
	}
	
	public void drawPaddle(Graphics2D g, int xLocation){
		Color temp = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(xLocation, location.y, size.width, size.height);
		g.setColor(temp);
		location.x = xLocation;
		updateHitBox();
	}
	
	public void updateHitBox(){
		hitBox.x = location.x;
		hitBox.y = location.y;
	}
	
	public String getName(){
		return name;
	}
	
	public void increaseScore(int amount){
		score += amount;
	}
	
	public int getScore(){
		return score;
	}
	
	public void movePaddle(int amount, Graphics2D g){
		Rectangle bounds = g.getClipBounds();
		if(location.y <= 40) location.y = 41;
		else if(location.y + size.height >= (int)(bounds.getHeight() -10)) location.y = (int)(bounds.getHeight() - 11 - size.height);
		else {
			location.y += amount;
		}
		hitBox.y = location.y;
	}
	
	public Rectangle getHitBox(){
		return hitBox;
	}
	
	public void resetPaddle(Graphics2D g){
		Rectangle bounds = g.getClipBounds();
		location.y = (int) (bounds.getCenterY() - size.getHeight()/2);
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public int getY(){
		return location.y;
	}
	
	public int getX(){
		return location.x;
	}
	
	public int getHeight(){
		return size.height;
	}
	
	public int getWidth(){
		return size.width;
	}
	
}