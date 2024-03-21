import javax.swing.JFrame;

public class Vec2d {
    public double x;
    public double y;

	Vec2d(double x, double y){
        this.x = x;
        this.y = y;
	}

    public double distance(Vec2d vector){// trova distanza di x da y 
        return Math.sqrt((vector.x-this.x)*(vector.x-this.x) + (vector.y-this.y)*(vector.y-this.y));
    }

    public void flipDirection(){
        this.x = -this.x;
        this.y = -this.y;
    }

    public Vec2d getDirection(Vec2d target){
        return new Vec2d(target.x - this.x, target.y - this.y);
    }

    public void normalize(double MAXSPEED){
        double l = Math.sqrt(this.x*this.x + this.y*this.y);
        if (l > MAXSPEED){
            double n = MAXSPEED/l;
            this.x*=n;
            this.y*=n;
        }
    }

    public void rotate90CC(){
        this.x = -y;
        this.y = x;
    }

    public void rotate90C(){
        this.x = y;
        this.y = -x;
    }
}
