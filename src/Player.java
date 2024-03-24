import java.awt.Color;

public class Player extends Entity {

	Player(){
        super("player", new Vec2d(50, 50), new Vec2d(0, 0));
	}

	public void move(int up, int down, int left, int right){
		this.acc.x = right - left;
		this.acc.y = down - up;
		this.acc = this.acc.getVersor(MINSPEED);
        super.move();
	}
}