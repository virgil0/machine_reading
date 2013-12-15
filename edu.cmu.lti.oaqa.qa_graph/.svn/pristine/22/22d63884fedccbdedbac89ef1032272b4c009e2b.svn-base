package edu.cmu.lti.oaqa.graph.kbinterfaces;

import java.util.List;

public interface KBStrategy {
		
	public List<String> listKnownRelationships();
	public List<String> listKnownTypes();
	
	public boolean compliantWithMasterList(String masterlistFile);
	public List<String> relationshipsNotInMasterList();
	public List<String> typesNotInMasterList();
	
	public boolean kbKnowsAboutType(String type);
	public boolean kbKnowsAboutRelationship(String rel);
	
	public CanonicalForm generateCanonicalForm(String question);
	
	public List<String> getAllStrategies();
	public void queryKB(CanonicalForm canonical,String strategy);
	
}
