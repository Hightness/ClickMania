import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class Entity{
	private final int MAX_BULLETS = 200;
    protected double cattrito = 0.04;
    protected double MAXSPEED = 10;
	protected double repulsion_radius = 30;
    protected double MINSPEED = 0.4;
	protected double attackArea = 300;
	public int reloading = 0;
    public Vec2d pos, speed, acc, pd_rotated_versor;
    public double size;

	Entity(Vec2d pos, Vec2d speed, Vec2d acc, double size, double MAXSPEED, double MINSPEED, double attackArea){
        this.MAXSPEED = MAXSPEED;
        this.attackArea = attackArea;
        this.MINSPEED = MINSPEED;
        this.speed = speed;
        this.acc = acc;
        this.pos = pos;
        this.size = size;
    }

	public void fire(ArrayList<Bullet> bullets, Vec2d bullet_target, double BULLET_SPEED){
		if(reloading <= 0 && bullets.size() < MAX_BULLETS && getCenter().distance(bullet_target) < attackArea + size/2){
			Vec2d bullet_speed = getCenter().getDirection(bullet_target).getVersor(MINSPEED);
			bullet_speed.multiply(BULLET_SPEED);
			Bullet player_bullet = new Bullet(getCenter(), bullet_speed, this);
			bullets.add(player_bullet);
			reloading = 10;
		}
	}

    public double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }

	public void checkCollisions(Player player, Map mappa, ArrayList<Enemy> enemies, int tag) {
		Vec2d new_dir = new Vec2d(0, 0);

		//Collision with enemies
		Set<Integer> tags = mappa.checkCollisions(this, tag);
		Iterator<Integer> it = tags.iterator();

		while (it.hasNext()){
			Enemy entity = enemies.get(it.next()-1);
			double distanza_nemici = getCenter().distance(entity.getCenter()) - this.size/2 - entity.size/2;
			double repulsion_vector_weight = 2*(sigmoid(Math.pow(repulsion_radius/distanza_nemici, 2)) - 0.5);
			Vec2d repulsion_vector = entity.getCenter().getDirection(getCenter()).getVersor(MINSPEED);
			repulsion_vector.multiply(repulsion_vector_weight*MAXSPEED);
			new_dir.add(repulsion_vector);
		}

		//Collision with player
		if (player.pos.x != this.pos.x || player.pos.y != this.pos.y){
			double distanza_player = getCenter().distance(player.getCenter()) - this.size/2 - player.size/2;
			double repulsion_player_weight = 2*(sigmoid(Math.pow((attackArea/distanza_player), 2)) - 0.5);
			Vec2d repulsion_vector = player.getCenter().getDirection(getCenter()).getVersor(MINSPEED);
			repulsion_vector.multiply(repulsion_player_weight*MAXSPEED);
			//new_dir.multiply((1-repulsion_player_weight)*MAXSPEED);
			new_dir.add(repulsion_vector);
			this.acc.multiply(0.8);
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
		}

		if(this.pos.y <= 0){
			if(this.speed.y < 0)this.speed.y = -this.speed.y;
			if(this.acc.y < 0)this.acc.y = -this.acc.y;
		}

		if (this.pos.x <= 0){
			if(this.speed.x < 0)this.speed.x = -this.speed.x;
			if(this.acc.x < 0)this.acc.x = -this.acc.x;
		}

		if(this.pos.x + this.size >= mappa.background.getWidth(null)*10){
			if(this.speed.x > 0)this.speed.x = -this.speed.x;
			if(this.acc.x > 0)this.acc.x = -this.acc.x;
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