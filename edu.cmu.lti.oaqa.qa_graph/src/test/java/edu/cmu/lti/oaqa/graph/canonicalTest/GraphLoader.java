package edu.cmu.lti.oaqa.graph.canonicalTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.rexster.RexsterGraph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.graphml.Key;

class TagInfo{
   public int DocNo;
   public int tagID;
   public String tagValue;
   public int offset;
   public int length;
   public int parentTagID;
   public String pieceOfText;
}

public class GraphLoader {

  public int edgeCount;
  RexsterGraph graph;
  public ArrayList<TagInfo> nodeList;
  public GraphLoader(){
    nodeList = new ArrayList<TagInfo>();
    edgeCount = 0;
    graph = new RexsterGraph("http://localhost:8182/graphs/qa_graph");
  }
  
  public void load(String filename){
    BufferedReader br = null;
    int target = 11;
    int curNo = 1;
    try {
      String sCurrentLine;
      br = new BufferedReader(new FileReader(filename));
    
      while ((sCurrentLine = br.readLine()) != null) {
        String []words = sCurrentLine.split("[\t]");
        TagInfo tag = new TagInfo();
        tag.DocNo = Integer.parseInt(words[0]);
        if(tag.DocNo != curNo){
          System.out.println("Building No. "+ curNo + "..");
          final long startTime = System.currentTimeMillis();
          buildGraph();
          final long stopTime = System.currentTimeMillis();
          System.out.println("Cosume time: "+ (stopTime - startTime));
          nodeList.clear();
          curNo = tag.DocNo;
          if(curNo == target){
            break;
          }
        }   
        tag.tagID = Integer.parseInt(words[2]);
        tag.tagValue = words[3];
        tag.offset = Integer.parseInt(words[4]);
        tag.length = Integer.parseInt(words[5]);
        tag.parentTagID = Integer.parseInt(words[7]);
        tag.pieceOfText = words[8];
        nodeList.add(tag);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)br.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
  
  public void buildGraph(){
    HashMap<String, Vertex> vhm = new HashMap<String, Vertex>();
    System.out.println("node:" + nodeList.size());
    for(int i =0; i< nodeList.size(); i++){
      Vertex tmp = graph.addVertex(null);
      tmp.setProperty("DocNo", nodeList.get(i).DocNo);
      tmp.setProperty("tagID", nodeList.get(i).tagID);
      tmp.setProperty("tagValue", nodeList.get(i).tagValue);
      tmp.setProperty("offset", nodeList.get(i).offset);
      tmp.setProperty("length", nodeList.get(i).length);
      tmp.setProperty("parentTagID", nodeList.get(i).parentTagID);
      tmp.setProperty("pieceOfText", nodeList.get(i).pieceOfText);
      vhm.put(nodeList.get(i).DocNo + "_"+ nodeList.get(i).tagID, tmp);
    }
    edgeCount =0;
    for(int i =0; i< nodeList.size(); i++){
      if(nodeList.get(i).parentTagID == 0)
          continue;
      Vertex child = vhm.get(nodeList.get(i).DocNo + "_" + nodeList.get(i).tagID);
      Vertex parent = vhm.get(nodeList.get(i).DocNo + "_" + nodeList.get(i).parentTagID);
      Edge e = graph.addEdge(null, parent, child, "parent");
      edgeCount++;
    }
    System.out.println("Edge:" + edgeCount);
  }
  
  public List<Graph<Vertex, Object>> findAndBuildGraph(String key, Object value){
     ArrayList<Graph<Vertex, Object>> al = new ArrayList<Graph<Vertex, Object>>();
   //  graph.createKeyIndex( "DocNo", Vertex.class);
      //graph.makeKey("name").dataType(String.class).indexed(Vertex.class).unique().make();
      Iterator<Vertex> itor = graph.getVertices(key, value).iterator();
      int i = 0;
      while(itor.hasNext()){
       
        Vertex begin = itor.next();
        Graph<Vertex, Object> sm = new SparseMultigraph<Vertex, Object>();
        System.out.println(begin.getProperty("pieceOfText"));
        sm.addVertex(begin);
        for(Edge e : begin.query().edges()) { 
          addMore(begin, e, sm);
        }
        al.add(sm);
      }
    
      return al;
  }
    
  public void addMore(Vertex begin,  Edge e, Graph graph){
      
      Vertex current;
  //    boolean outFlag ;
      if(begin.getProperty("tagID") != e.getVertex(Direction.OUT).getProperty("tagID")){
        current = e.getVertex(Direction.OUT);
     //   outFlag = true;
      }
      else{
        current = e.getVertex(Direction.IN);
   //     outFlag = false;
      }
      if(graph.containsVertex(current)){
        return;
      }
      System.out.println(current.getProperty("DocNo").toString()+ current.getProperty("pieceOfText"));
      graph.addVertex(current);
    
       graph.addEdge(begin.getProperty("pieceOfText").toString()+current.getProperty("pieceOfText"), current, begin, EdgeType.DIRECTED);
      
      Iterator<Edge> edges = current.query().edges().iterator();
      while(edges.hasNext()){
        Edge ed = edges.next();
        addMore(current, ed, graph);
      }
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    GraphLoader ct = new GraphLoader();
   // List<Graph<Vertex, Object>> al = ct.findAndBuildGraph("pieceOfText", "The film");
    final long startTime = System.currentTimeMillis();
   ct.load("/Users/virgil/Documents/fall13/Capstone/senna_annot_n.tsv");
//   final long readEndTime = System.currentTimeMillis();
 //  System.out.println("Total reading time:" + (readEndTime - startTime));
//    ct.buildGraph();
    final long endTime = System.currentTimeMillis();
    System.out.println("Total store time:" + (endTime - startTime));
  }

}
