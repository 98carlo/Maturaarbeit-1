import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Data {

	private List<String> dataList = new ArrayList<String>();
	
	//constructor
	public Data(File fetcheddata) {
	
		
		String data =  null; //fetcheddata;

		try{
		    InputStream fis= new FileInputStream(data);
		    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		    
		    for (String line = br.readLine(); line != null; line = br.readLine()) {
		    	dataList.add(line);
		    }
		    
		    br.close();
		}
		catch(Exception e){
		    System.err.println("Error: Target File Cannot Be Read");
		}
	
	} // end of constructor

	
	
}
