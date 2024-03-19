
public class Bird extends GameObj {
	Bird(Vec2d pos, Vec2d speed, Vec2d acc, double size){
        super(pos, speed, acc, size);
	}

    public void fired; 

	public void move(boolean up, boolean down, boolean left, boolean right){
        //System.out.println(up+" " + down + " "+left+" "+right);
        int accy = 0, accx = 0;

		if (up)
            accy -= 1.0;

		if (down)
            accy += 1.0;

		if (left)
            accx -= 1.0;

		if (right)
            accx += 1.0;
        
        this.acc.x = accx;
        this.acc.y = accy;

        super.move();
		//camera.move((int)(bird.speedX*diagonal), (int)(bird.speedY*diagonal));
	}
}