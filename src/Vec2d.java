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

    public Vec2d clone(){
        return new Vec2d(this.x, this.y);
    }

    public void multiply(double a){
        this.x *= a; 
        this.y *= a;
    }
    
    public void add(Vec2d vector){
        this.x += vector.x;
        this.y += vector.y;
    }

    public void flipDirection(){
        this.x = -this.x;
        this.y = -this.y;
    }

    public Vec2d getDirection(Vec2d target){
        return new Vec2d(target.x - this.x, target.y - this.y);
    }

    public double getModule(){
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public Vec2d getVersor(){
        double modulo = getModule();
        return new Vec2d(this.x/modulo, this.y/modulo);
    }

    public void normalize(double MAXSPEED){
        double l = getModule();
        if (l > MAXSPEED){
            double n = MAXSPEED/l;
            this.x*=n;
            this.y*=n;
        }
    }

    public void rotate90CC(){
        double temp = this.x;
        this.x = -this.y;
        this.y = temp;
    }

    public void rotate90C(){
        double temp = this.x;
        this.x = this.y;
        this.y = -temp;
    }
}
