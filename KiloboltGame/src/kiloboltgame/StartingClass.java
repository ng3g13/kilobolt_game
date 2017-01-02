package kiloboltgame;

/*When adding new object
 * 0 - Create a class
 * 1 - Create objects in StartingClass with 'class' blueprints, declaring as variables and assigning them values in start()
 * 2 - In run() (gameloop) call the objects update() method.
 * 3 - Paint object in paint() method.
*/

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import kiloboltgame.framework.Animation;

public class StartingClass extends Applet implements Runnable, KeyListener{ //Runnable interface to implement a run() method for thread.

	static Background bg1 = new Background(0, 0);
	static Background bg2 = new Background(2160, 0);
	static Heliboy hb = new Heliboy(340, 360);
	static Heliboy hb2 = new Heliboy(700, 360);
	
	private Robot robot = new Robot();
	private Image image, currentSprite, character, character2, character3, characterDown, characterJumped, background, heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	private URL base;
	private Graphics second;
	private Animation anim, hanim; //anim for character animation and hanim for heliboy animation.
	
	@Override
	public void init() {
		//define applet size, background colour, applet title. Applet runs init instead of main.
		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true); //no need to click on applet.
		addKeyListener(this); //add the keylistener.
		Frame frame = (Frame)this.getParent().getParent();
		frame.setTitle("Q-Bot Alpha");
		try{
			base = getDocumentBase();
		}catch(Exception e){
			//TODO: handle exception.
		}
		
		//Image Setups
		character = getImage(base, "data/character.png");
		character2 = getImage(base, "data/character2.png");
		character3 = getImage(base, "data/character3.png");
		
		characterDown = getImage(base, "data/down.png");
		characterJumped = getImage(base, "data/jumped.png");
		
		background = getImage(base, "data/background.png");
		
		heliboy = getImage(base, "data/heliboy.png");
		heliboy2 = getImage(base, "data/heliboy2.png");
		heliboy3 = getImage(base, "data/heliboy3.png");
		heliboy4 = getImage(base, "data/heliboy4.png");
		heliboy5 = getImage(base, "data/heliboy5.png");
		
		anim = new Animation();
		anim.addFrame(character, 1250);
		anim.addFrame(character2, 50);		
		anim.addFrame(character3, 50);
		anim.addFrame(character2, 50);
		
		hanim = new Animation();
		hanim.addFrame(heliboy, 100);
		hanim.addFrame(heliboy2, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy5, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy2, 100);
		
		currentSprite = anim.getImage(); //get 'character' sprite.
	}
	
	@Override
	public void start() {
		Thread thread = new Thread(this); //connect the newly created run() to the thread.
		thread.start();
	}
	
	//Gameloop purpose - Update characters, Update environment, Repaint, Sleep(17) (~60fps)
	@Override
	public void run() {
		while(true){
			//tick()
			robot.update(); 
			if(robot.isJumped()){
				currentSprite = characterJumped;
			}else if(robot.isJumped() == false && robot.isDucked() == false){
				currentSprite = anim.getImage(); //animate() should change currentFrame.
			}
			
			ArrayList<Projectile> projectiles = robot.getProjectiles();
			for(int i = 0; i < projectiles.size(); i++){
				Projectile p = projectiles.get(i);
				if(p.isVisible() == true){
					p.update();
				}else{ //if projectile goes off screen, remove.
					projectiles.remove(i);
				}
			}
			
			bg1.update();
			bg2.update();
			hb.update();
			hb2.update();
			//render()
			
			animate();
			repaint(); 	//calls paint(), otherwise calls update(). I think it calls update, which calls paint inside.
			
			try{
				Thread.sleep(17);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void update(Graphics g){ //akin to tick()?
	//using double buffering technique.
		if(image == null){
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}
		
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);
		
		g.drawImage(image, 0, 0, this);
		
	}
	
	public void paint(Graphics g){ //akin to render()?
		//images are painted in order.
		g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
		g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
		
		ArrayList<Projectile> projectiles = robot.getProjectiles();
		for(int i = 0; i < projectiles.size(); i++){
			Projectile p = projectiles.get(i);
			g.setColor(Color.YELLOW);
			g.fillRect(p.getX(), p.getY(), 10, 5);
		}
		
		g.drawImage(currentSprite, robot.getCenterX() - 61, robot.getCenterY() - 63, this);
		g.drawImage(hanim.getImage(), hb.getCenterX() - 48, hb.getCenterY() - 48, this); //96x96 dimensions.
		g.drawImage(hanim.getImage(), hb2.getCenterX() - 48, hb2.getCenterY() - 48, this);
	}
	
	public void animate(){
		//duration values adds to animTime, which changes frames in turn depending on value.
		anim.update(10);
		hanim.update(50);
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			System.out.println("move up");
			break;
		case KeyEvent.VK_DOWN:
			currentSprite = characterDown;
			if(robot.isJumped() == false){
				robot.setDucked(true);
				robot.setSpeedX(0);
			}
			break;
		case KeyEvent.VK_LEFT:
			robot.moveLeft();
			robot.setMovingLeft(true);
			break;
		case KeyEvent.VK_RIGHT:
			robot.moveRight();
			robot.setMovingRight(true);
			break;
		case KeyEvent.VK_SPACE:
			robot.jump();
			break;
			
		case KeyEvent.VK_CONTROL:
			if(robot.isDucked() == false && robot.isJumped() == false){
				robot.shoot();
			}
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			System.out.println("stop move up");
			break;
		case KeyEvent.VK_DOWN:
			currentSprite = anim.getImage();
			robot.setDucked(false);
			break;
		case KeyEvent.VK_LEFT:
			robot.stopLeft();
			break;
		case KeyEvent.VK_RIGHT:
			robot.stopRight();
			break;
		case KeyEvent.VK_SPACE:
			break;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static Background getBg1(){
		return bg1;
	}
	
	public static Background getBg2(){
		return bg2;
	}
	
	
	
	
}
