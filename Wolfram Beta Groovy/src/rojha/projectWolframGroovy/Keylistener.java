package rojha.projectWolframGroovy;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keylistener implements KeyListener{
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			Clicklistener.answer();
		}
	}
}
