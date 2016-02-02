import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyAdapter implements KeyListener {

	public KeyAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void keyPressed(KeyEvent key)
	{
		switch (key.getKeyChar()) 
		{
		case 'w':
			Main.camera.translate(0, 0, .1);
			break;
		case 's':
			Main.camera.translate(0, 0, -.1);
			break;
		case 'a':
			Main.camera.translate(.1, 0, 0);
			break;
		case 'd':
			Main.camera.translate(-.1, 0, 0);
			break;
		case 'e':
			Main.camera.translate(0, .1, 0);
			break;
		case 'q':
			Main.camera.translate(0, -.1, 0);
			break;
		case KeyEvent.VK_SPACE:
			System.out.println(Main.camera.x + " " + Main.camera.y + " " + Main.camera.z);
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
 
		default:
			break;
		} // end of switch
	} // end of keyPressed

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}