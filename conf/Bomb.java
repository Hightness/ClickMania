
public class Bomb extends Entity{
    public Entity owner;
    public int tick;

    Bomb(Vec2d pos, Vec2d speed, Entity owner){
        this.owner = owner;
        tick = 200;
        super("bomb", pos, speed);
    }

    public boolean checkCollisions(Map mappa, Player player){
        if (tick == 0)return true;
        tick = tick-1;

        if ((getCenter().distance(player.getCenter()) <= size/2 + player.size/2)&& owner != player)return true;

		if(this.pos.y + this.size >= mappa.background.getHeight(null)*10 || this.pos.y <= 0 || this.pos.x <= 0 || this.pos.x + this.size >= mappa.background.getWidth(null)*10)
            return true;

        return false;
    }
}