import java.util.ArrayList;
import java.util.Random;

public class Enemy extends GameObj {
	Random rand = new Random();
	public int skipFrames = 0;

	Enemy(Vec2d pos, Vec2d speed, Vec2d acc, double size){
        super(pos, speed, acc, size);
		MAXSPEED = 6;
		MINSPEED = 1;
	}



	public void pathFinding(int x, int y, ArrayList<Enemy> enemies){

		double nspeedx = x - pos.x;
		double nspeedy = y - pos.y;
		double mincloseness = 100000;

		for(Enemy enemy: enemies){
			if (enemy.pos.x == pos.x && enemy.pos.y == pos.y)continue;

			Vec2d enemies_distance = new Vec2d(enemy.getCenter().x - getCenter().x, enemy.getCenter().y - getCenter().y);
			int bias = 40;
			double distance = Math.sqrt(enemies_distance.x*enemies_distance.x+enemies_distance.y*enemies_distance.y);

			if(distance - bias <= this.size/2 + enemy.size/2 && distance < mincloseness){

				if (pos.x - enemy.pos.x < 0){  //il nemico corrente si trova a sinistra di enemy
					nspeedx = -Math.random()*MAXSPEED;
				}else{
					nspeedx = Math.random()*MAXSPEED;
				}

				if (pos.y - enemy.pos.y < 0){  //il nemico corrente si trova sopra a enemy
					nspeedy = -Math.random()*MAXSPEED;
				}else{
					nspeedy = Math.random()*MAXSPEED;
				}

				mincloseness = distance;

				//deltax = -dy;
				//deltay = dx;

				//double delx = deltax + pos.x - x;
				//double dely = deltay + pos.y - y;

				//double d = Math.sqrt(delx*delx + dely*dely);

				//deltax = dy;
				//deltay = -dx;

				//delx = deltax + pos.x - x;
				//dely = deltay + pos.y - y;
				//double d2 = Math.sqrt(delx*delx + dely*dely);
				//if (d<d2){
					//deltax = -dy;
					//deltay = dx;
				//}
			}
		}
		double n = normalize(nspeedx, nspeedy);
		acc.x = nspeedx*n;
		acc.y = nspeedy*n;
		move();
	}
}