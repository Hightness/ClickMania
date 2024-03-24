public class Main{

	public static void main(String[] args) {
		GamePanel pane=new GamePanel();
		GameFrame frame=new GameFrame();
		pane.requestFocus();
		frame.add(pane);
	}

}
