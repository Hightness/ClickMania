public class Camera extends GameObj{
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
    Camera(Vec2d pos, Vec2d speed, Vec2d acc, int SCREEN_WIDTH, int SCREEN_HEIGHT){
        super(pos,speed,acc,0);
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
        this.SCREEN_WIDTH = SCREEN_WIDTH;
    }

    public void move(double x, double y){
        pos.x = x - SCREEN_WIDTH/2;
        pos.y = y - SCREEN_HEIGHT/2;
    }
}

