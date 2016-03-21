import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;


@SuppressWarnings("serial")
public class Main extends JFrame
    implements GLEventListener
{
 
	private GLU glu;
	private GLCapabilities caps;
	private static GLCanvas canvas; 
	
	
	public static camera camera;
	public static boolean polygonMode = false;
	
	private static boolean haveData = false;
	private static Data currentdata = null;
	
	public static boolean showSettings;
	
	
	private JButton btnDraw;
	private JButton btnNewData;
	
	public static JPanel panContainer = new JPanel();
	private static JPanel panDraw = new JPanel();
	private JPanel panSettings = new JPanel();
	public static CardLayout cl = new CardLayout();
	

	//constructor
	public Main()
	{
		super("Main");
			
		panContainer.setLayout(cl);
		
		// Settings Window
		panSettings.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitel = new JLabel("TITEL");
		lblTitel.setHorizontalAlignment(SwingConstants.CENTER);
		panSettings.add(lblTitel, BorderLayout.NORTH);
		
		btnDraw = new JButton("draw");
		btnDraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cl.show(panContainer, "2");
			};
		});
			
		panSettings.add(btnDraw, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		panSettings.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(4, 2));
		
		JLabel lblCurrentData = new JLabel("current Data:	" + currentdata);
		panel.add(lblCurrentData);
		
		btnNewData = new JButton("new Data");
		btnNewData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser datafetcher = new JFileChooser(); 
				
				if(datafetcher.showOpenDialog(canvas) == JFileChooser.APPROVE_OPTION){
				    	
			            File fetcheddata = datafetcher.getSelectedFile().getAbsoluteFile();
			            		
			            try{
			            	System.out.println(fetcheddata);
			            		
			            	currentdata = new Data(fetcheddata);
			            	haveData = true;
			            	Thread.sleep(10);
			            }
			            catch (Exception e){
			                System.out.println(e);
			           	}     
				    }
			};
		});
		panel.add(btnNewData);
		
		JLabel lblTopLeftCorner = new JLabel("top left corner: ");
		panel.add(lblTopLeftCorner);
		
		JTextArea txtTopLeftCorner = new JTextArea();
		txtTopLeftCorner.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		panel.add(txtTopLeftCorner);
		
		JLabel lblWidth = new JLabel("width: ");
		panel.add(lblWidth);
		
		JTextArea txtWidth = new JTextArea();
		txtWidth.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		panel.add(txtWidth);
		
		JLabel lblHeight = new JLabel("height: ");
		panel.add(lblHeight);
		
		JTextArea txtHeight = new JTextArea();
		txtHeight.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		panel.add(txtHeight);
		
		
		// drawWindow
		caps = new GLCapabilities(GLProfile.getDefault());
		canvas = new GLCanvas(caps);
		camera = new camera(0,-2,-10);
		canvas.addGLEventListener(this);
		this.addKeyListener(new KeyAdapter());
		canvas.addMouseListener(new MouseAdapter());
		panDraw.add(canvas, BorderLayout.CENTER);
		canvas.requestFocusInWindow();		
		
		
		panContainer.add(panSettings, "1");
		panContainer.add(panDraw, "2");
		
		cl.show(panContainer, "1");
		
		this.add(panContainer);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.setSize(640, 480);
		this.setLocationRelativeTo(null);
		
		this.setVisible(true);
		
	} // end of constructor
	
	

	public static void main(String[] args)
	{
		new Main();
		final FPSAnimator animator = new FPSAnimator(canvas, 60,true); 
	    animator.start();   
	    
	} // end of main
 
// ------------------ OpenGL Part  ------------------ //
  
	@Override
	public void init(GLAutoDrawable drawable)
	{
		GL2 gl = drawable.getGL().getGL2();  
		glu = new GLU();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// enables depth_test (which vertex is behind the other...)
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		setupPointers(gl);
	} // end of init
 
	@Override
	public void display(GLAutoDrawable drawable)
	{
		System.out.println("display");
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		//camera
		gl.glMatrixMode( GLMatrixFunc.GL_PROJECTION );
        gl.glLoadIdentity();
		glu.gluPerspective(camera.fovy, camera.aspect, camera.near, camera.far);
        gl.glTranslated(camera.x, camera.y, camera.z);
        
        //Modeling
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		if(polygonMode){
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);			
		} else {
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
		}
		
		
		int indices[] = new int[] { 0, 2, 1,   1, 3, 2,   3, 4, 2,   0, 4, 2 };
		IntBuffer indicesBuf = Buffers.newDirectIntBuffer(indices.length);
			
		for (int i = 0; i < indices.length; i++)
			indicesBuf.put(indices[i]);
			indicesBuf.rewind();
			gl.glDrawElements(GL2.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, indicesBuf);
		
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
	{
		GL2 gl = drawable.getGL().getGL2();

		camera.aspect = (double)w/h;
		gl.glViewport(0, 0, w, h);
  
	} //end of reshape
  
	@Override
	public void dispose(GLAutoDrawable drawable) {
  	
	}
  
//------------------ Pointers Part  ------------------ //
 
	private void setupPointers(GL2 gl)
	{
		double vertices[] = new double[]	
				{  		3, -1, 3,   	-3, 0, 3,   	0, 1.5, 0, 
						-3, 2.5, -3, 	 3, 1.5, -3,
				};
		double colors[] = new double[]
				{  		1, 0, 0, 	1, 2, 0, 	0, 1, 0, 
						1, 0, 0, 	1, 0, 0, 	
				};
		
		DoubleBuffer tmpVerticesBuf = Buffers.newDirectDoubleBuffer(vertices.length);
		DoubleBuffer tmpColorsBuf = Buffers.newDirectDoubleBuffer(colors.length);
		
		for (int i = 0; i < vertices.length; i++) {
			tmpVerticesBuf.put(vertices[i]);
		}
		
		for (int j = 0; j < colors.length; j++) {
			tmpColorsBuf.put(colors[j]);
		}
			
		tmpVerticesBuf.rewind();
		tmpColorsBuf.rewind();
		
		
		gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, tmpVerticesBuf);
		gl.glColorPointer(3, GL.GL_FLOAT, 0, tmpColorsBuf);
		
	} // end of setupPointers	
}