import java.util.ArrayList;
import java.util.Random;

public class Enemy extends GameObj {
	Random rand = new Random();

	Enemy(Vec2d pos, Vec2d speed, Vec2d acc, double size){
        super(pos, speed, acc, size);
		MAXSPEED = 6;
		MINSPEED = 0.3;
	}

	public void pathFinding(Vec2d target_center, ArrayList<Enemy> enemies){

		double distanza_target = 0;
		double distanza_nemici = 0;
		Vec2d vel_player_dir = new Vec2d(target_center.x - pos.x , target_center.y - pos.y);
		Vec2d repulsion_vector;
		//double best_distance = 10000000;
		//Enemy closest_enemy = new Enemy(new Vec2d(0,0),new Vec2d(0,0),new Vec2d(0,0),0);

		//per ogni nemico nx, controllo la distanza tra il nemico nx e il nemico corrente e aggiorna la velocita seguendo la direzione
		// del nemico (nx) piu' vicino al player
		//}
		for(Enemy enemy: enemies){
			if (enemy.pos.x == this.pos.x && enemy.pos.y == this.pos.y)continue;
			distanza_target = distance(enemy.getCenter(), target_center);
			distanza_nemici = distance(enemy.getCenter(), getCenter());

			repulsion_vector = new Vec2d(getCenter().x - enemy.getCenter().x, getCenter().y - enemy.getCenter().y);

			repulsion_vector.x *= normalize(repulsion_vector);
			repulsion_vector.y *= normalize(repulsion_vector);
			System.out.println("distanza nemici: " + distanza_nemici);

			vel_player_dir.x += (repulsion_vector.x)*Math.pow(100/(distanza_nemici+1),2);
			vel_player_dir.y += (repulsion_vector.y)*Math.pow(100/(distanza_nemici+1), 2);
			}

		double n = normalize(vel_player_dir);
		acc.x = vel_player_dir.x*n;
		acc.y = vel_player_dir.y*n;
	}
}