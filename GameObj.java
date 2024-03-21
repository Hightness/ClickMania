public class GameObj {
    protected double cattrito = 0.04;
    protected double MAXSPEED = 10;
    protected double MINSPEED = 0.4;
    public Vec2d pos, speed, acc;
    public double size;

	GameObj(Vec2d pos, Vec2d speed, Vec2d acc, double size, double MAXSPEED, double MINSPEED){
        this.MAXSPEED = MAXSPEED;
        this.MINSPEED = MINSPEED;
        this.speed = speed;
        this.acc = acc;
        this.pos = pos;
        this.size = size;
    }

    public void knockBack(Vec2d hitter_speed){
        speed.x += hitter_speed.x;
        speed.y += hitter_speed.y;
    }

    public Vec2d getCenter(){
        return new Vec2d(this.pos.x + this.size/2,this.pos.y + this.size/2); 
    }

	public void move(){
        speed.x = speed.x + acc.x;
        speed.y = speed.y + acc.y;

        speed.normalize(MAXSPEED);

		Vec2d friction = new Vec2d(-cattrito * speed.x, -cattrito*speed.y);

        speed.x += friction.x;
        speed.y += friction.y;

		if (Math.abs(speed.x) < MINSPEED)speed.x = 0;
		if (Math.abs(speed.y) < MINSPEED)speed.y = 0;

		this.pos.x += speed.x;
		this.pos.y += speed.y;
	}
}