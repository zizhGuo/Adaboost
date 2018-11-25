import java.io.FileReader;
import java.io.BufferedReader;
import java.math.*;


public class Wiki {

	//final static int NumofSameples = 10;
	
	public static void main(String[] args) {
		
		int numofSamples = Integer.valueOf(args[0]);
		if (Integer.valueOf(args[0] ) == 10) {
			Sample samples = new Sample(numofSamples);
			System.out.println("Hello Wrold!");
			try {
	            BufferedReader reader = new BufferedReader(new FileReader(args[1]));  
	            String line = null;
	           
	            while((line = reader.readLine())!=null) {
	            	// Get and store each line.
	            	samples.append(line);            	
	            }
	            samples.process();
	            samples.getLables();
	            samples.getFeatures();
	            
	            samples.print();
	            //samples.print();
	            //samples.getFeatures();
	            
			}
			catch(Exception e) {
				System.out.print("Exception: " + e);
				e.printStackTrace();
			}
			finally {
				
			}
		}
	}

}
class Test{
	
}
