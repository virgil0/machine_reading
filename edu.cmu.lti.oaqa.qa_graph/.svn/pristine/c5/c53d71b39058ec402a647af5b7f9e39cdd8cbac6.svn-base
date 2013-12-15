package edu.cmu.lti.oaqa.graph.kbinterfaces;

public final class CanonicalFormChunk {
	private Concepts concept;
	private Blanks end;
	
	//example: Who is peyton manning's coach would be
			//concept=Concepts.coach, end=Blanks.PersonOrOrganization
	
	public CanonicalFormChunk(Concepts concept, Blanks end){
		this.concept=concept;
		this.end=end;
	}
	
	public Concepts getConcept(){
		return this.concept;
	}
	
	public Blanks getEnd(){
		return this.end;
	}
	
	public enum Blanks{
		PersonOrOrganization, //who
		Entity, //what?
		Location, //where?
		TimeorDate, //when?
		Enumerate, //how many?
		TrueFalse, //Does? Is?
	}
}

//who is peyton manning's coach?
//who is peyton manning's brother's coach?
//how many people work in the LTI?
//does Dr. Nyberg work in the LTI?
//what is the relationship between Allan Black and CMU?
//where is CMU?