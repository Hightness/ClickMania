
public class Player extends Entity {

	Player(Vec2d pos, Vec2d speed, Vec2d acc, double size, double MAXSPEED, double MINSPEED){
        super(pos, speed, acc, size, MAXSPEED, MINSPEED);
	}

	public void move(int up, int down, int left, int right){
		this.acc.x = right - left;
		this.acc.y = down - up;
        super.move();
	}
}