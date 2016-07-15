package com.graph.chen.graph;

import com.graph.chen.graph.data.EdgeInfo;
import com.graph.chen.graph.data.GEdge;
import com.graph.chen.graph.data.Graph;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2016/7/14.
 */
public class GraphTest {
    @Test
    public void dfsVisit(){
        Graph graph=new Graph();
        List t=new ArrayList();
        t.add(new EdgeInfo(1,2));
        t.add(new EdgeInfo(1,2));
        t.add(new EdgeInfo(2,3));
        t.add(new EdgeInfo(3,4));
        graph.createGraph(t,5);
        graph.dfsVisit(0);
        graph.bfsVisit(0);
    }
}
