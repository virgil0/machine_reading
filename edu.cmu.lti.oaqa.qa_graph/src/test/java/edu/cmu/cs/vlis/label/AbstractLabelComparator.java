package edu.cmu.cs.vlis.label;

public abstract class AbstractLabelComparator {
	protected LabelSet labelSet;
	
	public AbstractLabelComparator(LabelSet labelSet) {
		this.labelSet = labelSet;
	}
	
	public abstract double similarity(int label1, int label2);
}
