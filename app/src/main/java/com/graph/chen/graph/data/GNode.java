package com.graph.chen.graph.data;

/**
 * Created by chen on 2016/7/12.
 * the node info of the graph
 */
public class GNode {
    public GEdge mNext;
    public float mPosx;
    public float mPosy;
    public int mIndex;
    public static final int RADIUS=15;
    public static int radius;
    public static final byte NORMAL_STATE=0;
    public static final byte VISITED_STATE=1;
    public static final byte DELETED_STATE=2;
    public int state;
    public GNode(int index){
        this.mIndex=index;
        mNext=null;
        state=NORMAL_STATE;
    }

    @Override
    public String toString() {
        return "GNode{" +
                "mNext=" + mNext +
                ", mPosx=" + mPosx +
                ", mPosy=" + mPosy +
                ", mIndex=" + mIndex +
                ", state=" + state +
                '}';
    }
}
