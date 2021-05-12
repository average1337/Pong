package playpong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class Graphics implements WindowListener, Runnable, KeyListener, MouseListener {
public static final String TITLE = "Pong";
public static final Dimension SIZE = new Dimension(2000,1200);

private Image imgBuffer;
private Frame frame;

private boolean isRunning, isDone;

private Paddle player1, player2;

private Ball ball;

private ArrayList<String> pressedKeys;

private String lastHit, curFrame;

private Button start, quit, yes, no, menu;

public Graphics(){
	frame = new Frame();
	frame.addWindowListener(this);
	frame.addKeyListener(this);
	frame.addMouseListener(this);
	
	frame.setSize(SIZE);
	frame.setTitle(TITLE);
	frame.setBackground(Color.BLACK);
	frame.setMinimumSize(new Dimension(500,300));
	
	isRunning = true;
	isDone = false;
	frame.setVisible(true);
	imgBuffer = frame.createImage(SIZE.width, SIZE.height);
	
	player1 = new Paddle(50,50,"Player 1");
	player2 = new Paddle(1290,50,"Player 2");
	
	start = new Button(220,330,175,75,Color.WHITE,"Start","Game");
	quit = new Button(220,580,175,75,Color.WHITE,"Quit","");
	
	yes = new Button(380,390,150,50,Color.GREEN,true,"Sure!","Game");
	no = new Button(160,390,150,50,Color.RED,true,"Nah","Menu");
	
	menu = new Button(300,300,150,75,Color.WHITE,"Menu","");
	
	ball = new Ball(665,340);
	
	pressedKeys = new ArrayList<>();
	
	lastHit = "";
	curFrame = "Menu";
	
	
	
}

public void drawGame(Graphics2D g){
	g.clearRect(0, 0, SIZE.width, SIZE.height);
	Rectangle bounds = frame.getBounds();
	
	player1.drawPaddle(g, 50);
	player2.drawPaddle(g,(int)(bounds.getWidth() - 50 - player2.getWidth()));
	ball.drawBall(g);
	
	if(player1.getHitBox().intersects(ball.getHitBox())) lastHit = player1.getName();
	if(player2.getHitBox().intersects(ball.getHitBox())) lastHit = player2.getName();
	
	if(ball.wentOut()){
		if(lastHit.equals(player1.getName())) player1.increaseScore(1);
		else if(lastHit.equals(player2.getName())) player2.increaseScore(1);
		ball.setWentOut(false);
		lastHit = "";
	}
	
	Font font = new Font("Javanese Text", Font.PLAIN, 50);
	g.setFont(font);
	g.setColor(Color.WHITE);
	
	FontMetrics fm   = g.getFontMetrics(font);
	Rectangle2D rect1 = fm.getStringBounds(String.valueOf(player1.getScore()), g);
	Rectangle2D rect2 = fm.getStringBounds(String.valueOf(player2.getScore()), g);
	
	g.drawString(String.valueOf(player1.getScore()), (int)(bounds.getWidth()/2 - 150) - (int)(rect1.getWidth()/2), 80);
	g.drawString(String.valueOf(player2.getScore()), (int)(bounds.getWidth()/2 + 150) - (int)(rect2.getWidth()/2), 80);
	
	int size = (int)((bounds.getHeight() - 50)/9);
	
	for(int i = 40; i <= bounds.getHeight() - size/2; i+=size*2){
		g.fillRect((int)(bounds.getWidth()/2 - 5), i, 10, size);
	}
	
	ball.updateBall(g, player1, player2);
	updateGame(g);
	
}

public void drawMenu(Graphics2D g){
	
	g.clearRect(0, 0, SIZE.width, SIZE.height);
	Rectangle bounds = g.getClipBounds();
	
	Font font = new Font("Javanese Text", Font.PLAIN, (int)((bounds.getWidth() + bounds.getHeight())/23));
	g.setFont(font);
	g.setColor(Color.WHITE);
	
	FontMetrics fm = g.getFontMetrics();
	String title = "Pong!";
	Rectangle2D stringBounds = fm.getStringBounds(title, g);
	
	g.drawString(title, (int)(bounds.getWidth()/2 - stringBounds.getWidth()/2), (int)(bounds.getHeight() * .3));
	start.drawButton(g, (int)(bounds.getWidth()/2 - start.getWidth()/2), (int)(bounds.getHeight() * .4));
	quit.drawButton(g, (int)(bounds.getWidth()/2 - quit.getWidth()/2), (int)(bounds.getHeight() * .7));
}

public void drawPause(Graphics2D g){
	g.clearRect(0, 0, SIZE.width, SIZE.height);
	Rectangle bounds = g.getClipBounds();
	
	Font font = new Font("Javanese Text", Font.PLAIN, (int)(bounds.getWidth()/15));
	g.setFont(font);
	
	g.setColor(Color.WHITE);
	
	FontMetrics fm = g.getFontMetrics();
	String display = "Paused";
	Rectangle2D stringBounds = fm.getStringBounds(display, g);
	
	g.drawString(display, (int)(bounds.getWidth()/2 - stringBounds.getWidth()/2), (int)(bounds.getHeight()/2));
	menu.drawButton(g, (int) (bounds.getWidth() - 160), (int) (bounds.getHeight()-85));
	updateGame(g);
	
}

public void drawWinner(Paddle player, Graphics2D g){
	g.clearRect(0, 0, SIZE.width, SIZE.height);
	Rectangle bounds = g.getClipBounds();
	
	String winner = player.getName() + " wins!";
	String playAgain = "Do you want to play again?";	
	
	Rectangle box = new Rectangle((int)(bounds.getWidth() * .4) , (int)(bounds.getHeight() * .4));
	
	box.x = (int) (bounds.getCenterX() - box.getWidth()/2);
	box.y = (int)(bounds.getCenterY() - box.getHeight()/2);
	
	g.setColor(Color.WHITE);
	g.fillRect((int)box.getX(), (int)box.getY(), (int)box.getWidth(), (int)box.getHeight());
	g.setColor(Color.BLACK);
	
	
	Font font = getWinnerFont(winner,g,100,box);
	g.setFont(font);
	FontMetrics fm = g.getFontMetrics();
	Rectangle2D stringBounds = fm.getStringBounds(winner, g);
	g.drawString(winner, (int)(((box.getWidth() - stringBounds.getWidth())/2) + box.getX()), (int)(box.y + stringBounds.getHeight() - fm.getAscent()));
	
	
	font = getWinnerFont(playAgain,g,100,box);
	g.setFont(font);
	fm = g.getFontMetrics();
	stringBounds = fm.getStringBounds(playAgain, g);
	g.drawString(playAgain, (int)(((box.getWidth() - stringBounds.getWidth())/2) + box.getX()), (int)(box.y + box.getHeight()/3 + stringBounds.getHeight() - fm.getAscent()));
	
	
	Dimension buttonSize = getWinnerSize(yes.getWidth(), yes.getHeight(), g, box);
	yes.setSize(buttonSize);
	yes.drawButton(g, (int) (((box.getWidth()/2 - yes.getWidth())/2) + box.getX()), (int) (box.getY() + (2*box.getHeight())/3));
	
	
	buttonSize = getWinnerSize(no.getWidth(), no.getHeight(), g, box);
	no.setSize(buttonSize);
	no.drawButton(g, (int) (((box.getWidth()/2 - no.getWidth())/2) + box.getX() + box.getWidth()/2), (int) (box.getY() + (2*box.getHeight())/3));
	//((box.getWidth() + 2*box.getX())/2) - (stringBounds.getWidth()/2)
}

public Font getWinnerFont(String display, Graphics2D g, int size, Rectangle bounds){
	Font font = new Font("Javanese Text", Font.PLAIN, size);
	g.setFont(font);
	
	FontMetrics fm = g.getFontMetrics();
	Rectangle2D stringBounds = fm.getStringBounds(display, g);
	
	if(stringBounds.getHeight() > bounds.getHeight()/3 || stringBounds.getWidth() > bounds.getWidth()){
		return getWinnerFont(display,g,size-1,bounds);
	}
	
	return font;
	
}

public Dimension getWinnerSize(int width, int height, Graphics2D g, Rectangle bounds){
	if(height > bounds.getHeight()/3 - 10 ){
		return getWinnerSize(width, height - 1, g, bounds);
	}
	if(width > bounds.getWidth()/2 - 10){
		return getWinnerSize(width - 1, height, g, bounds);
	}
	return new Dimension(width,height);
	
}

public void draw(){
	Graphics2D g = (Graphics2D)imgBuffer.getGraphics();
	g.setClip(0, 0, frame.getWidth(), frame.getHeight());
	
	if(curFrame.equals("Menu")) drawMenu(g);
	if(curFrame.equals("Game")) drawGame(g);
	if(curFrame.equals("Pause")) drawPause(g);
	//updateGame(g);
	
	if((player1.getScore() >= 5 || player2.getScore() >= 5) && Math.abs(player2.getScore() - player1.getScore())  >= 2) curFrame = "Winner";
	if(player1.getScore() >= 5 && curFrame.equals("Winner") && player1.getScore() - player2.getScore() >= 2) drawWinner(player1, g);
	if(player2.getScore() >= 5 && curFrame.equals("Winner") && player2.getScore() - player1.getScore() >= 2) drawWinner(player2, g);
	
	g = (Graphics2D)frame.getGraphics();
	g.drawImage(imgBuffer, 0, 0, SIZE.width, SIZE.height, 0, 0, SIZE.width, SIZE.height, null);
	
	
	
	g.dispose();
}

public void updateGame(Graphics2D g){
	
	if(curFrame.equals("Game")){
		if(pressedKeys.contains(String.valueOf(KeyEvent.VK_UP))) player2.movePaddle(-5, g);
		if(pressedKeys.contains(String.valueOf(KeyEvent.VK_DOWN))) player2.movePaddle(5, g);
		if(pressedKeys.contains(String.valueOf(KeyEvent.VK_W))) player1.movePaddle(-5, g);
		if(pressedKeys.contains(String.valueOf(KeyEvent.VK_S))) player1.movePaddle(5, g);
	}
	
	if(pressedKeys.contains(String.valueOf(KeyEvent.VK_P))) {
		if(curFrame.equals("Pause")) curFrame = "Game";
		else if(curFrame.equals("Game")) curFrame = "Pause";
		pressedKeys.remove(String.valueOf(KeyEvent.VK_P));
	}
	
}

public void resetGame(Graphics2D g){
	ball.resetBall(g);
	
	player1.resetPaddle(g);
	player2.resetPaddle(g);
	
	player1.setScore(0);
	player2.setScore(0);
	
}

@Override
public void run() {
	while(isRunning){
		draw();
		
		try{Thread.sleep(5);}
		catch(InterruptedException ie){ie.printStackTrace();}
	}
	isDone = true;
}



@Override
public void windowClosed(WindowEvent e) {
	while(true){
		if(isDone){
			System.exit(0);
		}
		try{Thread.sleep(50); }
		catch(InterruptedException ie){ie.printStackTrace();}
	}
}

@Override
public void windowClosing(WindowEvent e) {
	frame.setVisible(false);
	isRunning = false;
	frame.dispose();
}




@Override
public void windowDeactivated(WindowEvent e) {
	// TODO Auto-generated method stub
}

@Override
public void windowDeiconified(WindowEvent e) {
	
}

@Override
public void windowIconified(WindowEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowOpened(WindowEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowActivated(WindowEvent e) {
	// TODO Auto-generated method stub
	
}


@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	/*String k = String.valueOf(KeyEvent.VK_P);
	if(!pressedKeys.contains(k)){
		pressedKeys.add(k);
	}
	else pressedKeys.remove(k);*/
}


@Override
public void keyPressed(KeyEvent e) {
	// TODO Auto-generated method stub
	String key = String.valueOf(e.getKeyCode());
	if(!pressedKeys.contains(key) && !key.equals(String.valueOf(KeyEvent.VK_P))) pressedKeys.add(key);
	
}


@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	String key = String.valueOf(e.getKeyCode());
	
	if(!key.equals(String.valueOf(KeyEvent.VK_P))){
		pressedKeys.remove(String.valueOf(e.getKeyCode()));
	}
	
	else { 
		if(!pressedKeys.contains(key))pressedKeys.add(key);
		else pressedKeys.remove(key);
	}
	
}

@Override
public void mouseClicked(MouseEvent e) {
	// TODO Auto-generated method stub
	if(start.mouseInButton(e.getPoint()) && curFrame.equals("Menu")){
		Graphics2D g = (Graphics2D)(frame.getGraphics());
		g.setClip(0, 0, frame.getWidth(), frame.getHeight());
		resetGame(g);
		curFrame = start.getAction();
		frame.setResizable(false);
	}
	
	if(quit.mouseInButton(e.getPoint()) && curFrame.equals("Menu")){
		frame.setVisible(false);
		isRunning = false;
		frame.dispose();
	}
	
	if(yes.mouseInButton(e.getPoint()) && curFrame.equals("Winner")){
		Graphics2D g = (Graphics2D)(frame.getGraphics());
		g.setClip(0, 0, frame.getWidth(), frame.getHeight());
		resetGame(g);
		curFrame = "Game";
	}
	
	if(no.mouseInButton(e.getPoint()) && curFrame.equals("Winner")){
		Graphics2D g = (Graphics2D)(frame.getGraphics());
		g.setClip(0, 0, frame.getWidth(), frame.getHeight());
		resetGame(g);
		frame.setResizable(true);
		curFrame = "Menu";
	}
	
	if(menu.mouseInButton(e.getPoint()) && curFrame.equals("Pause")){
		Graphics2D g = (Graphics2D)(frame.getGraphics());
		g.setClip(0, 0, frame.getWidth(), frame.getHeight());
		resetGame(g);
		frame.setResizable(true);
		curFrame = "Menu";
	}
	
}

@Override
public void mousePressed(MouseEvent e) {
	// TODO Auto-generated method stub
	if(start.mouseInButton(e.getPoint()) && curFrame.equals("Menu")){
		start.setColor(start.getColor().darker());
	}
	
	if(quit.mouseInButton(e.getPoint()) && curFrame.equals("Menu")){
		quit.setColor(quit.getColor().darker());
	}
	
	if(yes.mouseInButton(e.getPoint()) && curFrame.equals("Winner")){
		yes.setColor(yes.getColor().darker());
	}
	
	if(no.mouseInButton(e.getPoint()) && curFrame.equals("Winner")){
		no.setColor(no.getColor().darker());
	}
	
	if(menu.mouseInButton(e.getPoint()) && curFrame.equals("Pause")){
		menu.setColor(menu.getColor().darker());
	}
	
}

@Override
public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub
	if(curFrame.equals("Menu")){
		start.setColor(Color.WHITE);
		quit.setColor(Color.WHITE);
	}
	
	if(curFrame.equals("Winner")){
		yes.setColor(Color.GREEN);
		no.setColor(Color.RED);
	}
	
	if(curFrame.equals("Pause")){
		menu.setColor(Color.WHITE);
	}
	
}

@Override
public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub
	
}


}