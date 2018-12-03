import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



public class DecisionTree {
	int numofSamples;
	int numofFeatures;
	
	double[][] orgSamples;
	double[] orgLabels;
	Object[][] rawData;
	
	String[] attrNames = new String[] {"ContainsDE", "ContainsEN", "ContainsEEN", "ContainsHET","ContainsVAN", 
			"ContainsOP","ContainsIJ","ContainsDIE","Length>0.5","Cons>3.5"};
	
	public DecisionTree(Sample sample) {
		this.numofSamples = sample.numofSamples;
		this.numofFeatures = sample.numofFeatures;
		
		this.orgSamples = sample.samples;
		this.orgLabels = sample.labels;
		
		this.rawData = new Object[numofSamples][numofFeatures + 1];
	}
	
	/**
	 * The function used for set up the whole table into String type.
	 * 
	 */
	public void setRawdate() {
		for (int i = 0; i < numofSamples; i++) {
			for (int j = 0; j < numofFeatures + 1; j++) {
				if (j < numofFeatures) {
					if(orgSamples[i][j] > 0) rawData[i][j] = "1";
					else rawData[i][j] = "-1";
				}
				else {
					if(orgLabels[i] > 0) rawData[i][j] = "1";
					else rawData[i][j] = "0";
				}
			}
		}
	}
	
	public void printRawdata() {
		for (int i = 0; i < numofSamples; i++) {
			for (int j = 0; j < numofFeatures + 1; j++) {
				if (j < numofFeatures) 
					System.out.print(rawData[i][j] + "    ");
				else 
					System.out.print(rawData[i][j]+ "    ");
			}
			System.out.println();
		}
	}
	
	public Map<Object, List<TreeSample>> readSamples(String[] attrNames) {
		
		// Create a map categorized by the first feature
		Map<Object, List<TreeSample>> ret = new HashMap<Object, List<TreeSample>>();
		
		// Categorize the each feature.
		for (Object[] row : rawData) {
			TreeSample sample = new TreeSample();
			int i = 0;
			for (int n = row.length - 1; i < n; i++)
				sample.setAttribute(attrNames[i], row[i]);
			sample.setCategory(row[i]);
			List<TreeSample> samples = ret.get(row[i]);
			if (samples == null) {
				samples = new LinkedList<TreeSample>();
				ret.put(row[i], samples);
			}
			samples.add(sample);
		}
		return ret;
	}
}

// A Node class for store the all its 
class TreeSample{
	private Map<String, Object> attributes = new HashMap<String, Object>();
	 
	private Object category;

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public Object getCategory() {
		return category;
	}

	public void setCategory(Object category) {
		this.category = category;
	}

	public String toString() {
		return attributes.toString();
	}

}


class Tree {
	
	private String attribute;
	
	private Map<Object, Object> children = new HashMap<Object, Object>();
	
	public Tree(String attribute) {
		this.attribute = attribute;
	}
	
	public String getAttribute() {
		return attribute;
	}
	
	public Object getChild(Object attraValue) {
		return children.get(attraValue);
	}
	
	public void setChild(Object attraValue, Object child) {
		children.put(attraValue, child);
	}
	
	public Set<Object> getAttributeValues(){
		return children.keySet();
	}
}