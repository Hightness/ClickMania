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
        if (owner == player){
            ArrayList<Entity> enemies = mappa.checkCollisions(this,this.type);
            for(Entity e : enemies) {
                e.health --;
            }

        }
        if ((getCenter().distance(player.getCenter()) <= size/2 + player.size/2)&& owner != player)
            return true;

        return this.pos.y + this.size >= mappa.height || this.pos.y <= 0 || this.pos.x <= 0 || this.pos.x + this.size >= mappa.width;
    }
}