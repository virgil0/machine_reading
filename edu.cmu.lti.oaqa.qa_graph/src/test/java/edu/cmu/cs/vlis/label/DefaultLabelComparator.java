package edu.cmu.cs.vlis.label;

public class DefaultLabelComparator extends AbstractLabelComparator {
	public DefaultLabelComparator(LabelSet labelSet) {
	    super(labelSet);
    }

	/** different labels are always not equal */
	@Override
    public double similarity(int label1, int label2) {
	    return label1 == label2 ? 1 : 0;
    }
}
