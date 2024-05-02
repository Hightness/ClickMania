import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;



public class GamePanel extends JPanel implements ActionListener{
	int P_up, P_down, P_left, P_right;
	boolean game_running;
	Player player;
	Map map;
	Camera camera;
	Random rand = new Random();
	Timer timer;
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<Bullet> bullets = new ArrayList<>();
	ArrayList<Bomb> bombs = new ArrayList<>();
	private final Image[] menu_gif = new Image[50];
	Params parametri = new Params();

	GamePanel(){
		this.setPreferredSize(new Dimension(500 , 500));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.addMouseListener(new MyMouseListener());
		//initializeAudioFiles();
		for(int i = 1; i < 50; i++) menu_gif[i] = Toolkit.getDefaultToolkit().getImage("texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-0" + (int)i/10 + "" + (int)i%10+".png");
		startGame();}

	public void startGame() {
		enemies.clear();
		bullets.clear();
		bombs.clear();


		map = new Map(new ImageIcon("texture_packs/background.jpeg").getImage(), parametri.MAP_PADDING);
		camera = new Camera(new Vec2d(0,0));
		timer = new Timer(parametri.DELAY, this);
		player = new Player();

		//for(int i = 0 ; i < 10; i++)
			//bombs.add(new Bomb(new Vec2d(rand.nextInt(map.width), rand.nextInt(map.height)), new Vec2d(0, 0), null));

		for(int i = 0 ; i < parametri.num_enemy_archers; i++)
			enemies.add(new Enemy("enemy_archer", new Vec2d(rand.nextInt(map.width), rand.nextInt(map.height))));

		for(int i = 0 ; i < parametri.num_enemy_tanks; i++)
			enemies.add(new Enemy("enemy_tank", new Vec2d(rand.nextInt(map.width), rand.nextInt(map.height))));

		game_running = false;
		P_up = P_down = P_left = P_right = 0;
		timer.start();}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(game_running) {
			player.reloading = player.reloading - 1;
			if(player.upgrade_animation < 50){
				player.upgrade_animation++;
				player.color = Color.yellow;
			}else player.color = Color.red;

			player.move(P_up, P_down, P_left, P_right, map);
			camera.follow(player.getCenter(), getHeight(), getWidth());
			//..

			//moving enemies
			for(int i = 0 ; i < enemies.size(); i++){
				Enemy e = enemies.get(i);
				e.reloading = e.reloading - 1;
				e.fire(bullets, player.getCenter(), parametri.MAX_BULLETS);
				map.delete(e);
				e.pathFinding(player, map);
				e.checkCollisions(player, map);
				e.move();
				map.update(e);
			}
			//..

			int i = 0;
			while(i < bombs.size()){
				if(bombs.get(i).checkCollisions(map, player)) bombs.remove(i);
				else{
					bombs.get(i).move();
					i++;
				}
			}
			i = 0;
			while(i < bullets.size()){
				if(bullets.get(i).checkCollisions(map, player)) bullets.remove(i);
				else{
					bullets.get(i).move();
					i++;
				}
			}

			//disegna il background
			g.drawImage(map.background, -(int)camera.pos.x, -(int)camera.pos.y, map.width, map.height, this);

			for (Bomb bomb : bombs){
				g.setColor(bomb.color);
				g.fillOval((int)bomb.pos.x - (int)camera.pos.x, (int)bomb.pos.y - (int)camera.pos.y, (int)bomb.size, (int)bomb.size);
			}		

			for (Bullet bullet : bullets){
				if(bullet.owner == player)g.setColor(Color.yellow);
				else g.setColor(bullet.color);
				g.fillOval((int)bullet.pos.x - (int)camera.pos.x, (int)bullet.pos.y - (int)camera.pos.y, (int)bullet.size, (int)bullet.size);
			}		

			i = 0;
			while(i < enemies.size()){
				if(enemies.get(i).health <= 0){
					enemies.remove(i);
				}else{
					g.setColor(enemies.get(i).color);
					g.fillOval((int)enemies.get(i).pos.x - (int)camera.pos.x, (int)enemies.get(i).pos.y - (int)camera.pos.y, (int)enemies.get(i).size, (int)enemies.get(i).size);
					i++;
				}
			}

			g.setColor(player.color);
			g.fillOval((int)player.pos.x - (int)camera.pos.x, (int)player.pos.y - (int)camera.pos.y, (int)player.size, (int)player.size);
		}
		else {
			timer.stop(); // Resetto il
			mainMenu(g);
		}
	}

	public void mainMenu(Graphics g){
		if (parametri.mainMenuCounter >= 50) parametri.mainMenuCounter = 1;
		g.drawImage(menu_gif[parametri.mainMenuCounter],0,0, map.width, map.height, this);
		parametri.mainMenuCounter ++; // Tempo di visualizzazione predefinito per ogni immagine (2 secondi)
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public class MyMouseListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			Vec2d click_pos = new Vec2d(e.getX(), e.getY()); 
			click_pos.add(camera.pos);
			if(!game_running){
				game_running = true;
				timer.start();
			}else{
				player.fire(bullets, click_pos, parametri.MAX_BULLETS);
			}
		}
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				if (!game_running){
					game_running = true;
				}
				timer.start();
			}

			if (e.getKeyCode() == KeyEvent.VK_SHIFT)
				timer.stop();

			if (e.getKeyCode() == KeyEvent.VK_W)
				P_up = (int)player.MAXSPEED/3;

			if (e.getKeyCode() == KeyEvent.VK_A)
				P_left = (int)player.MAXSPEED/3;

			if (e.getKeyCode() == KeyEvent.VK_S)
				P_down = (int)player.MAXSPEED/3;

			if (e.getKeyCode() == KeyEvent.VK_D)
				P_right = (int)player.MAXSPEED/3;

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