import java.util.ArrayList;
import java.awt.Color;
import java.util.Random;

public class Enemy extends Entity {

	Enemy(String type, Vec2d pos){
        super(type, pos, new Vec2d(0, 0));
	}

	public void pathFinding(Player target, Map mappa){
		double	distanza_target = getCenter().distance(target.getCenter()) - this.size/2 - target.size/2;

		Vec2d player_direction_versor = this.getCenter().getDirection(target.getCenter()).getVersor(0);
		Vec2d pd_rotated_versor = player_direction_versor.clone();

		double player_direction_weight = 2*(sigmoid(Math.pow(distanza_target/(attackArea + 100), 2)) - 0.5);
		double pd_rotated_weight = 1 - player_direction_weight;

		if (counter_clockwise)
			pd_rotated_versor.rotate90CC();
		else
			pd_rotated_versor.rotate90C();

		player_direction_versor.multiply(player_direction_weight*MAXSPEED);
		pd_rotated_versor.multiply(pd_rotated_weight*MAXSPEED);


		Vec2d player_opposition_vector = target.getCenter().getDirection(getCenter()).getVersor(0);
		double player_oppositioin_weight = 2*(sigmoid(Math.pow(attackArea/distanza_target, 2)) - 0.5);

		player_opposition_vector.multiply(player_oppositioin_weight*MAXSPEED);

		this.acc = player_direction_versor.clone();
		this.acc.add(pd_rotated_versor);
		this.acc.multiply((1-player_oppositioin_weight)*MAXSPEED);
		this.acc.add(player_opposition_vector);
	}
}