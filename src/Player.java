import java.awt.Color;

public class Player extends Entity {

	Player(Vec2d pos, Vec2d speed, Vec2d acc, double size, double MAXSPEED, double MINSPEED, double attackArea){
        super(pos, speed, acc, size, MAXSPEED, MINSPEED, attackArea, Color.red, 0);
	}

	public void move(int up, int down, int left, int right){
		this.acc.x = right - left;
		this.acc.y = down - up;
		this.acc = this.acc.getVersor(MINSPEED);
        super.move();
	}
}