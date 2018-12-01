import java.util.ArrayList;

import javax.print.attribute.standard.NumberOfDocuments;

import java.math.BigDecimal;

public class Sample implements Feature, Label{
	
	ArrayList<String> raw;
	int numofSamples;
	int numofPredicts;
	final int numofFeatures = 10;
	ArrayList<String[]> sentence;
	ArithmeticUtils utils;
	
	// The true-false table for building the hypothesis.
	double[][] samples;
	
	// Dynamic changed Sample Weights
	double[] weightsSp;
	
	// The label for samples
	double[] labels;
	
	//The un-predect raw data.
	ArrayList<String> predict_raw;
	
	//The un-predect processed sentence.
	ArrayList<String[]> predict_sentence;
	
	// The true-false table for predicting the hypothesis
	double[][] predict_data;
	
	public Sample (int num, int num2) {
		this.utils = new ArithmeticUtils();
		this.numofSamples = num;
		this.numofPredicts = num2;
		this.raw = new ArrayList<String>();
		this.samples = new double[num][numofFeatures];
		this.sentence = new ArrayList<String[]>();
		this.weightsSp = new double[numofSamples];
		this.labels = new double[numofSamples];
		
		for(int i = 0; i < numofSamples; i++) {
			weightsSp[i] = utils.div(1.00, (double)numofSamples, 2);
		}
		
		predict_raw = new ArrayList<>();
		predict_sentence = new ArrayList<String[]>();
		predict_data = new double[numofPredicts][numofFeatures];

	}
	public int getNumOfFeatures() {
		return numofFeatures;
	}
	
	public int getNumOfSamples() {
		return numofSamples;
	}
	
	public double[][] getSamples(){
		return samples;
	}
	
	public double[] getLabels(){
		return labels;
	}
	
	public double[] getWeightsSp() {
		return weightsSp;
	}
	
	public void append(String str) {
		raw.add(str);
	}
	
	public void load(String str) {
		predict_raw.add(str);
	}
	
	public void setPredictionData() {
		for (int i = 0; i < this.predict_raw.size(); i++) {
			String[] str = predict_raw.get(i).split(" ");
			this.predict_sentence.add(str); 
			// sentence: the list of string[] : 1: a, b, c, d, e
			//                                : 2: f, g, h, j, k
			//                                : 3: l, m, n, e, s
		}
		
		// set features for prediction.
		for (int i = 0; i < predict_sentence.size(); i++) {
			this.ifContains_de_Predict(this.predict_sentence.get(i), i);
			this.ifContains_en_Predict(this.predict_sentence.get(i), i);
			this.ifContains_een_Predict(this.predict_sentence.get(i), i);
			this.ifContains_het_Predict(this.predict_sentence.get(i), i);
			this.ifContains_van_Predict(this.predict_sentence.get(i), i);
			this.ifContains_op_Predict(this.predict_sentence.get(i), i);
			this.ifContains_ij_Predict(this.predict_sentence.get(i), i);
			this.ifContains_die_Predict(this.predict_sentence.get(i), i);
			this.averageLengthMoreThanFive_Predict(this.predict_sentence.get(i), i);
			this.consonantsMoreThanThreepointFive_Predict(this.predict_sentence.get(i), i);
		}
	}
	
	public void print() {
		for (int i = 0; i < numofPredicts; i++) {
			for (int j = 0; j < numofFeatures; j++) {
				// 4 spaces between each value.
				System.out.println("predicts: " + predict_data[i][j]);
					
			}
			System.out.println();
		}
	}
	
	/*
	 * This function is used for process input string.
	 */
	public void setData() {
		for (int i = 0; i < this.raw.size(); i++) {
			String[] str = raw.get(i).split(" ");
			this.sentence.add(str); 
			// sentence: the list of string[] : 1: a, b, c, d, e
			//                                : 2: f, g, h, j, k
			//                                : 3: l, m, n, e, s
		}
	}
	
	public void setFeatures() {
		for (int i = 0; i < sentence.size(); i++) {
			this.ifContains_de(this.sentence.get(i), i);
			this.ifContains_en(this.sentence.get(i), i);
			this.ifContains_een(this.sentence.get(i), i);
			this.ifContains_het(this.sentence.get(i), i);
			this.ifContains_van(this.sentence.get(i), i);
			this.ifContains_op(this.sentence.get(i), i);
			this.ifContains_ij(this.sentence.get(i), i);
			this.ifContains_die(this.sentence.get(i), i);
			this.averageLengthMoreThanFive(this.sentence.get(i), i);
			this.consonantsMoreThanThreepointFive(this.sentence.get(i), i);
		}
	}
	
