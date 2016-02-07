import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataSwisstopo200m {

	List<String> data = new ArrayList<String>();
	
	public List<String> getData() {
		return data;
	}

	public DataSwisstopo200m() {
	
		
		String csvFile = "C:/Users/Daniel/git/Maturaarbeit/datas/SwissTopoData200mRegionEinsiedeln-Pfäffikon.csv";

		try{
		    InputStream fis= new FileInputStream(csvFile);
		    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		    
		    for (String line = br.readLine(); line != null; line = br.readLine()) {
		    	data.add(line);
		    }
		    
		    br.close();
		}
		catch(Exception e){
		    System.err.println("Error: Target File Cannot Be Read");
		}
	
	}

	
	
}
