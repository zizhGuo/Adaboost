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
			System.out.println("Number of samples:     " + numofSamples);
			System.out.println("Number of prediction:  " + numofPredicts);
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
	            DecisionTree decisionTree = new DecisionTree(samples);
	            decisionTree.setRawdate();
	            decisionTree.printRawdata();

	            // This hypotheses are used for building the adaboost stumps.
	            Hypotheses hypotheses = new Hypotheses(samples);
	            hypotheses.printSamples();
	            
	            // Create stumps based on error rate.
	            hypotheses.createStump();
	            
	            //Adaboost to create decision stumps.
	            hypotheses.adaboost();
	            
	            // Read prediction file.
	            BufferedReader reader2 = new BufferedReader(new FileReader(args[3]));
	            String line1 = null;
	            while((line1 = reader2.readLine())!=null) {
	            	samples.load(line1);            	
	            }
	            
	            // Set up the prediction data.
	            samples.setPredictionData();
	            
	            // Use the Decision Stumps to predict the result.
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