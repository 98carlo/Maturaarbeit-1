import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.util.FPSAnimator;


@SuppressWarnings("serial")
public class Main extends JFrame
    implements GLEventListener
{
 
	private GLU glu;
	private GLCapabilities caps;
	private static GLCanvas canvas; 
	public static Settings settings = new Settings("Settings");
	
	public static camera camera;
	public static boolean polygonMode = false;
	

	//constructor
	public Main()
	{
		super("Main");
		
		caps = new GLCapabilities(GLProfile.getDefault());
		canvas = new GLCanvas(caps);
		camera = new camera(0,-2,-10);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(new KeyAdapter());
		canvas.addMouseListener(new MouseAdapter());
		
		getContentPane().add(canvas, BorderLayout.CENTER);
	} // end of constructor
 
	public void run()
	{
		setSize(640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		canvas.requestFocusInWindow();
		
	} // end of run
	
	public static void main(String[] args)
	{
		new Main().run();
		final FPSAnimator animator = new FPSAnimator(canvas, 60,true); 
	    animator.start(); 
	    
	    try {
			new DataSwisstopo200m();
		} catch (Exception e) {
			System.err.println("Something wrong with data");
		}
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
		float vertices[] = new float[]	
				{  		3, -1, 3,   	-3, 0, 3,   	0, 1.5f, 0, 
						-3, 2.5f, -3, 	 3, 1.5f, -3,
				};
		float colors[] = new float[]
				{  		1, 0, 0, 	1, 2, 0, 	0, 1, 0, 
						1, 0, 0, 	1, 0, 0, 	
				};
		/*int normals[] = new int[]
				{ 		1, 0, 0, 	1, 0, 0, 	1, 0, 0, 
						1, 0, 0, 	1, 0, 0, 	1, 0, 0,
						1, 0, 0, 	1, 0, 0, 	1, 0, 0,
						1, 0, 0, 	1, 0, 0, 	1, 0, 0,
				};*/
		FloatBuffer tmpVerticesBuf = Buffers.newDirectFloatBuffer(vertices.length);
		FloatBuffer tmpColorsBuf = Buffers.newDirectFloatBuffer(colors.length);
		//IntBuffer tmpNormalsBuf = Buffers.newDirectIntBuffer(normals.length);
		for (int i = 0; i < vertices.length; i++)
			tmpVerticesBuf.put(vertices[i]);
		for (int j = 0; j < colors.length; j++)
			tmpColorsBuf.put(colors[j]);
		/*for (int k = 0; k < normals.length; k++)
			tmpNormalsBuf.put(normals[k]);  */
		tmpVerticesBuf.rewind();
		tmpColorsBuf.rewind();
		//tmpNormalsBuf.rewind();
		
		//
		gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		//gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
		//
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, tmpVerticesBuf);
		gl.glColorPointer(3, GL.GL_FLOAT, 0, tmpColorsBuf);
		//gl.glNormalPointer(GL2ES2.GL_INT, 0, tmpNormalsBuf);
 
	} // end of setupPointers
  
}