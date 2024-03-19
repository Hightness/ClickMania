public class GameObj {
    protected double cattrito = 0.04;
    protected int MAXSPEED = 10;
    protected double MINSPEED = 3;
	public int skipFrames = 0;
    public Vec2d pos, speed, acc;
    public double size;

	GameObj(Vec2d pos, Vec2d speed, Vec2d acc, double size){
        this.speed = speed;
        this.acc = acc;
        this.pos = pos;
        this.size = size;
    }

    public void knockBack(double x, double y){
        speed.x = x;
        speed.y = y;
        skipFrames = 10;
    }

    public Vec2d getCenter(){
        return new Vec2d(this.pos.x + this.size/2,this.pos.y + this.size/2); 
    }

    public double normalize(double x, double y){
        double l = Math.sqrt(x*x + y*y);
        if (l > MAXSPEED)
            return  MAXSPEED/l;
        return 1;
    }
    public void normalize(){
        double l = Math.sqrt(speed.x*speed.x + speed.y*speed.y);
        if (l > MAXSPEED){
            double n = MAXSPEED/l;
            speed.x*=n;
            speed.y*=n;
        }
    }

	public void move(){
        speed.x = speed.x + acc.x;
        speed.y = speed.y + acc.y;

        normalize();

		Vec2d friction = new Vec2d(-cattrito * speed.x, -cattrito*speed.y);

        speed.x += friction.x;
        speed.y += friction.y;

		if (Math.abs(speed.x) < MINSPEED)speed.x = 0;
		if (Math.abs(speed.y) < MINSPEED)speed.y = 0;


		this.pos.x += speed.x;
		this.pos.y += speed.y;
	}
}