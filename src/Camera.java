import java.awt.Color;

public class Camera extends Entity{

    Camera(Vec2d pos){
        super("camera", pos, new Vec2d(0, 0));
    }

    public void follow(Vec2d target_center, double SCREEN_HEIGHT, double SCREEN_WIDTH){
        pos.x = target_center.x - SCREEN_WIDTH/2;
        pos.y = target_center.y - SCREEN_HEIGHT/2;
    }
}

