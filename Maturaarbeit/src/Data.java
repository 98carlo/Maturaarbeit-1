import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Data {

	private List<String> dataList = new ArrayList<String>();
	private String dataString = null;
	private double[] dataXYZ = null;
	private double[] dataX = null;
	private double[] dataY = null;
	private double[] area = null;
	
	private int[] indices;
	
	private int triangulationMode = 0;
	
	private double width;
	private double height;
	private double step;
	
	
	private String Name = null;
	private String Path = null;
	
	//empty constructor
	public Data(){
	};
	
	//constructor
	public Data(File fetcheddata) {
	
		
		Path = fetcheddata.getAbsolutePath();
		Name = fetcheddata.getName();
		
		try{
		    InputStream fis= new FileInputStream(this.Path);
		    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		    
		    for (String line = br.readLine(); line != null; line = br.readLine()) {
		    	dataList.add(line);
		    }
		    
		    br.close();   
		    
		    dataString = dataList.toString();
		   

		    // filtering the extra characters and setting up for splitting with pattern and matcher
		    Pattern extraCharacters = Pattern.compile("([\\,\\]\\[])");
		    Matcher matcherExtra = extraCharacters.matcher(dataString);
		    dataString = matcherExtra.replaceAll("");
		    
		    
		    Pattern comma = Pattern.compile("\\s+");
		    Matcher matcherComma = comma.matcher(dataString);
		    dataString = matcherComma.replaceAll(",");
			   
		    
		    /* same thing just with only replaceAll
		    dataString = dataString.replaceAll(",", "");
		    dataString = dataString.replaceAll("\\s", ",");
		    dataString = dataString.replaceAll("\\[", "");
		    dataString = dataString.replaceAll("\\]", "");		    
		    */
		    
		    String[] dataStringArray = dataString.split(",");

		    
		    dataXYZ = new double[dataStringArray.length];
		    
		    List<Double> allX = new ArrayList<Double>();
		    List<Double> allY = new ArrayList<Double>();
		    
		    
		    for(int i = 0; i < dataStringArray.length; i++){
		    	dataXYZ[i] = Double.parseDouble(dataStringArray[i]);

		    	
		    	if((i%3) == 0){
		    		
		    		allX.add(dataXYZ[i]);
		    		
		    	} else if((i%3) == 1){
		    		
		    		allY.add(dataXYZ[i]);

		    	} 
		    	
		    };
		    
		    dataX = new double[allX.size()];
		    for (int i = 0; i < dataX.length; i++) {
		    	dataX[i] = allX.get(i);
		    }
		    
		    dataY = new double[allY.size()];
		    for (int i = 0; i < dataY.length; i++) {
		    	dataY[i] = allY.get(i);
		    }
		    
		    Arrays.sort(dataX);
		    Arrays.sort(dataY);
		    
		    
		}
		catch(Exception e){
		    System.err.println("Error: Target File Cannot Be Read");
		    System.err.println(e);
		}
	
	} // end of constructor

	
	
	public void newArea(double topleftX, double topleftY, double botrightX, double botrightY) {
		
		List<Double> areaList = new ArrayList<Double>();

		width = Math.abs(botrightX - topleftX);
		height = Math.abs(topleftY - botrightY);
		
		try{
			for(int i = 0; i < dataXYZ.length; i+=3) {
				if((dataXYZ[i] >= topleftX) && (dataXYZ[i] <= botrightX)){
					
					if((dataXYZ[i+1] <= topleftY) && (dataXYZ[i+1] >= botrightY)) {

						areaList.add(dataXYZ[i]);
						areaList.add(dataXYZ[i+2]);		//y and z are switched in OpenGL
						areaList.add(dataXYZ[i+1]);
					}
				}
			}
						
			this.area = new double[areaList.size()];
		    
			
			for (int i = 0; i < area.length; i++) {
		    	area[i] = areaList.get(i);
		    }
		    	
		} 
		catch(Exception e){
		    System.err.println(e);
		}	
		
	}

	public void setupDraw() {
		
		double x = area[0];
		double y = area[1];
		double z = area[2];
		List<Integer> indicesList = new ArrayList<Integer>(); 
		
		step = Math.abs(area[3] - x);

		width /= step;	width += 1;
		height/= step;	height += 1;
		
		try{
		
			for(int i = 0; i < area.length; i+=3){
				area[i] = (area[i] - x) / step;
				area[i+1] = (area[i+1] - y) / step;
				area[i+2] = (area[i+2] - z) / step;
			}
		
		
			for(y = 0; y < (height-1); y++){
				for(x = 0; x < (width-1); x++){
					int A = (int) (y*width + x); 
					int B = (int) ((y+1)*width + x);
					int C = (int) ((y+1)*width + (x+1));
					int D = (int) (y*width + (x+1));
				
					
					// for the different triangulation methods..
					// the square is bottom left = A, top left = B, top right = C, bottom right = D
					
					
					switch(triangulationMode){
					
					case 0: 	// mode 0 makes the triangles: ABC & ACD (square split form bot left to top right...) 
						indicesList.add(A);
						indicesList.add(B);
						indicesList.add(C);
						
						indicesList.add(A);
						indicesList.add(C);
						indicesList.add(D);
						break;
						
					case 1:		// mode 0 makes the triangles: ABD & BCD (square split form top left to bot right...) 
						
						indicesList.add(A);
						indicesList.add(B);
						indicesList.add(D);
						
						indicesList.add(B);
						indicesList.add(C);
						indicesList.add(D);
						
						break;
					
					default:
						System.out.println("NO TRIANGULATION MODE");
						break;
					}
					
				}
			}
		
			
			indices = new int[indicesList.size()];
			for(int i = 0; i < indices.length; i++){
				indices[i] = indicesList.get(i);
			}
		
		} catch(Exception e){
			System.err.println(e);
		}
	} // end of setupDraw

	
	//getter and setter:
	
	public String getName() {
		return this.Name;
	}

	public double randomX() {
		return dataX[(int)Math.floor(Math.random()* dataX.length)];
	}
	
	public double randomY() {
		return dataY[(int)Math.floor(Math.random()*dataY.length)];
	}

	public double getMaxX() {
		return dataX[dataX.length-1];
	}
	
	public double getMaxY() {
		return dataY[dataY.length-1];
	}
	
	public double getMinX() {
		return dataX[0];
	}

	public double getMinY() {
		return dataY[0];
	}
	
	public double[] getVertices(){
		return area;
	}
	
	public int[] getIndices(){
		return indices;
	}

}
