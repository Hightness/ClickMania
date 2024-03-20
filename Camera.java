public class Camera extends GameObj{
	public int SCREEN_WIDTH;
	public int SCREEN_HEIGHT;

    Camera(Vec2d pos, Vec2d speed, Vec2d acc, int SCREEN_WIDTH, int SCREEN_HEIGHT){
        super(pos,speed,acc,0);
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
        this.SCREEN_WIDTH = SCREEN_WIDTH;
    }

    public void move(Vec2d target_center){
        pos.x = target_center.x - SCREEN_WIDTH/2;
        pos.y = target_center.y - SCREEN_HEIGHT/2;
    }
}

