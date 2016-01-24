
public class Vector extends Position {

	public double length;
	
	public Vector(Position p) {
		super(p);
		length = getLength(this);
	}

	public Vector(double x, double y, double z) {
		super(x, y, z);
		length = getLength(this);
	}

	public double getLength(Vector v){
		return(Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z));
	}
	
	public void normalize(){
		this.x /= length;
		this.y /= length;
		this.z /= length;
	}
	
	public double angleTo(Vector v) {
		double skal = this.x*v.x + this.y*v.y + this.z*v.z;
		if(skal == 0) {
			return 90;
		} else {
			return(Math.acos(skal/(this.length * v.length)));
		}
	} // end of angleTo
	
	public Vector crossProduct(Vector v) {
		return new Vector(this.y*v.z - this.z*v.y, this.z*v.x - this.x*v.z, this.x*v.y - this.y*v.x);
	}
	
	public Vector getNormal(Position p1, Position p2, Position p3){
		Vector a = new Vector(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z);
		Vector b = new Vector(p3.x-p1.x, p3.y-p1.y, p3.z-p1.z);
		return a.crossProduct(b);
	}
	
}
