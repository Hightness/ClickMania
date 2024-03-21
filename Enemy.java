import java.util.ArrayList;
import java.util.Random;

public class Enemy extends GameObj {
	Random rand = new Random();

	Enemy(Vec2d pos, Vec2d speed, Vec2d acc, double size, double MAXSPEED, double MINSPEED){
        super(pos, speed, acc, size, MAXSPEED, MINSPEED);
	}

	public void pathFinding(Vec2d target_center, ArrayList<Enemy> enemies){

		double distanza_target = 0;
		double distanza_nemici = 0;
		double bias = 20;
		Vec2d vel_player_dir = pos.getDirection(target_center);
		Vec2d repulsion_vector;

		//per ogni nemico nx, controllo la distanza tra il nemico nx e il nemico corrente e aggiorna la velocita seguendo la direzione
		// del nemico (nx) piu' vicino al player
		//}
		for(Enemy enemy: enemies){
			if (enemy.pos.x == this.pos.x && enemy.pos.y == this.pos.y)continue;

			distanza_target = enemy.getCenter().distance(target_center);
			distanza_nemici = enemy.getCenter().distance(getCenter());

			repulsion_vector = getCenter().getDirection(enemy.getCenter());
			repulsion_vector.flipDirection();
			repulsion_vector.normalize(MAXSPEED);
			repulsion_vector.x *= Math.pow(bias/(distanza_nemici-bias),2);
			repulsion_vector.y *= Math.pow(bias/(distanza_nemici-bias),2);

			vel_player_dir.x += repulsion_vector.x;
			vel_player_dir.y += repulsion_vector.y;
			}

		vel_player_dir.normalize(MAXSPEED);
		acc.x = vel_player_dir.x;
		acc.y = vel_player_dir.y;
	}
}