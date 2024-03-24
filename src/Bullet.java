import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class Bullet extends Entity{
    public Entity owner;

	Bullet(Vec2d pos, Vec2d speed, Entity owner){
        super("bullet", pos, speed);
        this.owner = owner;
	}

    public boolean checkCollisions(Map mappa, Player player){
        if (speed.getModule() < MINSPEED)return true;
        if ((getCenter().distance(player.getCenter()) <= size/2 + player.size/2)&& owner != player)
            return true;

		if(this.pos.y + this.size >= mappa.background.getHeight(null)*10 || this.pos.y <= 0 || this.pos.x <= 0 || this.pos.x + this.size >= mappa.background.getWidth(null)*10)
            return true;

        return false;
    }
}