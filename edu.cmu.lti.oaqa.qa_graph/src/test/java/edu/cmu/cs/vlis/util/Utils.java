package edu.cmu.cs.vlis.util;

import java.util.*;
import edu.uci.ics.jung.graph.Graph;

public class Utils {
	public static class Pair<K, V> {
		private K key;
		private V value;
		
		public void setKey(K key) { 
			this.key = key; 
		}
		
		public K getKey() { 
			return key; 
		}
		
		public void setValue(V value) { 
			this.value = value; 
		}
		
		public V getValue() { 
			return value; 
		} 
		
		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		public int hashCode() {
			return key.hashCode() * 37 + value.hashCode();
		}
		
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if(!(obj instanceof Pair)) return false; 
			Pair<K, V> p = (Pair<K, V>) obj;
			return key.equals(p.key) && value.equals(p.value);
		}
		
		public String toString() {
			return "key = " + key + " ; value = " + value; 
		}
	}
	
	public static <E> List<E> shallowCopy(Collection<E> collection) {
		List<E> copy = new ArrayList<E>();
		for(E element : collection) 
			copy.add(element);
		return copy;
	}
	
	public static <K, V> Map<K, V> shallowCopy(Map<K, V> map) {
		Map<K ,V> copy = new HashMap<K, V>();
		copy.putAll(map);
		return copy;
	}
	
	@SuppressWarnings("unchecked")
	public static <V, E> Graph<V, E> shallowCopy(Graph<V, E> graph) 
			throws InstantiationException, IllegalAccessException {	
		Graph<V, E> copy = (Graph<V, E>) graph.getClass().newInstance();
		graphCopy(graph, copy);
		return copy;
	}
	
	@SuppressWarnings("unchecked")
	public static <V, E> Graph<V, E> shallowCopy(Graph<V, E> graph, Class<?> graphType) 
			throws InstantiationException, IllegalAccessException {	
		Graph<V, E> copy = (Graph<V, E>) graphType.newInstance();
		graphCopy(graph, copy);
		return copy;
	}
	
	private static <V, E> void graphCopy(Graph<V, E> src, Graph<V, E> dst) {
		for(V vertex : src.getVertices())
			dst.addVertex(vertex);
		
		for(E edge : src.getEdges()) {
			edu.uci.ics.jung.graph.util.Pair<V> vertexex = src.getEndpoints(edge);
			dst.addEdge(edge, 
					vertexex.getFirst(),
					vertexex.getSecond(), 
					src.getEdgeType(edge));
		}
	}

    /**
     *
     * @param elements
     * @param size
     * @param <T>
     * @return index array of randomly chosen elements
     */
    public static <T> int [] chooseElementsRandomly(List<T> elements, int size) {
        int [] index = new int [elements.size()];
        for(int i = 0; i < index.length; i++)
            index[i] = i;

        int [] selection = new int [size];
        int k = 0;

        Random ran = new Random();
        for(int i = 0; i < size; i++) {
            int next = ran.nextInt(index.length - k);
            selection[k] = index[next];
            swap(next, index.length - k - 1, index);
            k++;
        }

        return selection;
    }

    private static void swap(int i, int j, int [] index) {
        int v = index[i];
        index[i] = index[j];
        index[j] = v;
    }


    /**
     *
     * @param elements
     * @param <T>
     * @param size
     * @return all combinations (with given size) of elements, by subscript
     */
    public static <T> List<List<Integer>> combinations(List<T> elements, int size) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        choose(elements, 0, size, new ArrayList<Integer>(), ans);
        return ans;
    }

    private static <T> void choose(List<T> elements,
                                   int start,
                                   int size,
                                   List<Integer> curSelection,
                                   List<List<Integer>> allSelections) {
        if(curSelection.size() == size) {
            allSelections.add(shallowCopy(curSelection));
            return;
        }

        for(int i = start; i < elements.size(); i++) {
            curSelection.add(i);
            choose(elements, i + 1, size, curSelection, allSelections);
            curSelection.remove(curSelection.size() - 1);
        }
    }

    public static <T> List<T> toList(Collection<T> elements) {
        List<T> list = new ArrayList<T>();
        for(T element : elements)
            list.add(element);
        return list;
    }
}
