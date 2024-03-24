import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class Entity{
    protected double cattrito, MAXSPEED, MINSPEED, attackArea;
	protected Color color;
	protected Random rand = new Random();
	protected int type;
	public boolean counter_clockwise = rand.nextBoolean();
	public int reloading, reloading_speed, repulsion_radius;
    public Vec2d pos, speed, acc;
    public double size;

	Entity(int type, Vec2d pos, Vec2d speed){
		switch(type){
			case 0:
				player(pos, speed);
				break;
			case 1:
				enemy_archer(pos, speed);
				break;
			case 2:
				bullet(pos, speed);
				break;
			case 3:
				enemy_tank(pos, speed);
				break;
			case 4:
				camera(pos);
				break;
		}
    }

	private void enemy_tank(Vec2d pos, Vec2d speed){
		type = 3;
		cattrito = 0.1;
		MAXSPEED = 3;
		repulsion_radius = 3;
		MINSPEED = 0.1;
		attackArea = 50;
		reloading = 0;
		reloading_speed = 100;
		color = Color.blue;
		this.pos = pos.clone();
		this.speed = speed.clone();
		acc = new Vec2d(0, 0);
		size = 40;
	};

	private void enemy_archer(Vec2d pos, Vec2d speed){
		type = 1;
		cattrito = 0.1;
		MAXSPEED = 6;
		repulsion_radius = 10;
		MINSPEED = 0.1;
		attackArea = 250;
		reloading = 0;
		reloading_speed = 10;
		color = Color.green;
		this.pos = pos.clone();
		this.speed = speed.clone();
		acc = new Vec2d(0, 0);
		size = 30;
	};

	private void player(Vec2d pos, Vec2d speed){
		type = reloading = 0;
		cattrito = 0.1;
		MAXSPEED = 10;
		repulsion_radius = 1;
		MINSPEED = 0.4;
		attackArea = 500;
		reloading_speed = 5;
		color = Color.red;
		this.pos = pos.clone();
		this.speed = speed.clone();
		acc = new Vec2d(0, 0);
		size = 50;
	};

	private void camera(Vec2d pos){
		type = 4;
		repulsion_radius = 0;
		cattrito = MAXSPEED = MINSPEED = attackArea = reloading = reloading_speed = 0;
		color = Color.black;
		this.pos = pos.clone();
		speed = new Vec2d(0, 0);
		acc = new Vec2d(0, 0);
		size = 0;
	};

	private void bullet(Vec2d pos, Vec2d speed){
		type = 2;
		cattrito = 0.005;
		MAXSPEED = 27;
		MINSPEED = 2;
		repulsion_radius = 0;
		attackArea = reloading = reloading_speed = 0;
		color = Color.black;
		this.pos = pos.clone();
		this.speed = speed.clone();
		acc = new Vec2d(0, 0);
		size = 10;
	};

	public void fire(ArrayList<Bullet> bullets, Vec2d bullet_target, int MAX_BULLETS){
		if(reloading <= 0 && bullets.size() < MAX_BULLETS && getCenter().distance(bullet_target) < attackArea + size/2){
			Vec2d bullet_speed = getCenter().getDirection(bullet_target).getVersor(MINSPEED);
			bullet_speed.multiply(MAXSPEED);
			Bullet player_bullet = new Bullet(getCenter(), bullet_speed, this);
			bullets.add(player_bullet);
			reloading = reloading_speed;
		}
	}

    public double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }

	public void checkCollisions(Player player, Map mappa) {
		Vec2d new_dir = new Vec2d(0, 0);

		//Collision with enemies
		ArrayList<Enemy> enemies = mappa.checkCollisions(this);

		for(Enemy entity : enemies){
			if (entity.counter_clockwise != this.counter_clockwise)entity.counter_clockwise = this.counter_clockwise;
			double distanza_nemici = getCenter().distance(entity.getCenter()) - this.size/2 - entity.size/2;
			//double repulsion_vector_weight = Math.pow(eepulsion_radius/distanza_nemici, 2);
			double repulsion_vector_weight = 2*(sigmoid(Math.pow((repulsion_radius/distanza_nemici), 2)) - 0.5);
			Vec2d repulsion_vector = entity.getCenter().getDirection(getCenter()).getVersor(MINSPEED);
			Vec2d entity_speed_dir = entity.speed.getVersor(MINSPEED);
			entity_speed_dir.multiply(repulsion_vector_weight*MAXSPEED/2);
			repulsion_vector.multiply(repulsion_vector_weight*MAXSPEED/2);
			repulsion_vector.add(entity_speed_dir);
			new_dir.add(repulsion_vector);
		}

		//Collision with player
		if (type != 0){
			double distanza_player = getCenter().distance(player.getCenter()) - this.size/2 - player.size/2;
			double repulsion_player_weight = 2*(sigmoid(Math.pow((attackArea/distanza_player), 2)) - 0.5);
			Vec2d repulsion_vector = player.getCenter().getDirection(getCenter()).getVersor(MINSPEED);
			new_dir.multiply(1 - repulsion_player_weight);
			repulsion_vector.multiply(repulsion_player_weight*MAXSPEED);
			new_dir.add(repulsion_vector);
			this.acc.add(new_dir);
			this.acc.normalize(MAXSPEED);
		}else{
			this.speed.add(new_dir);
			this.speed.normalize(MAXSPEED);
		}
		//Collision with walls
		if(this.pos.y + this.size >= mappa.background.getHeight(null)*10){
			if(this.speed.y > 0)this.speed.y = -this.speed.y;
			if(this.acc.y > 0)this.acc.y = -this.acc.y;
			if(this.pos.x > player.pos.x)this.counter_clockwise = true;
			if(this.pos.x < player.pos.x) this.counter_clockwise = false;
		}

		if(this.pos.y <= 0){
			if(this.speed.y < 0)this.speed.y = -this.speed.y;
			if(this.acc.y < 0)this.acc.y = -this.acc.y;
			if(this.pos.x > player.pos.x)this.counter_clockwise = false;
			if(this.pos.x < player.pos.x) this.counter_clockwise = true;
		}

		if (this.pos.x <= 0){
			if(this.speed.x < 0)this.speed.x = -this.speed.x;
			if(this.acc.x < 0)this.acc.x = -this.acc.x;
			if(this.pos.y > player.pos.y)this.counter_clockwise = true;
			if(this.pos.x < player.pos.x) this.counter_clockwise = false;
		}

		if(this.pos.x + this.size >= mappa.background.getWidth(null)*10){
			if(this.speed.x > 0)this.speed.x = -this.speed.x;
			if(this.acc.x > 0)this.acc.x = -this.acc.x;
			if(this.pos.x > player.pos.x)this.counter_clockwise = false;
			if(this.pos.x < player.pos.x) this.counter_clockwise = true;
		}
	}

    public Vec2d getCenter(){
        return new Vec2d(this.pos.x + this.size/2,this.pos.y + this.size/2); 
    }

	public void move(){
        speed.x = speed.x + acc.x;
        speed.y = speed.y + acc.y;

        speed.normalize(MAXSPEED);

		Vec2d friction = new Vec2d(-cattrito * speed.x, -cattrito*speed.y);

        speed.x += friction.x;
        speed.y += friction.y;

		if (Math.abs(speed.x) < MINSPEED)speed.x = 0;
		if (Math.abs(speed.y) < MINSPEED)speed.y = 0;

		this.pos.x += speed.x;
		this.pos.y += speed.y;
	}
}