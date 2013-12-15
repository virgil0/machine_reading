Graph Based QA System
===============
Usage for Graph Database Access Code

Server Install Instruction

1. Download a copy of the current titan- server-VERSION.zip file from the Downloads page(https://github.com/thinkaurelius/titan
/wiki/Downloads). Unzip it and enter the titan- server-VERSION directory
2. Run bin/titan.sh start to start a Rexster server with Titan + Cassandra; Connect to Rexster us- ing bin/rexster-console.sh, the REST API, or an application using RexsterClient
3. Run bin/titan.sh status to check on the forked processes and bin/titan.sh stop to kill them

Run Instruction

All the code are located in class called GraphLoader, packaged in edu.cmu.lti.oaqa.graph.canonicalTest. The following table explains the usage of each functions.
• void load(String filename): Load annotation file into memory
• void buildGraph(): Form annotations to graph and store in graph DB
• List<Graph<Vertex, Ob ject>> findAndBuild- Graph(String key, Object value): Based on the property of vertex and the property value, find out the graph containing this kind of key-value pair vertex, which is useful for graph matching.

Usage for Graph Matching Package (edu.cmu.cs.vlis.*)

Using this package is straightforward. All source files are written in Java 6, and fully 
commented. 

Particularly, it uses JUNG (http://jung.sourceforge.net/) to represent the underlying 
graph structure, so corresponding jars are required to run this package. 
Jars can be downloaded here :
http://sourceforge.net/projects/jung/files/ 
All source files are written with "jung2-2_0_1.zip" 

Also, in order to run the Hadoop example (edu.cmu.cs.vlis.examples.IndriGraphMatchWithHadoop), 
core jars for Hadoop (http://hadoop.apache.org/releases.html) is required 
(the source code uses hadoop version 1.1.2).

The project should already contain all required jars for this package, so users do not need
to manually add the dependencies. 