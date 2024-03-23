import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity {
	Random rand = new Random();

	Enemy(Vec2d pos, Vec2d speed, Vec2d acc, double size, double MAXSPEED, double MINSPEED, double attackArea){
        super(pos, speed, acc, size, MAXSPEED, MINSPEED, attackArea);
	}

	public void pathFinding(Player target, ArrayList<Enemy> enemies){

		double distanza_nemici = 0;
		double	distanza_target = getCenter().distance(target.getCenter()) - this.size/2 - target.size/2;

		Vec2d player_dir = this.getCenter().getDirection(target.getCenter()).getVersor(MINSPEED);

		Vec2d pd_rotated = player_dir.clone().getVersor(MINSPEED);
		//double random = rand.nextInt(2);
		//if (random == 1)
			//pd_rotated.rotate90C();
		//else
			pd_rotated.rotate90CC();

		player_dir.multiply(Math.pow(distanza_target/attackArea, 2));

		pd_rotated.multiply(attackArea/distanza_target);

		player_dir.add(pd_rotated);
		this.speed.add(player_dir);

		this.speed.normalize(MAXSPEED);

		acc.x = player_dir.x;
		acc.y = player_dir.y;
	}
}