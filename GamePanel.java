import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener{
	static final int UNIT_SIZE = 50;
	static final int SCREEN_WIDTH = 500;
	static final int SCREEN_HEIGHT = 500;
	static final int DELAY = 20;
	//Vec2d enemy1Center = new Vec2d(enemy1Pos.x+UNIT_SIZE, enemy1Pos.y + UNIT_SIZE);
	//double enemy1Size = 2*UNIT_SIZE;
	Bird bird ;
	boolean game_running, P_up, P_down, P_left, P_right;
	Image mappa;
	Camera camera;
	Vec2d mouse_coord;
	Timer timer = new Timer(DELAY,this);
	//Bullet bullet;
	Random rand = new Random();
	ArrayList<Enemy> enemies = new ArrayList<>();
	int num_enemies = 15;
	


	GamePanel(){
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.addMouseListener(new MyMouseListener());
		startGame();
	}

	public void startGame() {
		camera = new Camera(new Vec2d(0,0),new Vec2d(0,0),new Vec2d(0,0), SCREEN_WIDTH - UNIT_SIZE/2, SCREEN_HEIGHT - UNIT_SIZE/2);
		mappa = new ImageIcon("background.jpeg").getImage();
		bird = new Bird(new Vec2d((int)(UNIT_SIZE + 50), (int)(UNIT_SIZE + 50)) , new Vec2d(0,0), new Vec2d(0,0), UNIT_SIZE);

		//bullet new Bullet(owner)

		P_up = P_down = P_left = P_right = false;

		enemies.clear();

		for(int i = 0 ; i < num_enemies; i++){
			enemies.add(new Enemy(new Vec2d(rand.nextInt(SCREEN_WIDTH*3),rand.nextInt(SCREEN_HEIGHT*3)), new Vec2d(0,0), new Vec2d(0,0), UNIT_SIZE/2));
		}

		System.out.println("game started");
		game_running = true;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if(game_running) {

			if (bird.skipFrames == 0){
				bird.move(P_up, P_down, P_left, P_right);
				camera.move(bird.pos.x, bird.pos.y);
			}
			else{
				bird.move();
				camera.move(bird.pos.x, bird.pos.y);
				bird.skipFrames -= 1;
			}

			for(int i = 0 ; i < num_enemies; i++){
				enemies.get(i).pathFinding((int)bird.getCenter().x, (int)bird.getCenter().y, enemies);
			}

			checkCollisions();

			g.drawImage(mappa, -(int)camera.pos.x, -(int)camera.pos.y, mappa.getWidth(null)*10, mappa.getHeight(null)*10, this);

			for (Enemy enemy : enemies){
				g.setColor(Color.green);
				g.fillOval((int)enemy.pos.x - (int)camera.pos.x, (int)enemy.pos.y - (int)camera.pos.y, (int)enemy.size, (int)enemy.size);
			}		

			g.setColor(Color.red);
			g.fillOval((int)bird.pos.x - (int)camera.pos.x, (int)bird.pos.y - (int)camera.pos.y, (int)bird.size, (int)bird.size);
		}

		else {
			gameOver(g);
			timer.stop();			
		}
	}

	public void checkCollisions() {
		if(bird.pos.y + UNIT_SIZE >= SCREEN_HEIGHT*3 || bird.pos.y <= 0 || bird.pos.x <= 0 || bird.pos.x + UNIT_SIZE >= SCREEN_WIDTH*3){
			//game_running=false;
		}

		for (Enemy enemy : enemies){
			double distance = Math.sqrt((bird.getCenter().x-enemy.getCenter().x)*(bird.getCenter().x-enemy.getCenter().x)+(bird.getCenter().y-enemy.getCenter().y)*(bird.getCenter().y-enemy.getCenter().y));

			if(distance <= bird.size/2 + enemy.size/2 && bird.skipFrames == 0){
				bird.knockBack(enemy.speed.x*3, enemy.speed.y*3);
				//game_running=false;
			}
		}
	}
	
	public void gameOver(Graphics g) {
		//Score
		System.out.println("gameover");
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		//g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			mouse_coord.x = e.getX();
			mouse_coord.y = e.getY();
		}
	}
	
	public class MyKeyAdapter extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				if (!game_running)
					startGame();
				timer.start();
			}

			if (e.getKeyCode() == KeyEvent.VK_SHIFT)
				timer.stop();

			if (e.getKeyCode() == KeyEvent.VK_W)
				P_up = true;

			if (e.getKeyCode() == KeyEvent.VK_A)
				P_left = true;

			if (e.getKeyCode() == KeyEvent.VK_S)
				P_down = true;

			if (e.getKeyCode() == KeyEvent.VK_D)
				P_right = true;

		}

		@Override
		public void keyReleased(KeyEvent e){
			if (e.getKeyCode() == KeyEvent.VK_W)
				P_up = false;

			if (e.getKeyCode() == KeyEvent.VK_A)
				P_left = false;

			if (e.getKeyCode() == KeyEvent.VK_S)
				P_down = false;

			if (e.getKeyCode() == KeyEvent.VK_D)
				P_right = false;

		}
	}
}

