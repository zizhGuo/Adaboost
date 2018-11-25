import java.util.ArrayList;
import java.math.BigDecimal;

public class Sample implements Feature, Label{
	
	ArrayList<String> raw;
	int numofSamples;
	final int numofFeatures = 10;
	ArrayList<String[]> sentence;
	ArithmeticUtils utils;
	
	// The true-false table for building the hypothesis.
	double[][] samples;
	
	// Dynamic changed Sample Weights
	double[] weightsSp;
	
	// The label for samples
	double[] labels;
	
	public Sample (int num) {
		this.utils = new ArithmeticUtils();
		this.numofSamples = num;
		this.raw = new ArrayList<String>();
		this.samples = new double[num][numofFeatures];
		this.sentence = new ArrayList<String[]>();
		this.weightsSp = new double[numofSamples];
		this.labels = new double[numofSamples];
		
		for(int i = 0; i < numofSamples; i++) {
			weightsSp[i] = utils.div(1.00, (double)numofSamples, 2);
		}

	}
	public int getNumOfFeatures() {
		return numofFeatures;
	}
	
	public void append(String str) {
		raw.add(str);
	}
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
	 * This function is used for process input string.
	 */
	public void process() {
		for (int i = 0; i < this.raw.size(); i++) {
			String[] str = raw.get(i).split(" ");
			this.sentence.add(str); 
			// sentence: the list of string[] : 1: a, b, c, d, e
			//                                : 2: f, g, h, j, k
			//                                : 3: l, m, n, e, s
		}
	}
	
	public void getFeatures() {
		for (int i = 0; i < sentence.size(); i++) {
			this.ifContains_de(this.sentence.get(i), i);
			this.ifContains_en(this.sentence.get(i), i);
			this.ifContains_een(this.sentence.get(i), i);
			this.ifContains_het(this.sentence.get(i), i);
			this.ifContains_van(this.sentence.get(i), i);
			this.ifContains_op(this.sentence.get(i), i);
			this.ifContains_ij(this.sentence.get(i), i);
			this.ifContains_die(this.sentence.get(i), i);
			this.consonantsMoreThanFive(this.sentence.get(i), i);
//			this.averageLengthMoreThanFive(this.sentence.get(i), i);
		}
	}
	
	public void getLables() {
		
		System.out.println("what?");
		for (int i = 0; i < this.sentence.size(); i++) {
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
	public void consonantsMoreThanFive(String[] str, int indexSp) {
		double length = 0;
		for (int i = 0; i < str.length; i++) {
			length += str[i].length();
		}
		if ((length / (double)str.length) >= 5.0) {
			//System.out.print("The average is greater than 5; the length = " + (length / (double)str.length));
			samples[indexSp][8] = weightsSp[indexSp];
		}
	}
	public void averageLengthMoreThanFive(String[] str, int index) {
		
	}
}