	public void setLables() {
		
		System.out.println("what?");
		System.out.print(" sentence size is  " + this.sentence.size());
		for (int i = 0; i < this.sentence.size(); i++) {
		//for (int i = 0; i < this.numofSamples; i++) {
			String[] strings = this.sentence.get(i);
			int length = strings.length;
			for (int j = 0; j < length; j++)
				System.out.print(strings[j] + "  ");
			
			if (strings[length - 1].contains("true")) {
				labels[i] = weightsSp[i];
			}
			else {
				labels[i] = 0.0;
			}
			
			System.out.println(" ");
		}
	}
	public void ifContains_de_Predict(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("de")) {
				predict_data[indexSp][0] = 1;
			}
			if (str[i].contentEquals("De")) {
				predict_data[indexSp][0] = 1;		
			}
		}
	}
	public void ifContains_en_Predict(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("en")) {
				predict_data[indexSp][1] = 1;
			}
			if (str[i].contentEquals("En")) {
				predict_data[indexSp][1] = 1;		
			}
		}	
	}
	public void ifContains_een_Predict(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("een")) {
				predict_data[indexSp][2] = 1;
			}
			if (str[i].contentEquals("Een")) {
				predict_data[indexSp][2] = 1;		
			}
		}
	}
	public void ifContains_het_Predict(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("het")) {
				predict_data[indexSp][3] = 1;
			}
			if (str[i].contentEquals("Het")) {
				predict_data[indexSp][3] = 1;		
			}
		}
	}
	public void ifContains_van_Predict(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("van")) {
				predict_data[indexSp][4] = 1;
			}
			if (str[i].contentEquals("Van")) {
				predict_data[indexSp][4] = 1;		
			}
		}
	}
	public void ifContains_op_Predict(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("op")) {
				predict_data[indexSp][5] = 1;
			}
			if (str[i].contentEquals("Op")) {
				predict_data[indexSp][5] = 1;		
			}
		}
	}
	public void ifContains_ij_Predict(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contains("ij")) {
				samples[indexSp][6] = 1;
			}
		}
	}
	public void ifContains_die_Predict(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("die")) {
				predict_data[indexSp][7] = 1;
			}
			if (str[i].contentEquals("Die")) {
				predict_data[indexSp][7] = 1;		
			}
		}
	}
	public void averageLengthMoreThanFive_Predict(String[] str, int indexSp) {
		double length = 0;
		for (int i = 0; i < str.length; i++) {
			length += str[i].length();
		}
		if ((length / (double)str.length) >= 5.0) {
			//System.out.print("The average is greater than 5; the length = " + (length / (double)str.length));
			predict_data[indexSp][8] = 1;
		}
	}
	public void consonantsMoreThanThreepointFive_Predict(String[] str, int indexSp) {
		double size = 0;
		double count = 0;
		double count_total = 0;
		for (int i = 0; i < str.length; i++) {
			size++;
			count_total += str[i].length();
			for (int j = 0; j < str[i].length(); j++) {
				 char ch = str[i].charAt(j);
				 if (ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U' || 
						 ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
					 count++;
				 }
			}
		}
		//System.out.println("The size: " + size);
		//System.out.println("The count: " + count);
		
		if (((count_total - count)/size) >= 3.5 )
			predict_data[indexSp][9] = 1;
	}
	
	
	public void ifContains_de(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("de")) {
				samples[indexSp][0] = weightsSp[indexSp];
			}
			if (str[i].contentEquals("De")) {
				samples[indexSp][0] = weightsSp[indexSp];		
			}
		}
	}
	public void ifContains_en(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("en")) {
				samples[indexSp][1] = weightsSp[indexSp];
			}
			if (str[i].contentEquals("En")) {
				samples[indexSp][1] = weightsSp[indexSp];		
			}
		}	
	}
	public void ifContains_een(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("een")) {
				samples[indexSp][2] = weightsSp[indexSp];
			}
			if (str[i].contentEquals("Een")) {
				samples[indexSp][2] = weightsSp[indexSp];		
			}
		}
	}
	public void ifContains_het(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("het")) {
				samples[indexSp][3] = weightsSp[indexSp];
			}
			if (str[i].contentEquals("Het")) {
				samples[indexSp][3] = weightsSp[indexSp];		
			}
		}
	}
	public void ifContains_van(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("van")) {
				samples[indexSp][4] = weightsSp[indexSp];
			}
			if (str[i].contentEquals("Van")) {
				samples[indexSp][4] = weightsSp[indexSp];		
			}
		}
	}
	public void ifContains_op(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("op")) {
				samples[indexSp][5] = weightsSp[indexSp];
			}
			if (str[i].contentEquals("Op")) {
				samples[indexSp][5] = weightsSp[indexSp];		
			}
		}
	}
	public void ifContains_ij(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contains("ij")) {
				samples[indexSp][6] = weightsSp[indexSp];
			}
		}
	}
	public void ifContains_die(String[] str, int indexSp) {
		for (int i = 0; i < str.length; i++) {
			if (str[i].contentEquals("die")) {
				samples[indexSp][7] = weightsSp[indexSp];
			}
			if (str[i].contentEquals("Die")) {
				samples[indexSp][7] = weightsSp[indexSp];		
			}
		}
	}
	public void averageLengthMoreThanFive(String[] str, int indexSp) {
		double length = 0;
		for (int i = 0; i < str.length; i++) {
			length += str[i].length();
		}
		if ((length / (double)str.length) >= 5.0) {
			//System.out.print("The average is greater than 5; the length = " + (length / (double)str.length));
			samples[indexSp][8] = weightsSp[indexSp];
		}
	}
	public void consonantsMoreThanThreepointFive(String[] str, int indexSp) {
		double size = 0;
		double count = 0;
		double count_total = 0;
		for (int i = 0; i < str.length; i++) {
			size++;
			count_total += str[i].length();
			for (int j = 0; j < str[i].length(); j++) {
				 char ch = str[i].charAt(j);
				 if (ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U' || 
						 ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
					 count++;
				 }
			}
		}
		//System.out.println("The size: " + size);
		//System.out.println("The count: " + count);
		
		if (((count_total - count)/size) >= 3.5 )
			samples[indexSp][9] = weightsSp[indexSp];
	}
}
