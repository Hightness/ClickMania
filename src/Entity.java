import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class Entity{
    protected double cattrito = 0.04;
    protected double MAXSPEED = 10;
    protected double MINSPEED = 0.4;
    public Vec2d pos, speed, acc;
    public double size;

	Entity(Vec2d pos, Vec2d speed, Vec2d acc, double size, double MAXSPEED, double MINSPEED){
        this.MAXSPEED = MAXSPEED;
        this.MINSPEED = MINSPEED;
        this.speed = speed;
        this.acc = acc;
        this.pos = pos;
        this.size = size;
    }

	public void checkCollisions(Player player, ArrayList<Enemy> entities, Image mappa) {
		double distanza_target;
		double repulsion_radius_ee = 70;
		double repulsion_radius_ep = 400;
		Vec2d repulsion_vector;
		Vec2d new_dir = new Vec2d(0,0);


		if (player.pos.x == this.pos.x && player.pos.y == this.pos.y)
			repulsion_radius_ee /= 4;

		//Collision with walls
		if(this.pos.y + this.size >= mappa.getHeight(null) || this.pos.y <= 0 || this.pos.x <= 0 || this.pos.x + this.size >= mappa.getWidth(null)){
			//game_running=false;
		}

		//Collision with enemies
		for(Enemy entity: entities){
			if (entity.pos.x == this.pos.x && entity.pos.y == this.pos.y)continue;

			distanza_target = entity.getCenter().distance(getCenter()) - entity.size/2 - this.size/2;

			repulsion_vector = entity.getCenter().getDirection(getCenter()).getVersor();

			repulsion_vector.multiply(Math.pow((repulsion_radius_ee/distanza_target),2));

			new_dir.add(repulsion_vector);
		}

		//Collision with player
		if (player.pos.x != this.pos.x || player.pos.y != this.pos.y){
			distanza_target = getCenter().distance(player.getCenter()) - this.size/2 - player.size/2;
			repulsion_vector = player.getCenter().getDirection(getCenter()).getVersor();
			repulsion_vector.multiply(Math.pow((repulsion_radius_ep/distanza_target),2));
			new_dir.add(repulsion_vector);
			this.acc = speed.clone();
			this.acc.add(new_dir);
		}
		else
			this.speed.add(new_dir);

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