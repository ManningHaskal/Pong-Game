import javax.swing.JFrame;

public class PongGameFrame extends JFrame{

	PongGameFrame()
	{
		PongGamePanel Panel = new PongGamePanel();
		this.add(Panel);
		this.setTitle("Pong Tests");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);


	}
}

