package playpong;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ball {
	private int radius;
	private Point location;
	private int speed, maxSpeed;
	private double angle;
	private Rectangle hitBox;
	private int numOfHits;
	private long speedIncreaseStart;
	private boolean wentOut;
	private Image cookie;
	
	public Ball(int xLocation, int yLocation){
		radius = 20;
		location = new Point(xLocation,yLocation);
		speed = 5;
		maxSpeed = 15;
		angle =  getGoodAngle();
		hitBox = new Rectangle(xLocation,yLocation,radius,radius);
		speedIncreaseStart = 0;
		wentOut = false;
		//System.out.println(System.getProperty("user.dir") + "\\cookie.jpg");
		try {
			cookie = ImageIO.read(new File(System.getProperty("user.dir") + "\\thecookie.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawBall(Graphics2D g){
			Color temp = g.getColor();
			g.setColor(Color.WHITE);
			//g.drawImage(cookie, location.x, location.y, radius, radius, null);
			g.fillRect(location.x,location.y,radius,radius);
			g.setColor(temp);
	}
	
	private Font getProperFont(Graphics2D g, String label, int size){
		
		Rectangle bounds = g.getClipBounds();
		
		Font font = new Font("Javanese Text", Font.PLAIN, size);
		FontMetrics fm   = g.getFontMetrics(font);
		Rectangle2D stringBounds = fm.getStringBounds(label, g);
		
		if(stringBounds.getWidth() > bounds.getWidth() * .5) return getProperFont(g,label,size - 1);
		if(stringBounds.getHeight() > bounds.getHeight()  * .2) return getProperFont(g,label,size - 1);
		
		return font;
	}
	
	public void drawSpeedIncrease(Graphics2D g){
		Rectangle bounds = g.getClipBounds();
		g.setColor(Color.WHITE);
		Font temp = g.getFont();
		
		
		String display = "";
		if(speed < maxSpeed) display = "Speed Increased!";
		else display = "Max Speed!";
		
		Font font = getProperFont(g,display,92);
		g.setFont(font);
		
		FontMetrics fm   = g.getFontMetrics(font);
		
		Rectangle2D stringBounds = fm.getStringBounds(display, g);
		
		g.drawString(display, (int)((bounds.getWidth()/2) - (stringBounds.getWidth()/2)), (int)(bounds.getHeight() - fm.getAscent()));
		
		
		
		g.setFont(temp);
	}
	
	public void updateBall(Graphics2D g, Paddle player1, Paddle player2){
		Rectangle bounds = g.getClipBounds();
		
		if((System.currentTimeMillis() / 1000) - speedIncreaseStart <= 5 && speed <= maxSpeed){
			drawSpeedIncrease(g);
		}
		
		if(hitBox.intersects(player1.getHitBox())) angle = getHitAngle(player1);
		if(hitBox.intersects(player2.getHitBox())) angle = Math.PI - getHitAngle(player2);
		
		double xScale = Math.sin(angle + Math.PI/2);
		double yScale = Math.cos(angle  + Math.PI/2);
		double xVelocity =(speed * xScale);
		double yVelocity=(speed * yScale);
		 
		location.x += (int)xVelocity;
		location.y += (int)yVelocity;
		
		checkBounds(g,bounds);
		updateHitBox(location.x,location.y);
	}
	
	public void checkBounds(Graphics2D g, Rectangle bounds){
		if(hitBox.x <= 0 || hitBox.x + radius >= bounds.getWidth()) resetBall(g);
		if(hitBox.y <=35 || hitBox.y + radius >= bounds.getHeight()){
			if(hitBox.y <= 35) location.y = 36;
			if(hitBox.y + radius >= bounds.getHeight()) location.y = (int)(bounds.getHeight() - 5 - radius);
			angle = 2*Math.PI - angle;
		}
	}
	
	public void resetBall(Graphics2D g){
		Rectangle bounds = g.getClipBounds();
		location = new Point((int)(bounds.getWidth()/2),(int)(bounds.getHeight()/2));
		angle =  getGoodAngle();
		speed = 5;
		wentOut = true;
		numOfHits = 0;
	}
	
	public boolean wentOut(){
		return wentOut;
	}
	
	private double getGoodAngle(){
		int foo = (int)(Math.random() * 2);
		if(foo == 0){
			return (Math.random() * Math.PI/2) - Math.PI/4;
		}
		else{
			return (Math.random() * Math.PI/2) + (3*Math.PI)/4;
		}
	}
	
	
	private void updateHitBox(int xLocation, int yLocation){
		hitBox.x = xLocation;
		hitBox.y = yLocation;
	}
	
	public Rectangle getHitBox(){
		return hitBox;
	}
	
	public void setWentOut(boolean newBoolean){
		wentOut = newBoolean;
	}
	
	private double getHitAngle(Paddle playerPaddle){
		/*if(((System.currentTimeMillis() / 1000) % 60) - lastHit > 1){
			numOfHits++;
		}*/
		numOfHits++; 
		if(numOfHits % speed == 0 && numOfHits != 0){
			if(speed < maxSpeed){
				speed++;
				numOfHits = 0;
				speedIncreaseStart = System.currentTimeMillis() / 1000;
			}
		}
		//lastHit = ((System.currentTimeMillis() / 1000) % 60);
		
		Rectangle intersection = hitBox.intersection(playerPaddle.getHitBox());
		double intersectY = intersection.getCenterY();
		double relativeIntersectY = (playerPaddle.getY()+(playerPaddle.getHeight()/2)) - intersectY;
		double normalizedRelativeIntersectionY = (relativeIntersectY/(playerPaddle.getHeight()/2));
		double bounceAngle = normalizedRelativeIntersectionY * Math.PI/3;
		
		return bounceAngle;
	}
	
}
