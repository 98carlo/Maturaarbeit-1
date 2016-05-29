import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyAdapter implements KeyListener {

	public KeyAdapter() {

	}

	@Override
	public void keyPressed(KeyEvent key)
	{
		switch (key.getKeyCode())  
		{/*
		case KeyEvent.VK_W:
			Main.camera.translate(0, 0, .1);
			break;
		case KeyEvent.VK_S: 
			Main.camera.translate(0, 0, -.1);
			break;
		case KeyEvent.VK_A:
			Main.camera.translate(.1, 0, 0);
			break;
		case KeyEvent.VK_D:
			Main.camera.translate(-.1, 0, 0);
			break;
		case KeyEvent.VK_C:
			Main.camera.translate(0, .1, 0);
			break;
		case KeyEvent.VK_SPACE:
			Main.camera.translate(0, -.1, 0);
			break;*/
		case KeyEvent.VK_G:
			//System.out.println(Main.camera.x + " " + Main.camera.y + " " + Main.camera.z);
			if (Main.polygonMode){
				Main.polygonMode = false;
			} else {
				Main.polygonMode = true;
			};
			break;
		case KeyEvent.VK_ESCAPE:
			Main.cl.next(Main.panContainer);
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
