package edu.cmu.cs.vlis.matching;

import java.util.*;
import edu.cmu.cs.vlis.util.Utils;
import edu.uci.ics.jung.graph.Graph;

/**
 * The exact matching algorithm, also known as the 
 * graph isomorphism algorithm.
 * The code is based on a straightforward implementation 
 * of VF2 algorithm.
 * 
 * @author morefree
 *
 */

public class ExactIsomorphism<V, E> extends Matching<V, E> {
	private ExactIsomorphismState<V, E> state;

	public ExactIsomorphism(Graph<V, E> pattern, Graph<V, E> target) {
        super(pattern, target);
		state = new ExactIsomorphismState<V, E>(pattern, target);
	}

    public ExactIsomorphism() {

    }
	
	public List<Map<V, V>> match() {
		mappings = new ArrayList<Map<V, V>>();
		
		// get any node in pattern
		V patternVertex = pattern.getVertices().iterator().next();
		
		// match patternVertex with every node from target. 
		// it avoids generating duplicate states.
		for(V targetVertex : target.getVertices())
			match(patternVertex, targetVertex);
		
		return mappings;
	}
	
	private void match(V patternVertex, V targetVertex) {
		if(!state.isConsistent(patternVertex, targetVertex))
			return;
		
		state.update(patternVertex, targetVertex);
		
		if(state.isFinished()) {
			mappings.add(Utils.<V, V>shallowCopy(state.getMappings()));
		} else {
			if(!matchSucc()) 
				if(!matchPred())
					matchOtherComponents();
		}
		
		state.restore(patternVertex, targetVertex);
	}
	
	private boolean matchSucc() {
		List<V> patternSucc =  Utils.<V>shallowCopy(state.patternSucc());
		List<V> targetSucc =  Utils.<V>shallowCopy(state.targetSucc());
		
		if(patternSucc.size() == 0 || targetSucc.size() == 0)
			return false;
		
		V m = patternSucc.get(0);
		state.removeFromPatternSucc(m);
		for(V n : targetSucc) {
			state.removeFromTargetSucc(n);
			match(m, n);
			state.addToTargetSucc(n);
		}
		state.addToPatternSucc(m);
		
		return true;
	}
	
	private boolean matchPred() {
		List<V> patternPred =  Utils.<V>shallowCopy(state.patternPred());
		List<V> targetPred =  Utils.<V>shallowCopy(state.targetPred());
		
		if(patternPred.size() == 0 || targetPred.size() == 0)
			return false;
		
		V m = patternPred.get(0);
		state.removeFromPatternPred(m);
		for(V n : targetPred) {
			state.removeFromTargetPred(n);
			match(m, n);
			state.addToTargetPred(n);
		}
		state.addToPatternPred(m);
		
		return true;
	}
	
	// not used for annotation graph matching, because in annotation graph matching,
	// a pattern graph has only one connected component
	private boolean matchOtherComponents() {
		List<V> patternOther =  Utils.<V>shallowCopy(state.patternOtherComponents());
		List<V> targetOther =  Utils.<V>shallowCopy(state.targetOtherComponents());
		
		if(patternOther.size() == 0 || targetOther.size() == 0)
			return false;
		
		V m = patternOther.get(0);
	
		for(V n : targetOther) {
			match(m, n);
		}
		
		return true;
	}
}
