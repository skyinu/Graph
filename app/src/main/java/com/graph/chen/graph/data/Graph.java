package com.graph.chen.graph.data;

import android.content.Context;
import android.util.SparseArray;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by chen on 2016/7/12.
 * the data structure of graph
 */
public class Graph {
    private List<GNode> mNodes;
    private int mNodeNumber;
    public Graph() {
    }

    public Graph(List<EdgeInfo> datas, int nodeNumber) {
        createGraph(datas, nodeNumber);

    }


    /**
     * create a undirected graph
     * @param datas the list contains the edge info
     * @param nodeNumber the number of the node
     */
    public void createGraph(List<EdgeInfo> datas, int nodeNumber) {
        this.mNodeNumber = nodeNumber;
        mNodes = new ArrayList<>(nodeNumber);
        for (int i = 0; i < nodeNumber; i++) {
            mNodes.add(new GNode(i));
        }
        for (EdgeInfo info : datas) {
            GNode sn = mNodes.get(info.mStart);
            GNode en = mNodes.get(info.mEnd);
            //first edge
            GEdge edge = new GEdge(info.mEnd);
            edge.next = sn.mNext;
            sn.mNext = edge;
            //second edge
            edge = new GEdge(info.mStart);
            edge.next = en.mNext;
            en.mNext = edge;
        }
    }

    /**
     * 计算各个节点的坐标
     * @param context
     * @param width
     */
    public void calculateCoordiate(Context context,int width) {
        int padding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,context.getResources().getDisplayMetrics());
        int r=width/2-padding;
        float degree= (float) (2*Math.PI/mNodeNumber);
        for (int i=0;i<mNodeNumber;i++) {
            float t=degree*i;
            GNode node=mNodes.get(i);
            node.mPosx= (float) (r+r*Math.cos(t));
            node.mPosy= (float) (r+r*Math.sin(t));
        }
    }

    /**
     * depth first search of graph
     * @param start start node's index
     */
    public List<Integer> dfsVisit(int start) {
        List<Integer> visitOrder=new ArrayList<>(mNodes.size());
        boolean visited[]=new boolean[mNodeNumber];
        Arrays.fill(visited,false);
        visited[start]=true;
        visitOrder.add(start);
        dfsVisit(visitOrder,visited,start);
        if(visitOrder.size()==mNodeNumber){
            return visitOrder;
        }
        for(int i=0;i<mNodeNumber;i++){
            if(!visited[i]){
                visited[i]=true;
                visitOrder.add(i);
                dfsVisit(visitOrder,visited,i);
            }
        }
        return visitOrder;
    }

    /**
     * recode the node,then visit the node
     * @param visitOrder
     * @param visited
     * @param index
     */
    private void dfsVisit(List<Integer> visitOrder, boolean visited[],int index) {
        GNode node=mNodes.get(index);
        GEdge edge=node.mNext;
        while(edge!=null){
            if(!visited[edge.mIndex]){
                visited[edge.mIndex]=true;
                visitOrder.add(edge.mIndex);
                dfsVisit(visitOrder,visited,edge.mIndex);
            }
            edge=edge.next;
        }
    }
    /**
     * breadth first search of graph
     * @param start start node's index
     */
    public List<Integer> bfsVisit(int start){
        List<Integer> visitOrder=new ArrayList<>(mNodes.size());
        boolean visited[]=new boolean[mNodeNumber];
        Arrays.fill(visited,false);
        visited[start]=true;
        visitOrder.add(start);
        bfsVisit(visitOrder,visited,start);
        if(visitOrder.size()==mNodeNumber){
            return visitOrder;
        }
        for(int i=0;i<mNodeNumber;i++){
            if(!visited[i]){
                visited[i]=true;
                visitOrder.add(i);
                bfsVisit(visitOrder,visited,i);
            }
        }
        return visitOrder;
    }
    /**
     * recode the node,then visit the node
     * @param visitOrder
     * @param visited
     * @param index
     */
    private void bfsVisit(List<Integer> visitOrder, boolean visited[],int index){
        Queue<GNode> back=new LinkedList<>();
        back.add(mNodes.get(index));
        while (!back.isEmpty()){
            GNode node=back.poll();
            for(GEdge edge=node.mNext;edge!=null;edge=edge.next){
                if(!visited[edge.mIndex]) {
                    back.add(mNodes.get(edge.mIndex));
                    visitOrder.add(edge.mIndex);
                    visited[edge.mIndex] = true;
                }
            }
        }
    }

    public List<GNode> getmNodes() {
        return mNodes;
    }
}
