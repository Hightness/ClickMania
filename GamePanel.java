
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener{
	static final int UNIT_SIZE = 50;
	static final int DELAY = 20;

	Bird bird ;
	int num_enemies = 15;
	boolean game_running;
	Image mappa;
	Camera camera;
	Vec2d mouse_coord;
	Timer timer = new Timer(DELAY,this);
	Random rand = new Random();
	ArrayList<Enemy> enemies = new ArrayList<>();
	


	GamePanel(){
		this.setPreferredSize(new Dimension(500 , 500));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyListener());
		this.addMouseListener(new MyMouseListener());
		startGame();
	}

	public void startGame() {
		double MINSPEED = 0.4;
		double MAXSPEED = rand.nextInt(6) + 4;
		double SIZE = rand.nextInt(UNIT_SIZE/2) + UNIT_SIZE;
		camera = new Camera(new Vec2d(0,0), new Vec2d(0,0), new Vec2d(0,0), MAXSPEED, MINSPEED);
		mappa = new ImageIcon("background.jpeg").getImage();

		bird = new Bird(new Vec2d((int)(UNIT_SIZE + 50), (int)(UNIT_SIZE + 50)) , new Vec2d(0,0), new Vec2d(0,0)
						, UNIT_SIZE, 10, MINSPEED);

		enemies.clear();
		for(int i = 0 ; i < num_enemies; i++){
			SIZE = rand.nextInt(UNIT_SIZE/2) + UNIT_SIZE/2;
			MAXSPEED = rand.nextInt(5) + 3;

			enemies.add(new Enemy(new Vec2d(rand.nextInt(mappa.getWidth(null)*10),rand.nextInt(mappa.getHeight(null)*10)), 
						new Vec2d(0,0), new Vec2d(0,0), SIZE, MAXSPEED, MINSPEED));
		}

		System.out.println("game starteed");
		game_running = true;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if(game_running) {

			//bird.move(P_up, P_down, P_left, P_right);
			camera.follow(bird.getCenter(), getHeight(), getWidth());
			bird.checkCollisions(enemies, mappa);
			//enemies.add(bird);

			for(int i = 0 ; i < num_enemies; i++){
				enemies.get(i).pathFinding(bird, enemies);
				enemies.get(i).checkCollisions(enemies, mappa);
				enemies.get(i).move();
			}

			//enemies.remove(enemies.size()-1);

			//disegna la mappa
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
			mouse_coord.x = e.getX();
			mouse_coord.y = e.getY();
		}
	}
	

}

