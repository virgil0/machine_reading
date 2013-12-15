package edu.cmu.cs.vlis.adapter;

/**
 * Created with IntelliJ IDEA.
 * User: morefree
 * Date: 11/18/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 *
 * An instance of IndriGraphNode stands for a single line in the output file with the format :
 *  <DocNo> "TAG" <tagID> <tagValue> <offset> <length> <parentTagID> <piece of text>
 */
public abstract class IndriGraphNode {
    public int docID;
    public int tagID;
    public String tagValue;
    public int offset;
    public int length;
    public int parentTagID;
    public String text;

    public abstract boolean equals(Object obj);

    public int hashCode() {
        return docID * 37 + tagID;
    }
}