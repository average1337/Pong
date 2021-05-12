package playpong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

public class Button {
private int width,height;
private Point location;
private String targetFrame,label;
private Color color;
private boolean hasBorder;
private Font font;
//private Font font;


public Button(int x, int y, int width,int height,Color color){
	location = new Point(x,y);
	this.width = width;
	this.height = height;
	this.color = color;
	targetFrame = "";
	hasBorder = false;
	label = "";
	font = null;
}

public Button(int x, int y, int width,int height,Color color, String label){
	location = new Point(x,y);
	this.width = width;
	this.height = height;
	this.color = color;
	targetFrame = "";
	hasBorder = false;
	this.label = label;
	font = null;
}

public Button(int x, int y, int width,int height,Color color, String label, String targetFrame){
	location = new Point(x,y);
	this.width = width;
	this.height = height;
	this.targetFrame = targetFrame;
	this.color = color;
	hasBorder = false;
	this.label = label;
	font = null;
}

public Button(int x, int y, int width,int height,Color color, boolean hasBorder){
	location = new Point(x,y);
	this.width = width;
	this.height = height;
	this.color = color;
	this.hasBorder = hasBorder;
	targetFrame = "";
	label = "";
	font = null;
}

public Button(int x, int y, int width,int height,Color color, boolean hasBorder,String targetFrame){
	location = new Point(x,y);
	this.width = width;
	this.height = height;
	this.color = color;
	this.hasBorder = hasBorder;
	this.targetFrame = targetFrame;
	label = "";
	font = null;
}

public Button(int x, int y, int width,int height,Color color, boolean hasBorder, String label, String targetFrame){
	location = new Point(x,y);
	this.width = width;
	this.height = height;
	this.color = color;
	this.hasBorder = hasBorder;
	this.targetFrame = targetFrame;
	this.label = label;
	font = null;
}

public Graphics2D drawButton(Graphics2D g){
	g.setColor(color);
	g.fillRect(location.x, location.y, width, height);
	if(hasBorder){
		g.setColor(Color.BLACK);
		g.drawRect(location.x, location.y, width, height);
	}
	g.setColor(Color.BLACK);
	if(!label.equals("")) drawCenteredText(g);
	return g;
}

public Graphics2D drawButton(Graphics2D g, int x, int y){
	g.setColor(color);
	g.fillRect(x, y, width, height);
	if(hasBorder){
		g.setColor(Color.BLACK);
		g.drawRect(location.x, location.y, width, height);
	}
	g.setColor(Color.BLACK);
	if(!label.equals("")) drawCenteredText(g);
	location = new Point(x,y);
	return g;
}

public Graphics2D drawCenteredText(Graphics2D g){
	if(font == null){
		font = getFont(g,92,label);
	}
	g.setFont(font);
	FontMetrics fm   = g.getFontMetrics(font);
	Rectangle2D rect = fm.getStringBounds(label, g);
	int textHeight = (int)(rect.getHeight()); 
	int textWidth  = (int)(rect.getWidth());
	int x = (width - textWidth) / 2;
	int y = (height - textHeight) /2 + fm.getAscent();
	g.drawString(label, x+location.x, y+location.y);
	return g;
}

public Font getFont(Graphics2D g, int size,String word){
	Font font = new Font("Comic Sans MS", Font.PLAIN, size);
	FontMetrics fm =g.getFontMetrics(font);
	Rectangle2D rect = fm.getStringBounds(word, g);
	if(rect.getWidth() > (.75*(double)width)) return getFont(g,size-1,word);
	if(rect.getHeight() > (.75*(double)height)) return getFont(g,size-1,word);
	return font;
}

public void setColor(Color color){
	this.color = color;
}

public void setBorder(boolean hasBorder){
	this.hasBorder = hasBorder;
}

public boolean getBorder(){
	return hasBorder;
}

public Color getColor(){
	return color;
}

public int getWidth(){
	return width;
}

public int getHeight(){
	return height;
}

public void setSize(Dimension size){
	width = (int) size.getWidth();
	height = (int) size.getHeight();
}

public boolean mouseInButton(Point mouse){
	if((mouse.x >= location.x && mouse.x <= location.x + width) && (mouse.y >= location.y && mouse.y <= location.y + height)){
		return true;
	}
	return false;
}

public String getAction(){
	return targetFrame;
}
}
