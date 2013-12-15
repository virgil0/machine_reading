package edu.cmu.cs.vlis.matcher;

import edu.cmu.cs.vlis.label.AbstractLabelComparator;
import edu.cmu.cs.vlis.label.DefaultLabelComparator;
import edu.cmu.cs.vlis.result.MatchingResult;
import edu.uci.ics.jung.graph.Graph;
import edu.cmu.cs.vlis.matching.Matching;
import java.util.Map;
import java.util.List;

/**
 * 
 * @author morefree
 *
 * @param <V> type of graph nodes
 * @param <E> type of graph edges
 * @param <T> type of matching results
 */
public abstract class GraphMatcher<V, E, T> {
	protected AbstractLabelComparator labelComparator;
    protected Matching<V, E> matching;
    protected List<Map<V, V>> mappings;
	
	public GraphMatcher(AbstractLabelComparator comparator) {
		this.labelComparator = comparator;
	}
	
	public GraphMatcher() {
		// do not need any label set
		this.labelComparator = new DefaultLabelComparator(null); 
	}

    public void setMatching(Matching<V, E> matching) {
        this.matching = matching;
    }

    /**
     * this method should only be invoked after match(Graph, Graph) method is called.
     * @return node to node mappings between pattern and graph. this method is especially
     * useful when matching algorithm is some kind of isomorphism.
     */
    public List<Map<V, V>> getMappings() {
        return mappings;
    }

    /**
     *
     * @param pattern
     * @param target
     * @return match result. typically it is a list of graphs, where each graph is a
     * sub-graph of target which partially or fully matches the target
     */
	public abstract MatchingResult<T> match(Graph<V, E> pattern, Graph<V, E> target);
}
