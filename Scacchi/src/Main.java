import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main {      
        public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new Scacchiera();
				frame.setTitle("Scacchi v1.0");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
                                frame.setResizable(false);
			}
		});
	}
}
