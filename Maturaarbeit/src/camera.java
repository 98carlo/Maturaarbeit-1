
public class camera {

	public double x;
	public double y;
	public double z; 
	
	public Vector up = new Vector(0,1,0);
	public Vector forward = new Vector(0, 0, -1);
	public Vector rigth = up.crossProduct(forward);
	
	public double fovy = 60;
	public double aspect = 3/4;
	public double near = 1;
	public double far = 20;
	

	public camera(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
