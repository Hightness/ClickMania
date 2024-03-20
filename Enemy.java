import java.util.ArrayList;
import java.util.Random;

public class Enemy extends GameObj {
	Random rand = new Random();
	public int skipFrames = 0;

	Enemy(Vec2d pos, Vec2d speed, Vec2d acc, double size){
        super(pos, speed, acc, size);
		MAXSPEED = 3;
		MINSPEED = 1;
	}

	public void pathFinding(int x, int y, ArrayList<Enemy> enemies){
		double dist_bias = 50;
		double distanza_target = distance(new Vec2d(x,y), getCenter())/dist_bias*3;
		double vel_player_dir_x = (x - pos.x)*(distanza_target);
		double vel_player_dir_y = (y - pos.y)*(distanza_target);
		double best_distance = 10000000;
		Enemy closest_enemy = new Enemy(new Vec2d(0,0),new Vec2d(0,0),new Vec2d(0,0),0);

		//per ogni nemico nx, controllo la distanza tra il nemico nx e il nemico corrente e aggiorna la velocita seguendo la direzione
		// del nemico (nx) piu' vicino al player
		//}
		for(Enemy enemy: enemies){
			if (enemy.pos.x == this.pos.x && enemy.pos.y == this.pos.y)continue;
			distanza_target = distance(enemy.getCenter(), new Vec2d(x,y));
			if((distance(enemy.getCenter(), getCenter()) < enemy.size/2 + this.size/2 + dist_bias ) && 
				(distanza_target < best_distance)){
				best_distance = distanza_target;
				closest_enemy = enemy;
			}
		}
		if(best_distance != 10000000 && best_distance < distance(new Vec2d(x,y), getCenter())){
				vel_player_dir_x += closest_enemy.speed.x;
				vel_player_dir_y += closest_enemy.speed.y;
		}

		double n = normalize(vel_player_dir_x, vel_player_dir_y);

		acc.x = vel_player_dir_x*n;
		acc.y = vel_player_dir_y*n;
		move();
	}
}