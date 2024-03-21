import java.awt.*;
import java.awt.event.*;

public class MyKeyListener extends KeyAdapter{
	int P_up, P_down, P_left, P_right;

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
		//if (e.getKeyCode() == e.VK_ENTER){
				//startGame();
		//}

		//if (e.getKeyCode() == e.VK_SHIFT)
			//timer.stop();

		if (e.getKeyCode() == KeyEvent.VK_W)
			P_up = 1;

		if (e.getKeyCode() == KeyEvent.VK_A)
			P_left = 1;

		if (e.getKeyCode() == KeyEvent.VK_S)
			P_down = 1;

		if (e.getKeyCode() == KeyEvent.VK_D)
			P_right = 1;

	}

	@Override
	public void keyReleased(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_W)
			P_up = 0;

		if (e.getKeyCode() == KeyEvent.VK_A)
			P_left = 0;

		if (e.getKeyCode() == KeyEvent.VK_S)
			P_down = 0;

		if (e.getKeyCode() == KeyEvent.VK_D)
			P_right = 0;
	}
}
