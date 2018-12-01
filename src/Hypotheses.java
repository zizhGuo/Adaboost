import java.math.BigDecimal;
import java.util.ArrayList;

import org.ietf.jgss.Oid;

public class Hypotheses {
	
	// ************************Hypotheses Models*****************************
	// The hypotheses that Adaboost produces, which is used for prediction.
	double[][] hypotheses;
	
	//The labels used for prediction
	double[][] labelsSp;
	
	// The weights for different hypotheses, which is sued for prediction
	double[] weightsZ;
	
	// ************************Initialization from input**********************
	int numofSamples;
	int numofFeatures;
	
	ArrayList<Integer> stumpsOrder;
	
	// ********************Dynamically changed weights for samples***********
	// The true-false table for building the hypothesis.
	double[][] samples;
	
	// The label for samples
	double[] labels;
	
	// Dynamic changed Sample Weights
	double[] weightsSp;
	
	// ************************Prediction data**********************
	
	double[][] predict_data;
	
	int numofPredicts;
	// **************************************************************
	public Hypotheses(Sample sample) {
		this.numofSamples = sample.numofSamples;
		this.numofFeatures = sample.numofFeatures;
		this.samples = sample.samples;
		this.labels = sample.labels;
		stumpsOrder = new ArrayList<>();
		this.predict_data = sample.predict_data;
		this.numofPredicts = sample.numofPredicts;
		
		this.weightsSp = sample.weightsSp;
		
		hypotheses = new double[sample.numofSamples][sample.numofFeatures]; // why is numOfSamples???
		labelsSp = new double[sample.numofSamples][sample.numofFeatures];
		weightsZ = new double[sample.numofFeatures];
		
		//initHypothese();
		
		
	}
	public void printSamples() {
		for (int i = 0; i < numofSamples; i++) {
			for (int j = 0; j < numofFeatures + 1; j++) {
				if (j < numofFeatures)
					System.out.print(samples[i][j] + "    ");
				else
					System.out.print(labels[i] + "    ");
					
			}
			System.out.println();
		}
	}
//	
//	// Initialization of the hypothese
//	public void initHypothese() {
//		for (int i = 0; i < numofSamples; i++) {
//			for (int j = 0; j < numofFeatures; j++) {
//				hypotheses[i][j] = samples[i][j];
//			}
//		}
//	}
	
	/**
	 * L-Algorithm to build the decision stump
	 * 
	 */
public void createDecisionStump() {
		
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
			
			/***
			 * add an if statement to rule out the stump index that selected before
			 * if (!stumpsOrder.contains(Integer.valueOf(f)))
			 */
			if (!stumpsOrder.contains(Integer.valueOf(f))) {
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
				System.out.println("Feature " + f + " Entropy = "+ featuresEntropy[f]);
			}
		}
		
		double gain = 0;
		int stumpIndex = 0;
		for (int f = 0; f < numofSamples; f++) {
			if (!stumpsOrder.contains(Integer.valueOf(f))) {
				double temp = labelsEntropy - featuresEntropy[f];
				if (Double.compare(gain, temp) < 0) {
					gain = temp;
					stumpIndex = f;
				}
			}
		}
		// Record down the index of chosen decision stump and add it to the array list.
		// This variable works for the later Adaboost initialization.
		stumpsOrder.add(stumpIndex);
		
