
public class camera extends Vector {

	public Vector normal = new Vector(0,1,0);
	public Position target = new Position(0,0,-100);
	public double fovy = 60;
	public double aspect = 3/4;
	public double near = 1;
	public double far = 20;
	
	public camera(Position p) {
		super(p);
	}

	public camera(double x, double y, double z) {
		super(x, y, z);
	}

}
