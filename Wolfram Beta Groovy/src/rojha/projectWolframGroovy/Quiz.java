package rojha.projectWolframGroovy;

import java.awt.*;

import javax.swing.JFrame;

public class Quiz extends JFrame{
	static GraphicsConfiguration gc;
	Font titleFont = new Font("Courier", Font.PLAIN, 65);
	int titleLength = getFontMetrics(titleFont).stringWidth("Wolfram Beta Quiz");
	Quiz() {
		super("Wolfram Beta Quiz");
		this.setFocusable(true);
		this.requestFocus();
		setSize(1500, 600);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.repaint();
	}
	public void paint(Graphics g) {
		super.paint(g);
	}
}