		// Produce hypotheses and their corresponding labels (weighted under current sample weights)
		for (int i = 0; i < numofSamples; i++) {
			hypotheses[i][stumpIndex] = samples[i][stumpIndex];
			labelsSp[i][stumpIndex] = labels[i];
		}
		System.out.println("gain = " + gain);
		System.out.println("Decision stump index = " + stumpIndex);
		
	}
	

	public void createStump() {
		int indexOfStump = 0;
		double error[] = new double[numofFeatures];
		
		
		// 找出错误率最低的feature
		for (int f = 0; f < numofFeatures; f++) {
			if (!stumpsOrder.contains(Integer.valueOf(f))) {
				for (int s = 0; s < numofSamples; s++) {
					if (samples[s][f] != labels[s]) {
						error[f] += labels[s];
					}
				}
			}
		}
		
		int maxIndex = 0;
		double max = 0.0;
		for (int f = 0; f < numofFeatures; f++) {
			//System.out.println(" error rate : " + error[f]);
			if (error[f] > max) {
				max = error[f];
				maxIndex = f;
			}
		}
		
		// 找出后把该feature加入stumpindex
		stumpsOrder.add(maxIndex);
		
		// 更新hyptheses
		for (int i = 0; i < numofSamples; i++) {
			hypotheses[i][maxIndex] = samples[i][maxIndex];
			labelsSp[i][maxIndex] = labels[i];
		}
	}
	
	public void adaboost() {
		for (int k = 0; k < numofFeatures; k++) {
			//int k = 0;
			// Create the decistion stump
			//createDecisionStump();
			createStump();
			
			double error = 0;
			
			// Calculate the total error
			for (int s = 0; s < numofSamples; s++) {
				if (hypotheses[s][stumpsOrder.get(k)] != labelsSp[s][stumpsOrder.get(k)] ) {
					error += weightsSp[s];
				}
			}
			
			if (error > 0.10 ) {
			// Update the weights of samples
			for (int s = 0; s < numofSamples; s++) {
				if (hypotheses[s][stumpsOrder.get(k)] == labelsSp[s][stumpsOrder.get(k)] ) {
					weightsSp[s] = weightsSp[s] * error /(1 - error);
				}
			}
			
			// Normalize the weights of samples
			normalizeWeights();
			System.out.println("error = " + error);
//			for (int s = 0; s < numofSamples; s++) {
//				System.out.println("weightsSP = " + weightsSp[s]);
//			}
			
			// Update the labels with new weights
			updateLabels();
			
			// Update the samples with new weights
			updateSamples();
				double e = (1 - error)/error;
				BigDecimal b = new BigDecimal(e);
				
				// Update the weights for the decision stumps
				weightsZ[k] = Math.log(b.doubleValue())/Math.log(2.0);
				//System.out.println(" Weight Z = " + weightsZ[k]);			
			}
			else {
				break;
			}
		}
		normalizedWeightsZ();
		for (int f = 0; f < stumpsOrder.size();f ++)
			System.out.println("weightsZ[" + (stumpsOrder.get(f))+ "] = " + weightsZ[stumpsOrder.get(f)]);
	}
	
	
	public void predictByAdaboostStumps() {
		double[] total = new double[numofFeatures];
		
		for (int s = 0; s < numofPredicts; s++) {
		{	
			for (int f = 0; f < numofFeatures; f++)
				total[s] += predict_data[s][f] * weightsZ[f];
				
			}
		}
		
		for (int s = 0; s < numofPredicts; s++) {
			System.out.println("prediction: " + total[s]);
				
		}
	}
	/*
	 * For print out the features.
	 */
	public void printWeightZ() {
		for (int f = 0; f < stumpsOrder.size();f ++)
			System.out.println("weightsZ[" + (stumpsOrder.get(f))+ "] = " + weightsZ[stumpsOrder.get(f)]);
	}
	
	/*
	 * For print out the whole samples and labels
	 */
	public void print() {
		//System.out.println("de    en    een");
		for (int i = 0; i < numofSamples; i++) {
			for (int j = 0; j < numofFeatures + 1; j++) {
				// 4 spaces between each value.
				if (j < numofFeatures)
					System.out.print(samples[i][j] + "    ");
				else
					System.out.print(labels[i] + "    ");
					
			}
			System.out.println();
		}
	}
	
	/*
	 * Normalization
	 */
	public void normalizeWeights() {
		double total = 0;
		for (int s = 0; s < numofSamples; s++) {
			total += weightsSp[s];
		}
		for (int s = 0; s < numofSamples; s++) {
			weightsSp[s] = weightsSp[s]/total;
		}
	}
	
	public void normalizedWeightsZ() {
		double total = 0;
		for (int s = 0; s < numofFeatures; s++) {
			total += weightsZ[s];
		}
		for (int s = 0; s < numofFeatures; s++) {
			weightsZ[s] = weightsZ[s]/total;
		}
	}
	
	public void updateLabels() {
		for (int s = 0; s < numofSamples; s++) {
			if (labels[s] != 0)
				labels[s] = weightsSp[s];
		}
		
	}
	public void updateSamples() {
		for (int s = 0; s < numofSamples; s++) {
			for (int f = 0; f < numofFeatures; f++) {
				if (samples[s][f] != 0)
					samples[s][f] = weightsSp[s];
			}
		}
	}
	
//	public void printHypotheses() {
//		for (int i = 0; i < numofSamples; i++) {
//			System.out.println(hypotheses[i][stumpsOrder.get(0)]);
//		}
//		for (int i = 0; i < numofSamples; i++) {
//			System.out.println(labelsSp[i][stumpsOrder.get(0)]);
//		}
//	}
    public double entropy(double[] labels, ArrayList<Integer> index, double[] weightsSp, int indexOfFeatures) {
    	if (index.size() == 0) return 0;
    	double p = 0.0;
    	double w = 0.0;
    	for (int i = 0; i < index.size(); i++) {
    		p += labels[index.get(i)];
    		w += weightsSp[index.get(i)];
    		//System.out.print("weights = " + weightsSp[index.get(i)] + "   ");
    		//System.out.println("");
    	}
    	//System.out.println("p = " + p);
    	//System.out.println("w = " + w);
    	double p_normalize = p/w;
    	//System.out.println("p_normalize = " + p_normalize);
    	BigDecimal b = new BigDecimal(p_normalize);
    	if (p_normalize == 1.0) {
    		return 0.0;
    	}
    	if (p_normalize == 0.0) {
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
    		System.out.println("W!!!!!!! " + lables[i]);
    	}
    	System.out.println("W!!!!!!!!!!!!!!!!!!!!!!!!!: " + p);
    	BigDecimal b = new BigDecimal(p);
    	return -(b.doubleValue())* Math.log(b.doubleValue())/Math.log(2.0) - (1.0 - b.doubleValue())*Math.log(1.0 - b.doubleValue())/Math.log(2.0);
    }
}
