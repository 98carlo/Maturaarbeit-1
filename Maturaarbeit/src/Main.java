import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

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
	private static Data currentdata = new Data();
	
	
	private JButton btnDraw;
	private JButton btnNewData;
	private JPanel panTopLeft = new JPanel();
	private JPanel panBotRight = new JPanel();

	private JSpinner spinnerTopLeftX;
	private JSpinner spinnnerTopLeftY;
	private JSpinner spinnerBotRightX;
	private JSpinner spinnerBotRightY;
	
	public static JPanel panContainer = new JPanel();
	private JPanel panSettings = new JPanel();
	public static CardLayout cl = new CardLayout();
	

	//constructor
	public Main()
	{
		super("Main");
			
		panContainer.setLayout(cl);
		
		// Settings Window
		panSettings.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitel = new JLabel("Shadowcalculations");
		lblTitel.setHorizontalAlignment(SwingConstants.CENTER);
		panSettings.add(lblTitel, BorderLayout.NORTH);
		
		btnDraw = new JButton("draw");
		btnDraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {		// action performed when you hit draw:
	
				if(haveData){
					spinnerTopLeftX.getValue();
					spinnnerTopLeftY.getValue();
					spinnerBotRightX.getValue();
					spinnerBotRightY.getValue();
				}
				
				cl.show(panContainer, "2");
			};
		});
			
		panSettings.add(btnDraw, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		panSettings.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(4, 2));
		
		JLabel lblCurrentData = new JLabel("current Data: no Data");
		panel.add(lblCurrentData);
		
		btnNewData = new JButton("new Data");
		btnNewData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {		//action performed when you hit new data:
				
				// sets up a window for choosing the file
				JFileChooser datafetcher = new JFileChooser(); 
				
				//checks if it's a valid file 
				if(datafetcher.showOpenDialog(canvas) == JFileChooser.APPROVE_OPTION){
				    	
					// saves the file path
					File fetcheddata = datafetcher.getSelectedFile().getAbsoluteFile();
			            		
					try{		
						currentdata = new Data(fetcheddata);
						haveData = true;
						
						//creates the spinner and which the current data ist
						lblCurrentData.setText("current Data: " + currentdata.getName());
			           
						SpinnerModel topleftX = new SpinnerNumberModel(
								currentdata.randomX(), currentdata.getMinX(), currentdata.getMaxX(),200
								); 
						SpinnerModel topleftY = new SpinnerNumberModel(
								currentdata.randomY(), currentdata.getMinY(), currentdata.getMaxY(), 200
								);
			            	
						SpinnerModel botrightX = new SpinnerNumberModel(
								currentdata.randomX(), currentdata.getMinX(), currentdata.getMaxX(), 200
								); 
						SpinnerModel botrightY = new SpinnerNumberModel(
								currentdata.randomY(), currentdata.getMinY(), currentdata.getMaxY(), 200
								);
			        	
						spinnerTopLeftX = new JSpinner(topleftX);
						spinnnerTopLeftY = new JSpinner(topleftY);
						panTopLeft.removeAll();
						panTopLeft.add(spinnerTopLeftX);
						panTopLeft.add(spinnnerTopLeftY);
						panTopLeft.repaint();
			            	
						spinnerBotRightX = new JSpinner(botrightX);
						spinnerBotRightY = new JSpinner(botrightY);
						panBotRight.removeAll();
						panBotRight.add(spinnerBotRightX);
						panBotRight.add(spinnerBotRightY);
						panBotRight.repaint();
						
						Thread.sleep(10);
					} catch (Exception e){
			          
						haveData = false;
						System.out.println(e);
					}     
				}
			};
		});
		panel.add(btnNewData);
		
		
		// label TopLeft Corner
		JLabel lblTopLeftCorner = new JLabel("top left corner: ");
		panel.add(lblTopLeftCorner);
		
		//input TopLeft
		JLabel lblnoDataTopLeft = new JLabel("no data");
		panTopLeft.add(lblnoDataTopLeft);
		panel.add(panTopLeft);
		
		
		// label BotRight Corner
		JLabel lblBotRightCorner = new JLabel("bottom right corner: ");
		panel.add(lblBotRightCorner);
		
		//input BotRight
		JLabel lblnoDataBotRight = new JLabel("no data");
		panBotRight.add(lblnoDataBotRight);
		panel.add(panBotRight);
		
		
		// drawWindow
		caps = new GLCapabilities(GLProfile.getDefault());
		canvas = new GLCanvas(caps);
		camera = new camera(0,-2,-10);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(new KeyAdapter());
		canvas.addMouseListener(new MouseAdapter());
		
		
		panContainer.add(panSettings, "1");
		panContainer.add(canvas, "2");
		
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