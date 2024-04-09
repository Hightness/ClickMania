import javax.swing.JFrame;

public class GameFrame extends JFrame{

	GameFrame(){
		this.setTitle("ClickMania");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.pack();
		this.setSize(500,500);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}
}