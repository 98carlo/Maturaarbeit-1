import java.awt.HeadlessException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Settings extends JFrame {
	

	public Settings(String arg0) throws HeadlessException {
		super(arg0);
		this.setSize(200, 200);
		
		//this.add(new JCheckBox());
		
		this.add(new JLabel("Hallo Welt"));		
		
	}
}
