import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener{
	static final int UNIT_SIZE = 50;
	static final int DELAY = 20;

	Player player ;
	int P_up, P_down, P_left, P_right, num_enemies = 15;
	boolean game_running = false;
	Image mappa;
	Camera camera;
	Timer timer = new Timer(DELAY,this);
	Random rand = new Random();
	ArrayList<Enemy> enemies = new ArrayList<>();
	


	GamePanel(){
		this.setPreferredSize(new Dimension(500 , 500));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.addMouseListener(new MyMouseListener());
		startGame();
	}

	public void startGame() {
		double MINSPEED = 0.4;
		double MAXSPEED = rand.nextInt(6) + 4;
		double SIZE = rand.nextInt(UNIT_SIZE/2) + UNIT_SIZE;

		camera = new Camera(new Vec2d(0,0), new Vec2d(0,0), new Vec2d(0,0), MAXSPEED, MINSPEED);
		P_up = P_down = P_left = P_right = 0;
		mappa = new ImageIcon("../texture_packs/background.jpeg").getImage();

		player = new Player(new Vec2d((int)(UNIT_SIZE), (int)(UNIT_SIZE)) , new Vec2d(0,0), new Vec2d(0,0)
						, UNIT_SIZE, 10, MINSPEED);

		enemies.clear();
		for(int i = 0 ; i < num_enemies; i++){
			SIZE = rand.nextInt((int)UNIT_SIZE/2) + (int)UNIT_SIZE/2;
			MAXSPEED = rand.nextInt(5) + 3;
			enemies.add(new Enemy(new Vec2d(rand.nextInt(mappa.getWidth(null)*10), rand.nextInt(mappa.getHeight(null)*10)), 
						new Vec2d(0,0), new Vec2d(0,0), SIZE, MAXSPEED, MINSPEED));
		}

		System.out.println("game started");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void mainMenu(Graphics g){
		this.setBackground(Color.black);
		g.setColor(Color.red);
		g.fillRect(50,50, 50+getWidth()/10, 50+getHeight()/10);
	}

	public void draw(Graphics g) {
		if(game_running) {
			player.move(P_up, P_down, P_left, P_right);
			camera.follow(player.getCenter(), getHeight(), getWidth());
			player.checkCollisions(player, enemies, mappa);
			//enemies.add(bird);

			for(int i = 0 ; i < num_enemies; i++){
				enemies.get(i).pathFinding(player, enemies);
				enemies.get(i).checkCollisions(player, enemies, mappa);
				enemies.get(i).move();
			}

			//enemies.remove(enemies.size-1);

			//disegna la mappa
			g.drawImage(mappa, -(int)camera.pos.x, -(int)camera.pos.y, mappa.getWidth(null)*10, mappa.getHeight(null)*10, this);

			for (Enemy enemy : enemies){
				g.setColor(Color.green);
				g.fillOval((int)enemy.pos.x - (int)camera.pos.x, (int)enemy.pos.y - (int)camera.pos.y, (int)enemy.size, (int)enemy.size);
			}		

			g.setColor(Color.red);
			g.fillOval((int)player.pos.x - (int)camera.pos.x, (int)player.pos.y - (int)camera.pos.y, (int)player.size, (int)player.size);
		}
		else {
			mainMenu(g);
			timer.stop();			
		}
	}

	
	public void gameOver(Graphics g) {
		//Score
		System.out.println("gameover");
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (getWidth() - metrics2.stringWidth("Game Over"))/2, getHeight()/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getX() < 500 && e.getY() < 500){
				game_running = true;
				timer.start();
			}
		}
	}
	
	public class MyKeyAdapter extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				if (!game_running)
					game_running = true;
				timer.start();
			}

			if (e.getKeyCode() == KeyEvent.VK_SHIFT)
				timer.stop();

			if (e.getKeyCode() == KeyEvent.VK_W)
				P_up = 1;

			if (e.getKeyCode() == KeyEvent.VK_A)
				P_left = 1;

			if (e.getKeyCode() == KeyEvent.VK_S)
				P_down = 1;

			if (e.getKeyCode() == KeyEvent.VK_D)
				P_right = 1;

		}

		@Override
		public void keyReleased(KeyEvent e){
			if (e.getKeyCode() == KeyEvent.VK_W)
				P_up = 0;

			if (e.getKeyCode() == KeyEvent.VK_A)
				P_left = 0;

			if (e.getKeyCode() == KeyEvent.VK_S)
				P_down = 0;

			if (e.getKeyCode() == KeyEvent.VK_D)
				P_right = 0;
		}
	}
}
