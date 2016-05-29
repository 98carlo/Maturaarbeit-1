
@SuppressWarnings("serial")
public class Vector extends Position {

	public double length;
	
	public Vector(Position p) {
		super(p);
		this.length = getLength(this);
	}
	
	public Vector(double x, double y, double z) {
		super(x, y, z);
		this.length = getLength(this);
	}
	
	public double getLength(Vector v){
		return(Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z));
	}
	
	public Vector add(Vector v2) {
		return new Vector(this.x + v2.x, this.y + v2.y, this.z + v2.z);
	}
	
	public Vector average(Vector...vecs){
		
		int numberOfvecs = vecs.length;
		Vector vectorAveraged = this;
		
		for(int i = 0; i < numberOfvecs; i++) {
			vectorAveraged = vectorAveraged.add(vecs[i]);
		}
	
		return vectorAveraged;
	}
	
	public Vector normalize(){
		x = this.x / this.length;
		y = this.y / this.length;
		z = this.z / this.length;
		
		return new Vector(x,y,z);
	}
	
	public double angleTo(Vector v) {
		double skal = this.x*v.x + this.y*v.y + this.z*v.z;
		
		return Math.acos(skal/(this.length * v.length));
	} // end of angleTo
	
	public Vector crossProduct(Vector v) {
		return new Vector(this.z*v.y - this.y*v.z, this.x*v.z - this.z*v.x, this.y*v.x - this.x*v.y);
	}
	
	public Vector getNormal(Position p1, Position p2, Position p3){
		Vector a = new Vector(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z);
		Vector b = new Vector(p3.x-p1.x, p3.y-p1.y, p3.z-p1.z);
		return a.crossProduct(b);
	}
	
	
}
