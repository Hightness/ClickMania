import java.util.ArrayList;
import java.awt.Color;
import java.util.Random;

public class Enemy extends Entity {

	Enemy(int type, Vec2d pos){
        super(type, pos, new Vec2d(0, 0));
	}

	public void pathFinding(Player target){
		double distanza_nemici = 0;
		double	distanza_target = getCenter().distance(target.getCenter()) - this.size/2 - target.size/2;
		double player_direction_weight = 2*(sigmoid(Math.pow(distanza_target/attackArea, 2)) - 0.5);
		double pd_rotated_weight = 1 - player_direction_weight;

		Vec2d player_direction_versor = this.getCenter().getDirection(target.getCenter()).getVersor(MINSPEED);

		Vec2d pd_rotated_versor = player_direction_versor.clone();
		if (counter_clockwise)
			pd_rotated_versor.rotate90CC();
		else
			pd_rotated_versor.rotate90C();

		player_direction_versor.multiply(player_direction_weight*MAXSPEED);
		pd_rotated_versor.multiply(pd_rotated_weight*MAXSPEED);

		player_direction_versor.add(pd_rotated_versor);
		this.acc.add(player_direction_versor);
		this.acc.normalize(MAXSPEED);
	}
}