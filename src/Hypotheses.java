import java.math.BigDecimal;
import java.util.ArrayList;

public class Hypotheses {

	double[] h;
	double[] dweightsH;
	
	int numofSamples;
	int numofFeatures;
	
	
	public Hypotheses(int numofSamples, int numofFeatures) {
		this.numofSamples = numofSamples;
		this.numofFeatures = numofFeatures;
		h = new double[numofSamples];
		dweightsH = new double[numofFeatures];
	}
	
	
	/**
	 * L-Algorithm to build the decision stump
	 * 
	 */
	public void createDecisionStump(double[][] samples, double[] labels, double[] weightsSp, int numorFeatures, int numofSamples) {
		
		// The entropy of Labels
		double labelsEntropy;
		
		// The entropy for each feature
		double[] featuresEntropy;
		
		// Get the label entropy
		labelsEntropy = entropy(labels, numofSamples);
		
		// Initialize entropies of features
		featuresEntropy = new double[numofFeatures];
		System.out.println("Labels' entropy: " + labelsEntropy);
		
		for (int f = 0; f < numofFeatures; f++) {
			
			// The indexes that labeled 
			ArrayList<Integer> index_labled = new ArrayList<>();
			
			// The indexes that un-labeled
			ArrayList<Integer> index_unlabled = new ArrayList<>();
			
			// Size of labeled samples in f-th feature
			double size_labeled = 0.0;
			
			// Size of un-labeled samples in f-th feature
			double size_unlabeled = 0.0;
			
			System.out.println("Feature : " + f);
			
			for (int s = 0; s < numofSamples; s++) {
				if (samples[s][f] > 0) {
					
					// add the index of labeled sample to the list
					index_labled.add(Integer.valueOf(s));
				}
				else {
					// add the index of un-labeled sample to the list
					index_unlabled.add(Integer.valueOf(s));
				}
			}
			size_labeled = index_labled.size();
			size_unlabeled = index_unlabled.size();
			
			/*
			 * Calculate all entropies for each features based on all samples
			 */
			featuresEntropy[f] = size_labeled/(double)numofSamples*entropy(labels, index_labled, weightsSp, f)
					+size_unlabeled/(double)numofSamples*entropy(labels, index_unlabled, weightsSp, f);
		  	
			// Print out the selected feature's index as the decision stumps.
			System.out.println("Feature " + f + " Entropy = "+ + featuresEntropy[f]);
		}
		
		double gain = 0;
		int stumpIndex = 0;
		for (int f = 0; f < numofSamples; f++) {
			double temp = labelsEntropy - featuresEntropy[f];
			if (Double.compare(gain, temp) < 0) {
				gain = temp;
				stumpIndex = f;
			}
		}
		System.out.println("Decision stump index = " + stumpIndex);
		
	}
    public double entropy(double[] labels, ArrayList<Integer> index, double[] weightsSp, int indexOfFeatures) {
    	double p = 0.0;
    	double w = 0.0;
    	for (int i = 0; i < index.size(); i++) {
    		p += labels[index.get(i)];
    		w += weightsSp[index.get(i)];
    	}
    	System.out.println("p = " + p);
    	System.out.println("w = " + w);
    	double p_normalize = p/w;
    	System.out.println("p_normalize = " + p_normalize);
    	BigDecimal b = new BigDecimal(p_normalize);
    	if (p_normalize == 1.0) {
    		return 0.0;
    	}
    	return -(b.doubleValue())* Math.log(b.doubleValue())/Math.log(2.0) - (1.0 - b.doubleValue())*Math.log(1.0 - b.doubleValue())/Math.log(2.0);
    }
	
//    // Entropy for the entropy of features.
//    public double entropy(double[][] samples, int index, int numofSamples) {
//    	double p = 0.0;
//    	for (int i = 0; i < numofSamples; i++) {
//    		p += samples[i][index];
//    	}
//    	BigDecimal b = new BigDecimal(p);
//    	return -(b.doubleValue())* Math.log(b.doubleValue())/Math.log(2.0) - (1.0 - b.doubleValue())*Math.log(1.0 - b.doubleValue())/Math.log(2.0);
//    }
    
    // Entropy for the entropy of labels.
    public double entropy(double[] lables, int numofSamples) {
    	double p = 0.0;
    	for (int i = 0; i < numofSamples; i++) {
    		p += lables[i];
    	}
    	BigDecimal b = new BigDecimal(p);
    	return -(b.doubleValue())* Math.log(b.doubleValue())/Math.log(2.0) - (1.0 - b.doubleValue())*Math.log(1.0 - b.doubleValue())/Math.log(2.0);
    }
}
