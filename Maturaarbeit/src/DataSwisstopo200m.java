import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataSwisstopo200m {

	public DataSwisstopo200m() {
	
		
		String csvFile = "C:/Users/Daniel/Desktop/Maturaarbeit/datas/SwissTopoData200m.csv";
		
		try{
		    InputStream fis= new FileInputStream(csvFile);
		    BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		    for (String line = br.readLine(); line != null; line = br.readLine()) {
		       System.out.println(line);
		    }

		    br.close();
		}
		catch(Exception e){
		    System.err.println("Error: Target File Cannot Be Read");
		}
	
	}

}
