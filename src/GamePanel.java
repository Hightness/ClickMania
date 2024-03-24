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

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class GamePanel extends JPanel implements ActionListener{

	
	Clip musicamainmenu;
	Player player;
	int P_up, P_down, P_left, P_right, DELAY, MAX_BULLETS, MAP_PADDING, num_enemy_archers, num_enemy_tanks;
	boolean game_running;
	Map map;
	Camera camera;
	Timer timer;
	Random rand = new Random();
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<Bullet> bullets = new ArrayList<>();
	ArrayList<Bomb> bombs = new ArrayList<>();
	
	private int currentImageIndex;
	private Image[] images;
	private int delayBetweenImages;
	private Timer timer1;

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
			enemies.clear();
			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream("../conf/game_settings.properties");
			prop.load(fis);
 			fis.close();
			DELAY = Integer.parseInt(prop.getProperty("DELAY"));
			MAX_BULLETS = Integer.parseInt(prop.getProperty("MAX_BULLETS"));
			MAP_PADDING = Integer.parseInt(prop.getProperty("MAP_PADDING"));
			map = new Map(new ImageIcon("../texture_packs/background.jpeg").getImage(), MAP_PADDING); 
			int nnum_enemy_archers = Integer.parseInt(prop.getProperty("num_enemy_archers")) - num_enemy_archers;
			int nnum_enemy_tanks = Integer.parseInt(prop.getProperty("num_enemy_tanks")) - num_enemy_tanks;
			timer = new Timer(DELAY, this);

			for(int i = 0 ; i < 10; i++)
				bombs.add(new Bomb(new Vec2d(rand.nextInt(map.width), rand.nextInt(map.height)), new Vec2d(0, 0), null));


			if (nnum_enemy_archers + nnum_enemy_tanks > 0){
				for(int i = 0 ; i < nnum_enemy_archers; i++){
					enemies.add(new Enemy("enemy_archer", new Vec2d(rand.nextInt(map.width), rand.nextInt(map.height))));
				}

				for(int i = 0 ; i < nnum_enemy_tanks; i++)
					enemies.add(new Enemy("enemy_tank", new Vec2d(rand.nextInt(map.width), rand.nextInt(map.height))));
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
            File file = new File("../sound/lightbringer.WAV");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            musicamainmenu = AudioSystem.getClip();
            musicamainmenu.open(audioInputStream);
        } catch (Exception e) {
			System.out.println("Error with playing sound.");
        }

		bullets.clear();
		bombs.clear();

		camera = new Camera(new Vec2d(0,0));


		game_running = false;
		P_up = P_down = P_left = P_right = 0;
		player = new Player();

		getData();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Disegna l'immagine corrente sul pannello
		if (images != null && images.length > 0) {
			Image currentImage = images[currentImageIndex];
			if (currentImage != null) {
				g.drawImage(currentImage, 50, 50, getWidth(), getHeight(), this);
			}
		}

		
		

		if(game_running) {
			player.checkCollisions(player, map);
			player.reloading = player.reloading - 1;
			player.move(P_up, P_down, P_left, P_right);
			camera.follow(player.getCenter(), getHeight(), getWidth());
			//map.update(player, 0);

			for(int i = 0 ; i < enemies.size(); i++){
				enemies.get(i).fire(bullets, player.getCenter(), MAX_BULLETS);
				enemies.get(i).pathFinding(player);
				enemies.get(i).checkCollisions(player, map);
			}

			for(int i = 0; i < enemies.size(); i++){
				map.delete(enemies.get(i));
				enemies.get(i).move();
				map.update(enemies.get(i));
			}

			int i = 0;
			while(i < bombs.size()){
				if(bombs.get(i).checkCollisions(map, player)){
					bombs.remove(i);
				}
				else{
					bombs.get(i).move();
					i++;
				}
			}


			i = 0;
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

			for (Bomb bomb : bombs){
				g.setColor(bomb.color);
				g.fillOval((int)bomb.pos.x - (int)camera.pos.x, (int)bomb.pos.y - (int)camera.pos.y, (int)bomb.size, (int)bomb.size);
			}		

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

		
		
		
		
		currentImageIndex = 0;
        delayBetweenImages = 50; // Tempo di visualizzazione predefinito per ogni immagine (2 secondi)

        // Carica le immagini
        images = new Image[50]; 
        images[0] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-001");
		images[1] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-002");
		images[2] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-003");
		images[3] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-004");
		images[4] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-005");
		images[5] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-006");
		images[6] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-007");
		images[7] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-008");
		images[8] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-009");
		images[9] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-010");
		images[10] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-011");
		images[11] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-012");
		images[12] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-013");
		images[13] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-014");
		images[14] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-015");
		images[15] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-016");
		images[16] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-017");
		images[17] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-018");
		images[18] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-019");
		images[19] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-020");
		images[20] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-021");
		images[21] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-022");
		images[22] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-023");
		images[23] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-024");
		images[24] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-025");
		images[25] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-026");
		images[26] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-027");
		images[27] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-028");
		images[28] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-029");
		images[29] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-030");
		images[30] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-031");
		images[31] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-032");
		images[32] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-033");
		images[33] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-034");
		images[34] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-035");
		images[35] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-036");
		images[36] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-037");
		images[37] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-038");
		images[38] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-039");
		images[39] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-040");
		images[40] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-041");
		images[41] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-042");
		images[42] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-043");
		images[43] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-044");
		images[44] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-045");
		images[45] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-046");
		images[46] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-047");
		images[47] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-048");
		images[48] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-049");
		images[49] = Toolkit.getDefaultToolkit().getImage("../texture_packs/ezgif-5-30d7aa09e1-png-split/ezgif-frame-050");
		
		// Avvia il timer per cambiare le immagini
		timer1 = new Timer(delayBetweenImages, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Timer action performed");
				currentImageIndex = (currentImageIndex + 1) % images.length;
				repaint(); // Ridisegna il pannello per visualizzare la nuova immagine
			}
		});
		timer1.start();
			

		
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

