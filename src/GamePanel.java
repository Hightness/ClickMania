import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.io.File;
import javafx.embed.swing.JFXPanel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class GamePanel extends JPanel implements ActionListener{

	Clip musicamainmenu;
	Player player;
	int P_up, P_down, P_left, P_right, DELAY, MAX_BULLETS, num_enemy_archers = 0, num_enemy_tanks = 0;
	boolean game_running;
	Map map;
	Camera camera;
	Timer timer;
	Random rand = new Random();
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<Bullet> bullets = new ArrayList<>();

	GamePanel(){
		this.setPreferredSize(new Dimension(500 , 500));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.addMouseListener(new MyMouseListener());
		startGame();
	}

	public void getData() {
		try{
			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream("C:\\Users\\aiman\\Desktop\\clickmania\\conf\\game_settings.properties");
			prop.load(fis);
 			fis.close();
			DELAY = Integer.parseInt(prop.getProperty("DELAY"));
			MAX_BULLETS = Integer.parseInt(prop.getProperty("MAX_BULLETS"));
			int nnum_enemy_archers = Integer.parseInt(prop.getProperty("num_enemy_archers")) - num_enemy_archers;
			int nnum_enemy_tanks = Integer.parseInt(prop.getProperty("num_enemy_tanks")) - num_enemy_tanks;

			if (nnum_enemy_archers + nnum_enemy_tanks > 0){
				for(int i = 0 ; i < nnum_enemy_archers; i++)
					enemies.add(new Enemy(1, new Vec2d(rand.nextInt(map.width), rand.nextInt(map.height))));

				for(int i = 0 ; i < nnum_enemy_tanks; i++)
					enemies.add(new Enemy(3, new Vec2d(rand.nextInt(map.width), rand.nextInt(map.height))));
			}

			if (nnum_enemy_archers < 0){
				for(int i = 0 ; i < -nnum_enemy_archers; i++)
					enemies.remove(enemies.size() - 1);
			}
			if (nnum_enemy_tanks < 0){
				for(int i = 0 ; i < -nnum_enemy_tanks; i++)
					enemies.remove(enemies.size() - 1);
			}

			num_enemy_archers += nnum_enemy_archers;
			num_enemy_tanks += nnum_enemy_tanks;
		}catch(IOException e){
			System.out.println("Error with loading properties.");
		}}

	public void startGame() {
		try {
            File file = new File("lightbringer.WAV");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            musicamainmenu = AudioSystem.getClip();
            musicamainmenu.open(audioInputStream);
        } catch (Exception e) {
			System.out.println("Error with playing sound.");
        }

		game_running = false;
		camera = new Camera(new Vec2d(0,0));
		P_up = P_down = P_left = P_right = 0;
		map = new Map(new ImageIcon("../texture_packs/background.jpeg").getImage()); 
		player = new Player();

		enemies.clear();
		bullets.clear();
		getData();
		timer = new Timer(DELAY, this);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		getData();

		if(game_running) {
			player.checkCollisions(player, map);
			player.move(P_up, P_down, P_left, P_right);
			camera.follow(player.getCenter(), getHeight(), getWidth());
			player.reloading = player.reloading - 1;
			//map.update(player, 0);

			for(int i = 0 ; i < enemies.size(); i++){
				enemies.get(i).fire(bullets, player.getCenter(), MAX_BULLETS);
				enemies.get(i).reloading = enemies.get(i).reloading - 1;
				enemies.get(i).pathFinding(player);
				enemies.get(i).checkCollisions(player, map);
			}

			for(int i = 0; i < enemies.size(); i++){
				map.delete(enemies.get(i));
				enemies.get(i).move();
				map.update(enemies.get(i));
			}

			int i = 0;
			while(i < bullets.size()){
				if(bullets.get(i).checkCollisions(map, player))
					bullets.remove(i);
				else{
					bullets.get(i).move();
					i++;
				}
			}

			//disegna la background
			g.drawImage(map.background, -(int)camera.pos.x, -(int)camera.pos.y, map.background.getWidth(null)*10, map.background.getHeight(null)*10, this);

			for (Bullet bullet : bullets){
				g.setColor(bullet.color);
				g.fillOval((int)bullet.pos.x - (int)camera.pos.x, (int)bullet.pos.y - (int)camera.pos.y, (int)bullet.size, (int)bullet.size);
			}		

			for (Enemy enemy : enemies){
				g.setColor(enemy.color);
				g.fillOval((int)enemy.pos.x - (int)camera.pos.x, (int)enemy.pos.y - (int)camera.pos.y, (int)enemy.size, (int)enemy.size);
			}		

			g.setColor(player.color);
			g.fillOval((int)player.pos.x - (int)camera.pos.x, (int)player.pos.y - (int)camera.pos.y, (int)player.size, (int)player.size);
		}
		else {
			mainMenu(g);
			timer.stop();			
		}
	}

	public void mainMenu(Graphics g){
		this.setBackground(Color.black);
		g.setColor(Color.red);
		g.fillRect(50,50, 50+getWidth()/10, 50+getHeight()/10);
		try {
			if(!musicamainmenu.isRunning())
            	musicamainmenu.start();
        } catch (Exception e) {
			System.out.println("Error with playing sound.");
        }
	}

	public void gameOver(Graphics g) {
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
			Vec2d click_pos = new Vec2d(e.getX(), e.getY()); 
			click_pos.add(camera.pos);

			if(!game_running){
				musicamainmenu.stop();
				game_running = true;
				timer.start();
			}else{
				player.fire(bullets, click_pos, MAX_BULLETS);
			}
		}
	}
	
	public class MyKeyAdapter extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				if (!game_running){
					musicamainmenu.stop();
					game_running = true;
				}

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

