import java.awt.Color;

public class Player extends Entity {
	public int upgrade_animation;

	Player(){
        super("player", new Vec2d(100, 100), new Vec2d(0, 0));
		upgrade_animation = 50;
	}

	public void move(int up, int down, int left, int right, Map map){
		this.acc.x = right - left;
		this.acc.y = down - up;
		this.acc.normalize(MAXSPEED/3);
		super.checkCollisions(this, map);
        super.move();
	}
}