import java.util.ArrayList;
import java.util.Random;

public class Enemy extends GameObj {
	Random rand = new Random();

	Enemy(Vec2d pos, Vec2d speed, Vec2d acc, double size, double MAXSPEED, double MINSPEED){
        super(pos, speed, acc, size, MAXSPEED, MINSPEED);
	}

	public void pathFinding(GameObj target, ArrayList<Enemy> enemies){

		double distanza_nemici = 0;
		double attackArea = 100;
		double	distanza_target = getCenter().distance(target.getCenter()) - this.size/2 - target.size/2;

		if(distanza_target < 0)distanza_target = -1/distanza_target;

		Vec2d player_dir = getCenter().getDirection(target.getCenter()).getVersor();
		Vec2d player_repulsion = player_dir.clone().getVersor();
		player_repulsion.flipDirection();

		Vec2d pd_rotated = player_dir.clone().getVersor();
		pd_rotated.rotate90C();

		player_repulsion.x *= Math.pow(attackArea/distanza_target, 2);
		player_repulsion.y *= Math.pow(attackArea/distanza_target, 2);

		player_dir.x*= Math.pow(distanza_target/attackArea, 2);
		player_dir.y*= Math.pow(distanza_target/attackArea, 2);

		pd_rotated.x *= Math.pow(attackArea/distanza_target, 2);
		pd_rotated.y *= Math.pow(attackArea/distanza_target, 2);

		player_dir.x+= pd_rotated.x + player_repulsion.x;
		player_dir.y+= pd_rotated.y + player_repulsion.y;

		//Vec2d player_speed = new Vec2d(target.speed.x*Math.pow(attackArea/distanza_target, 4),
										//target.speed.y*Math.pow(attackArea/distanza_target, 4));

		//if(distanza_target > attackArea){
			//player_dir.x += player_speed.x;
			//player_dir.y += player_speed.y;
			//System.out.println(" not rotating");
		//}
		//else{
			//System.out.println("rotating");
			////Vec2d player_repulsion = target.getCenter().getDirection(getCenter());
			////player_repulsion.normalize(MAXSPEED);

			////player_repulsion.x *= Math.pow(attackArea/distanza_target,2);
			////player_repulsion.y *= Math.pow(attackArea/distanza_target,2);
			//player_dir.x = pd_rotated.x;
			//player_dir.y = pd_rotated.y;
		//}


		//knockBack(player_speed);
		Vec2d repulsion_vector;

		//per ogni nemico nx, controllo la distanza tra il nemico nx e il nemico corrente e aggiorna la velocita seguendo la direzione
		// del nemico (nx) piu' vicino al player
		//}
		for(Enemy enemy: enemies){
			if (enemy.pos.x == this.pos.x && enemy.pos.y == this.pos.y)continue;

			distanza_target = enemy.getCenter().distance(target.getCenter()) - enemy.size/2 - target.size/2;
			distanza_nemici = enemy.getCenter().distance(getCenter()) - enemy.size/2 - this.size/2;
			if(distanza_nemici < 0)distanza_nemici = -1/distanza_nemici;

			repulsion_vector = enemy.getCenter().getDirection(getCenter()).getVersor();

			repulsion_vector.x *= Math.pow(((enemy.size + this.size)/distanza_nemici),2);
			repulsion_vector.y *= Math.pow(((enemy.size + this.size)/distanza_nemici),2);

			player_dir.x += repulsion_vector.x;
			player_dir.y += repulsion_vector.y;
			}

		player_dir.normalize(MAXSPEED);
		acc.x = player_dir.x;
		acc.y = player_dir.y;
	}
}