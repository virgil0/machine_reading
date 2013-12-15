package edu.cmu.cs.vlis.matching;

import java.util.List;
import java.util.Map;
import edu.uci.ics.jung.graph.Graph;

/**
 * base class for all matching algorithms
 * @param <V>
 * @param <E>
 */
public abstract class Matching <V, E> {
    protected Graph<V, E> pattern, target;
    protected List<Map<V, V>> mappings;

    public Matching() {

    }
    
    public void init() {
    	
    }
    
    public void clean() {
    	
    }

    public Matching(Graph<V, E> pattern, Graph<V, E> target) {
        this.pattern = pattern;
        this.target = target;
    }

    public Graph<V, E> getPattern() {
        return pattern;
    }

    public Graph<V, E> getTarget() {
        return target;
    }

    public void setPattern(Graph<V, E> pattern) {
        this.pattern = pattern;
    }

    public void setTarget(Graph<V, E> target) {
        this.target = target;
    }

    /**
     * @return matching result between pattern and target
     */
    public abstract  List<Map<V, V>> match();
}
