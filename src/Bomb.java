import java.util.ArrayList;

public class Bomb extends Entity{
    public Entity owner;
    public Vec2d destination;

    Bomb(Vec2d pos, Vec2d speed, Entity owner, Vec2d destination){
        super("bomb", pos, speed);
        this.destination = destination;
        if(owner != null) this.owner = owner;
    }

    public boolean checkCollisions(Map mappa, Player player){
        if (speed.getModule() < MINSPEED)return true;

        if (this.pos.x == destination.x && this.pos.y == destination.y){
            ArrayList<Entity> enemies = mappa.checkCollisions(this,this.type);
            for(Entity e : enemies) e.health --;
            if ((getCenter().distance(player.getCenter()) <= size/2 + player.size/2)&& owner != player)
                return true;
        }
        return this.pos.y + this.size >= mappa.height || this.pos.y <= 0 || this.pos.x <= 0 || this.pos.x + this.size >= mappa.width;
    }
}