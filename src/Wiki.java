import java.io.FileReader;
import java.io.BufferedReader;
import java.math.*;


public class Wiki {

	//final static int NumofSameples = 10;
	
	public static void main(String[] args) {
		
		int numofSamples = Integer.valueOf(args[1]);
		int numofPredicts = Integer.valueOf(args[4]);
		if (Integer.valueOf(args[1] ) == 20) {
			Sample samples = new Sample(numofSamples, numofPredicts);
			System.out.println("Hello Wrold!      " + numofSamples);
			System.out.println("Hello Wrold!      " + numofPredicts);
			try {
	            BufferedReader reader = new BufferedReader(new FileReader("test.txt"));  
	            String line = null;
	           
	            while((line = reader.readLine())!=null) {
	            	// Get and store each line.
	            	samples.append(line);            	
	            }
	            samples.setData();
	            samples.setLables();
	            samples.setFeatures();

	            // This hypotheses are used for building the adaboost stumps.
	            Hypotheses hypotheses = new Hypotheses(samples);
	            hypotheses.printSamples();
	            hypotheses.createStump();
	            hypotheses.adaboost();
	           // hypotheses.print();
	            //hypotheses.printWeightZ();
	           
	            // This hypotheses are used for building the decision tree.
	            //Hypotheses hypotheses2 = new Hypotheses(samples);
	            
	            BufferedReader reader2 = new BufferedReader(new FileReader(args[3]));
	            String line1 = null;
		           
	            while((line1 = reader2.readLine())!=null) {
	            	// Get and store each line.
	            	samples.load(line1);            	
	            }
	            samples.setPredictionData();
	            samples.print();
	            hypotheses.predictByAdaboostStumps();
	            
	            
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