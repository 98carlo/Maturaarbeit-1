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
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
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
	private JSpinner spinnerTopLeftY;
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
		
		// the button which switches to the scene
		btnDraw = new JButton("draw");
		btnDraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {		// action performed when you hit draw:
	
				if(haveData){
					
					double topleftX = Math.min((double)spinnerTopLeftX.getValue(), (double) spinnerBotRightX.getValue());
					double topleftY = Math.max((double)spinnerTopLeftY.getValue(), (double) spinnerBotRightY.getValue());
					double botrightX = Math.max((double)spinnerTopLeftX.getValue(), (double) spinnerBotRightX.getValue());
					double botrightY = Math.min((double)spinnerTopLeftY.getValue(), (double) spinnerBotRightY.getValue());
					
					currentdata.newArea(topleftX, topleftY, botrightX, botrightY);
					
					currentdata.setupDraw();
					
				}
				
				cl.show(panContainer, "2");
			};
		});
			
		panSettings.add(btnDraw, BorderLayout.SOUTH);
		
		// label which displays the name of the current data
		JPanel panel = new JPanel();
		panSettings.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(4, 2));
		
		JLabel lblCurrentData = new JLabel("current Data: no Data");
		panel.add(lblCurrentData);
		
		
		// button for fetching data
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
						
						//creates the spinner and which the current data is
						lblCurrentData.setText("current Data: " + currentdata.getName());
						
						SpinnerModel topleftX = new SpinnerNumberModel(
								//currentdata.randomX(), currentdata.getMinX(), currentdata.getMaxX(),200
								694000, currentdata.getMinX(), currentdata.getMaxX(),200
								); 
						SpinnerModel topleftY = new SpinnerNumberModel(
								//currentdata.randomY(), currentdata.getMinY(), currentdata.getMaxY(), 200
								232200, currentdata.getMinY(), currentdata.getMaxY(),200
								);
			            	
						SpinnerModel botrightX = new SpinnerNumberModel(
								//currentdata.randomX(), currentdata.getMinX(), currentdata.getMaxX(), 200
								705200, currentdata.getMinX(), currentdata.getMaxX(),200
								); 
						SpinnerModel botrightY = new SpinnerNumberModel(
								//currentdata.randomY(), currentdata.getMinY(), currentdata.getMaxY(), 200
									215800, currentdata.getMinY(), currentdata.getMaxY(),200
								);
			        	
						spinnerTopLeftX = new JSpinner(topleftX);
						spinnerTopLeftY = new JSpinner(topleftY);
						panTopLeft.removeAll();
						panTopLeft.add(spinnerTopLeftX);
						panTopLeft.add(spinnerTopLeftY);
						panTopLeft.repaint();
			            	
						spinnerBotRightX = new JSpinner(botrightX);
						spinnerBotRightY = new JSpinner(botrightY);
						panBotRight.removeAll();
						panBotRight.add(spinnerBotRightX);
						panBotRight.add(spinnerBotRightY);
						panBotRight.repaint();
						
						System.out.println("Datafetcher finished");
						
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
		camera = new camera(0,-2,-5);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(new KeyAdapter());
		canvas.addMouseListener(new MouseAdapter());
		
		
		panContainer.add(panSettings, "1");
		panContainer.add(canvas, "2");
		
		cl.show(panContainer, "1");
		
		this.add(panContainer);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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

		
		// enables OpenGL - Lighting		has to be removed
		gl.glEnable(GLLightingFunc.GL_LIGHTING);
		gl.glEnable(GLLightingFunc.GL_LIGHT0);
		
		// sets the light properties
		float[] lightPos = { 1f,1f,1f,0};        // light position
		float[] noAmbient = { 0.2f, 0.2f, 0.2f, 1f };     // low ambient light
		float[] diffuse = { 1f, 1f, 1f, 1f };        // full diffuse colour

		// activates the lighting
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_AMBIENT, noAmbient, 0);
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION,lightPos, 0);
		
		
		try {
			setupPointers(gl);
		} catch (Exception e) {
			System.out.println("setupPointersError");
			e.printStackTrace();
		}
	} // end of init
 
	@Override
	public void display(GLAutoDrawable drawable)
	{
		GL2 gl = drawable.getGL().getGL2();
		// clears the color and depth test buffers
		gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		//camera
		gl.glMatrixMode( GLMatrixFunc.GL_PROJECTION );
        gl.glLoadIdentity();
		glu.gluPerspective(camera.fovy, camera.aspect, camera.near, camera.far);
        gl.glTranslated(camera.x, camera.y, camera.z);
        
        //Modeling
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();	
		
		// sets the rendering on wire or fill according to the space input
		if(polygonMode){
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);			
		} else {
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
		}
		
		// gets the indices and loops over them
		int indices[] = currentdata.getIndices();
		IntBuffer indicesBuf = Buffers.newDirectIntBuffer(indices.length);
			
		for (int i = 0; i < indices.length; i++){
			indicesBuf.put(indices[i]);
		}
		indicesBuf.rewind();
		
		//draws the points
		gl.glDrawElements(GL2.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, indicesBuf);
		
		// forces execution of the draw..
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
 
	private void setupPointers(GL2 gl) throws Exception
	{
		
		double vertices[] = currentdata.getVertices();
		double normals[] = currentdata.getNormals();
		
		double colors[] = new double[vertices.length];
		for(int i = 0; i<vertices.length; i+=3){
			colors[i] = 0;
			colors[i+1] = 1;
			colors[i+2] = 0;
		}
		
		DoubleBuffer tmpVerticesBuf = Buffers.newDirectDoubleBuffer(vertices.length);
		DoubleBuffer tmpNormalsBuf = Buffers.newDirectDoubleBuffer(normals.length);
		DoubleBuffer tmpColorsBuf = Buffers.newDirectDoubleBuffer(colors.length);
		
		for (int i = 0; i < vertices.length; i++) {
			tmpVerticesBuf.put(vertices[i]);
		}
		
		for (int i = 0; i < normals.length; i++) {
			tmpNormalsBuf.put(normals[i]);
		}
		
		for (int j = 0; j < colors.length; j++) {
			tmpColorsBuf.put(colors[j]);
		}
			
		tmpVerticesBuf.rewind();
		tmpNormalsBuf.rewind();
		tmpColorsBuf.rewind();
		
		
		gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(3, GL2GL3.GL_DOUBLE, 0, tmpVerticesBuf);
		gl.glNormalPointer(GL2GL3.GL_DOUBLE, 0, tmpNormalsBuf);
		gl.glColorPointer(3, GL2GL3.GL_DOUBLE, 0, tmpColorsBuf);
		
	} // end of setupPointers	
}