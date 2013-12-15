package edu.cmu.lti.oaqa.graph.canonical;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class testText {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> list = Lists.newArrayList();
		
		System.out.println(makePlainText(list));
	}
	
	private static String makePlainText(Iterable<String> strings) {
		ArrayList<String> stringsList = Lists.newArrayList(strings);
		String output="";
		if(stringsList.size()<2){
			for(String answer:stringsList){
				output+=answer;
			}
		}
		else if(stringsList.size()==2){
			output=stringsList.get(0)+" and "+stringsList.get(1);
		}
		else{
			for(int x = 0; x<=stringsList.size()-2; x++){
				output+=stringsList.get(x)+", ";
			}
			output+="and "+stringsList.get(stringsList.size()-1);
		}
		return output;
	}

}
