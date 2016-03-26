import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {

	private List<String> dataList = new ArrayList<String>();
	private String dataString = "";
	private double[] dataXYZ;
	private double[] dataX;
	private double[] dataY;
	
	private String Name = "";
	private String Path;
	
	//constructor
	public Data(File fetcheddata) {
	
		
		this.Path = fetcheddata.getAbsolutePath();
		this.Name = fetcheddata.getName();
		
		try{
		    InputStream fis= new FileInputStream(this.Path);
		    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		    
		    for (String line = br.readLine(); line != null; line = br.readLine()) {
		    	dataList.add(line);
		    }
		    
		    br.close();   
		    
		    dataString = dataList.toString();
		   
		    dataString = dataString.replaceAll(",", "");
		    dataString = dataString.replaceAll("\\s", ",");
		    dataString = dataString.replaceAll("\\[", "");
		    dataString = dataString.replaceAll("\\]", "");
		    
		    
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
		    
		    System.out.println(dataX[0]);
		    System.out.println(dataX[dataX.length-1]);
		    
		    
		}
		catch(Exception e){
		    System.err.println("Error: Target File Cannot Be Read");
		    System.err.println(e);
		}
	
	} // end of constructor

	public String getName() {
		return this.Name;
	}

	public double randomX() {
		return dataX[(int)Math.floor(Math.random()*dataX.length)];
	}

	public double getMinX() {
		return dataX[0];
	}

	public double getMaxX() {
		// TODO Auto-generated method stub
		return 1;
	}

	public double getStep() {
		// TODO Auto-generated method stub
		return 1;
	}

	
}
