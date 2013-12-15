package edu.cmu.cs.vlis.adapter;

/**
 * used for work with Titan only.
 * see Fei's code for more info.
 */
public class Vertex {
    private TagInfo tagInfo;

    Vertex(TagInfo tagInfo) {
        this.tagInfo = tagInfo;
    }

    Object getProperty(String property) {
        return null; //
    }
}