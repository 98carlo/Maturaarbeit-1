import java.awt.Point;

public class Position extends Point {
	// variables
	public double x, y, z;

	//constructors
	public Position() {
		this(0, 0, 0);
	}

	public Position(Position p) {
		this(p.x, p.y, p.z);
		
	}

	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;		
	}
	
	// methods
	public void moveTo(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void moveTo(Position p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public void translate(double dx, double dy, double dz){
		this.x += dx;
		this.y += dy;
		this.z += dz;
	}
	
	public double distanceTo(Position p){
		double dx = p.x - this.x;
		double dy = p.y - this.y;
		double dz = p.z - this.z;
		
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	
}
