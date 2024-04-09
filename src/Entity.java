import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Entity{
    protected double cattrito, MAXSPEED, MINSPEED, attackArea;
	protected Color color;
	protected Random rand = new Random();
	protected String type;
	public boolean counter_clockwise = rand.nextBoolean();
	public int reloading, reloading_speed, repulsion_radius, knockback_intensity;
    public Vec2d pos, speed, acc;
    public double size;

	Entity(String type, Vec2d pos, Vec2d speed){
		String filename = "../conf/"+type+".properties";
		this.type = type;
		this.pos = pos.clone();
		this.speed = speed.clone();

		try{
			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream(filename);
			prop.load(fis);
 			fis.close();
			cattrito = Double.parseDouble(prop.getProperty("cattrito"));
			knockback_intensity = Integer.parseInt(prop.getProperty("knockback_intensity"));
			MAXSPEED = Double.parseDouble(prop.getProperty("MAXSPEED"));
			MINSPEED = Double.parseDouble(prop.getProperty("MINSPEED"));
			repulsion_radius = Integer.parseInt(prop.getProperty("repulsion_radius"));
			attackArea = Double.parseDouble(prop.getProperty("attackArea"));
			reloading = Integer.parseInt(prop.getProperty("reloading"));
			reloading_speed = Integer.parseInt(prop.getProperty("reloading_speed"));
			size = Double.parseDouble(prop.getProperty("size"));
			acc = new Vec2d(0, 0);
			String c = prop.getProperty("color");
			if(c.equals("red"))color = Color.red;
			if(c.equals("blue"))color = Color.blue;
			if(c.equals("green"))color = Color.green;
			if(c.equals("black"))color = Color.black;
			if(c.equals("yellow"))color = Color.yellow;
		}
		catch(IOException e){
			e.printStackTrace();
		}

    }

	public void fire(ArrayList<Bullet> bullets, Vec2d bullet_target, int MAX_BULLETS){
		if(reloading <= 0 && bullets.size() < MAX_BULLETS && getCenter().distance(bullet_target) < attackArea + size/2){
			Vec2d bullet_speed = getCenter().getDirection(bullet_target).getVersor(MINSPEED);
			Bullet player_bullet = new Bullet(getCenter(), new Vec2d(0, 0), this);
			bullet_speed.multiply(player_bullet.MAXSPEED);
			player_bullet.speed.add(bullet_speed);
			bullets.add(player_bullet);
			reloading = reloading_speed;
		}else reloading --;
	}

    public double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }

	public void checkCollisions(Player player, Map mappa){
		Vec2d new_dir = new Vec2d(0, 0);
		//Collision with enemies
		ArrayList<Enemy> enemies = mappa.checkCollisions(this,this.type);
		//if (enemies.size() > 0)this.counter_clockwise = !this.counter_clockwise;

		for(Enemy entity : enemies){
			//entity.counter_clockwise = this.counter_clockwise;
			double distanza_nemici = getCenter().distance(entity.getCenter()) - this.size/2 - entity.size/2;
			//double repulsion_vector_weight = Math.pow(eepulsion_radius/distanza_nemici, 2);
			double repulsion_vector_weight = 2*(sigmoid(Math.abs(repulsion_radius/distanza_nemici)) - 0.5);
			Vec2d repulsion_vector = entity.getCenter().getDirection(getCenter()).getVersor(0);
			//Vec2d entity_speed_dir = entity.speed.getVersor(MINSPEED);
			//entity_speed_dir.multiply(repulsion_vector_weight*MAXSPEED);
			repulsion_vector.multiply(repulsion_vector_weight*MAXSPEED);
			//repulsion_vector.add(entity_speed_dir);
			//repulsion_vector.normalize(MAXSPEED);
			new_dir.add(repulsion_vector);
		}

		double bottom_wall_opposition_weight = 2*(sigmoid(2000/Math.pow(this.getCenter().distance(new Vec2d(this.getCenter().x, mappa.height)), 2)) - 0.5);
		double top_wall_opposition_weight = 2*(sigmoid(2000/Math.pow(this.getCenter().distance(new Vec2d(this.getCenter().x, 0)), 2)) - 0.5);
		double left_wall_opposition_weight = 2*(sigmoid(2000/Math.pow(this.getCenter().distance(new Vec2d(0, this.getCenter().y)), 2)) - 0.5);
		double right_wall_opposition_weight = 2*(sigmoid(2000/Math.pow(this.getCenter().distance(new Vec2d(mappa.width, this.getCenter().y)), 2)) - 0.5);

		//if is player
		if (player == this){
			this.speed.add(new_dir);
			this.speed.normalize(MAXSPEED);


		}else{
			double	distanza_target = getCenter().distance(player.getCenter()) - this.size/2 - player.size/2;
			Vec2d player_opposition_vector = player.getCenter().getDirection(getCenter()).getVersor(0);
			double player_oppositioin_weight = 2*(sigmoid(Math.pow(attackArea/distanza_target, 2)) - 0.5);

			player_opposition_vector.multiply(player_oppositioin_weight*MAXSPEED);

			new_dir.multiply((1-player_oppositioin_weight)*MAXSPEED);
			this.acc.add(player_opposition_vector);
			this.acc.x = this.acc.x - right_wall_opposition_weight*MAXSPEED + left_wall_opposition_weight*MAXSPEED;
			this.acc.y = this.acc.y - bottom_wall_opposition_weight*MAXSPEED + top_wall_opposition_weight*MAXSPEED;
			this.acc.add(new_dir);
			this.acc.normalize(MAXSPEED);
		}

		//Collision with walls
		if(this.pos.y + this.size >= mappa.background.getHeight(null)*10){
			if(this.speed.y > 0)this.speed.y = -this.speed.y;
			if(this.acc.y > 0)this.acc.y = -this.acc.y;
			this.counter_clockwise = !this.counter_clockwise;
		}

		if(this.pos.y <= 0){
			if(this.speed.y < 0)this.speed.y = -this.speed.y;
			if(this.acc.y < 0)this.acc.y = -this.acc.y;
			this.counter_clockwise = !this.counter_clockwise;
		}

		if (this.pos.x <= 0){
			if(this.speed.x < 0)this.speed.x = -this.speed.x;
			if(this.acc.x < 0)this.acc.x = -this.acc.x;
			this.counter_clockwise = !this.counter_clockwise;
		}

		if(this.pos.x + this.size >= mappa.background.getWidth(null)*10){
			if(this.speed.x > 0)this.speed.x = -this.speed.x;
			if(this.acc.x > 0)this.acc.x = -this.acc.x;
			this.counter_clockwise = !this.counter_clockwise;
		}
	}

    public Vec2d getCenter(){
        return new Vec2d(this.pos.x + this.size/2,this.pos.y + this.size/2); 
    }

	public void move(){
		speed.add(acc);
        speed.normalize(MAXSPEED);

		Vec2d friction = speed.clone();
		friction.multiply(-cattrito);

		speed.add(friction);

		if (Math.abs(speed.x) < MINSPEED)speed.x = 0;
		if (Math.abs(speed.y) < MINSPEED)speed.y = 0;

		this.pos.x += speed.x;
		this.pos.y += speed.y;
	}
}