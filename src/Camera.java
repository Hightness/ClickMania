import java.awt.Color;

public class Camera extends Entity{

    Camera(Vec2d pos, Vec2d speed, Vec2d acc, double MAXSPEED, double MINSPEED){
        super(pos,speed,acc,0, MAXSPEED, MINSPEED, 0, Color.black);
    }

    public void follow(Vec2d target_center, double SCREEN_HEIGHT, double SCREEN_WIDTH){
        pos.x = target_center.x - SCREEN_WIDTH/2;
        pos.y = target_center.y - SCREEN_HEIGHT/2;
    }
}

