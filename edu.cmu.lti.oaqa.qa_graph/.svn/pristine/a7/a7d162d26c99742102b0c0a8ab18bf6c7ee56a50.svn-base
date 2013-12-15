package edu.cmu.lti.oaqa.graph.kbinterfaces;

import java.util.ArrayList;

import com.google.common.collect.Iterables;

public final class CanonicalForm<T> {
  
	private Iterable<CanonicalFormChunk> chunks = new ArrayList<CanonicalFormChunk>();
	private String startingPoint;
	
	
	//make CanonicalForm made up of smaller CanonicalFormChunks
	public CanonicalForm(String startingPoint, Iterable<CanonicalFormChunk> chunks){
		this.startingPoint=startingPoint;
		this.chunks=chunks;
	}
	
	public String getStartingPoint(){
		return this.startingPoint;
	}
	
	public Iterable<CanonicalFormChunk> getChunks(){
		return Iterables.unmodifiableIterable(chunks);
	}

}
