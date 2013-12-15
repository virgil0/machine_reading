package edu.cmu.cs.vlis.matching;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import edu.cmu.cs.vlis.util.Utils;
import edu.uci.ics.jung.graph.Graph;

/**
 * the internal state representation introduced by VF2 algorithm
 * 
 * @author morefree
 *
 */
class ExactIsomorphismState<V, E> implements MatchingState<V, E> {
	private State patternState;
	private State targetState;	
	private Map<V, V> partialMapping;
	
	Map<V, V> getMappings() { 
		return partialMapping; 
	}
	
	State getPatternState() {
		return patternState;
	}
	
	State getTargetState() {
		return targetState;
	}
		
	ExactIsomorphismState(Graph<V, E> pattern, Graph<V, E> target) {
		patternState = new State (pattern);
		targetState = new State (target);
		partialMapping = new HashMap<V, V> ();
	}
	
	void update(V patternVertex, V targetVertex) {
		patternState.updatePred(patternVertex);
		patternState.updateSucc(patternVertex);
		
		targetState.updatePred(targetVertex);
		targetState.updateSucc(targetVertex);
		
		if(!patternState.sol.contains(patternVertex) && 
				!targetState.sol.contains(targetVertex)) {
			patternState.sol.add(patternVertex);
			targetState.sol.add(targetVertex);
			
			partialMapping.put(patternVertex, targetVertex);
		}
	}
	
	// for certain cases, it deletes successors generated by predecessors of patternVertex 
	// rather than patternVertex itself.  This is the desirable behavior since it avoids 
	// generating duplicate states.
	void restore(V patternVertex, V targetVertex) {
		patternState.removeAllPreds(patternVertex);
		patternState.removeAllSuccs(patternVertex);
		patternState.sol.remove(patternVertex);
		
		targetState.removeAllPreds(targetVertex);
		targetState.removeAllSuccs(targetVertex);
		targetState.sol.remove(targetVertex);
		
		partialMapping.remove(patternVertex);
	}
	
	boolean isFinished() {
		return patternState.graph.getVertexCount() == patternState.sol.size() ||
				targetState.graph.getVertexCount() == targetState.sol.size();
	}
	
	Set<V> patternPred() {
		return patternState.pred;
	}
	
	Set<V> targetPred() {
		return targetState.pred;
	}
	
	Set<V> patternSucc() {
		return patternState.succ;
	}
	
	Set<V> targetSucc() {
		return targetState.succ;
	}
	
	// not used for annotation graph matching
	Set<V> patternOtherComponents() {
		return new HashSet<V>();
	}
	
	// not used for annotation graph matching
	Set<V> targetOtherComponents() {
		return new HashSet<V>();
	}
	
	void removeFromPatternPred(V vertex) {
		patternState.pred.remove(vertex);
	}
	
	void removeFromTargetPred(V vertex) {
		targetState.pred.remove(vertex);
	}
	
	void removeFromPatternSucc(V vertex) {
		patternState.succ.remove(vertex);
	}
	
	void removeFromTargetSucc(V vertex) {
		targetState.succ.remove(vertex);
	}
	
	void removeFromPatternSol(V vertex) {
		patternState.sol.remove(vertex);
	}
	
	void removeFromTargetSol(V vertex) {
		targetState.sol.remove(vertex);
	}
	
	void addToPatternPred(V vertex) {
		patternState.pred.add(vertex);
	}
	
	void addToTargetPred(V vertex) {
		targetState.pred.add(vertex);
	}
	
	void addToPatternSucc(V vertex) {
		patternState.succ.add(vertex);
	}
	
	void addToTargetSucc(V vertex) {
		targetState.succ.add(vertex);
	}
	
	void addToPatternSol(V vertex) {
		patternState.sol.add(vertex);
	}
	
	void addToTargetSol(V vertex) {
		targetState.sol.add(vertex);
	}
	
	// TODO apply more look-ahead feasibility rules to accelerate the program
	boolean isConsistent(V patternVertex, V targetVertex) {
		if(patternState.sol.size() == 0) 
			return true;
	
		if(patternState.sol.contains(patternVertex) || 
				targetState.sol.contains(targetVertex))
			return false;
		
		// predecessor feasibility rule
		List<V> patternPreds = Utils.shallowCopy
				( patternState.graph.getPredecessors(patternVertex) );
		for(V patternPred : patternPreds) {
			if(patternState.sol.contains(patternPred)) {
				if(!targetState.graph.isPredecessor(targetVertex, 
													partialMapping.get(patternPred)))
					return false;
			}
		}
		
		// successor feasibility rule
		List<V> patternSuccs = Utils.shallowCopy
				( patternState.graph.getSuccessors(patternVertex) );
		for(V patternSucc : patternSuccs) {
			if(patternState.sol.contains(patternSucc)) {
				if(!targetState.graph.isSuccessor(targetVertex, 
												  partialMapping.get(patternSucc)))
					return false;
			}
		}
		
		return true;
	}
	
	private class State {
		Set<V> pred;
		Set<V> succ;
		Set<V> sol;
		Graph<V, E> graph;
		
		State(Graph<V, E> graph) {
			this.graph = graph;
			pred = new HashSet<V>();
			succ = new HashSet<V>();
			sol = new HashSet<V>();
		}
		
		void updatePred(V vertex) {
			for(V nextPred : graph.getPredecessors(vertex)) 
				if(!sol.contains(nextPred))
					pred.add(nextPred);
		}
		
		void updateSucc(V vertex) {
			for(V nextSucc : graph.getSuccessors(vertex))
				if(!sol.contains(nextSucc))
					succ.add(nextSucc);
		}
			
		void removeAllPreds(V vertex) {
			pred.removeAll(graph.getPredecessors(vertex));
		}
		
		void removeAllSuccs(V vertex) {
			succ.removeAll(graph.getSuccessors(vertex));
		}
	}
}
