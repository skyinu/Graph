package com.graph.chen.graph.data;

/**
 * Created by chen on 2016/7/12.
 * the edge info of the graph
 */
public class GEdge {
    public int mIndex;
    public GEdge next;
    public GEdge(int index){
        this.mIndex=index;
    }
}
