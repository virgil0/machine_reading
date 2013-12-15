package edu.cmu.lti.oaqa.graph.kbinterfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.lti.oaqa.graph.core.load.TextFileReader;
import edu.cmu.lti.oaqa.graph.core.load.TextFileWriter;

public class EnumsManager {
	
	private String classLocation = EnumsManager.class.getResource("EnumsManager.class").getPath()
			.replaceAll("EnumsManager.class", "").replaceAll("target/classes", "src/main/java");
	
	public static void main(String[] args) throws IOException{	
		new EnumsManager().buildEnums();
	}
	
	private void buildEnums() throws IOException {
		buildRelationshipsEnum();
		buildConceptsEnum();
	}

	public void buildRelationshipsEnum() throws IOException{
		buildEnum(classLocation+"RelationshipsEnum_Saved.txt",classLocation+"Relationships.java");
	}
	
	public void buildConceptsEnum() throws IOException{
		buildEnum(classLocation+"ConceptsEnum_Saved.txt",classLocation+"Concepts.java");
	}
	
	public void addRelationships(List<String> additions) throws IOException{
		addToCSV(classLocation+"/RelationshipsEnum_Saved.txt",additions);
		buildRelationshipsEnum();
	}
	
	public void addConcepts(List<String> additions) throws IOException{
		addToCSV(classLocation+"/ConceptsEnum_Saved.txt",additions);
		buildConceptsEnum();
	}
	
	private void addToCSV(String txtFile, List<String> additions) throws IOException{
		List<String> existingItems = new ArrayList<String>();
		TextFileReader reader = new TextFileReader(txtFile);
		String fileContents ="";
		while(reader.hasNext()){
			String next = reader.next();
			fileContents +=next+"\n";
			existingItems.add(next);
		}
		fileContents=fileContents.trim()+"\n";
		for(String addition:additions){
			if(!existingItems.contains(addition)) fileContents+=addition+"\n";
		}
		fileContents=fileContents.trim();
		TextFileWriter.clearFile(txtFile);
		TextFileWriter.writeWholeFile(txtFile, fileContents);
	}
	
	private void buildEnum(String txtFile, String enumFile) throws IOException{
		TextFileWriter.clearFile(enumFile);
		TextFileWriter writer = new TextFileWriter(enumFile);
		TextFileReader reader = new TextFileReader(txtFile);
		
		//String currentLocation = System.getProperty("user.dir");
		
		String packageName = EnumsManager.class.getPackage().getName();
		
		String enumName = enumFile.replaceAll(this.classLocation,"").replaceAll(".java", "");
		
		String introLines = "package "+ packageName + ";\n\npublic enum " + enumName +" {\n";
		String closingLines = "\n}";
		
		writer.next(introLines);
		while(reader.hasNext()){
			writer.next("\t"+reader.next()+",");
		}
		writer.next(closingLines);
		writer.destroy();
	} 

}
