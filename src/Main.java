import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main{


	public static void main(String[] args) {
		GamePanel pane=new GamePanel();
		GameFrame frame=new GameFrame();
		frame.add(pane);
		pane.requestFocus();
	}

}
