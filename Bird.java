
public class Bird extends GameObj {

	Bird(Vec2d pos, Vec2d speed, Vec2d acc, double size){
        super(pos, speed, acc, size);
	}

	public void move(int up, int down, int left, int right){
		this.acc.x = right - left;
		this.acc.y = down - up;
        super.move();
	}
}