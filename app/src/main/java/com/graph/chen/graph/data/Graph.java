package com.graph.chen.graph.data;

import android.content.Context;
import android.support.v4.app.INotificationSideChannel;
import android.util.SparseArray;
import android.util.TypedValue;

import java.lang.annotation.Retention;
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
        int padding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,GNode.RADIUS,context.getResources().getDisplayMetrics());
        int c=width/2;
        int r=c-padding;
        float degree= (float) (2*Math.PI/mNodeNumber);
        GNode.radius=padding;
        for (int i=0;i<mNodeNumber;i++) {
            float t=degree*i;
            GNode node=mNodes.get(i);
            node.mPosx= (float) (c+r*Math.cos(t));
            node.mPosy= (float) (c+r*Math.sin(t));
        }
    }

    /**
     * depth first search of graph
     * @param start start node's index
     */
    public List<EdgeInfo> dfsVisit(int start) {
        List<EdgeInfo> visitOrder=new ArrayList<>(mNodes.size());
        boolean visited[]=new boolean[mNodeNumber];
        Arrays.fill(visited,false);
        dfsVisit(visitOrder,visited,start);
        if(visitOrder.size()==mNodeNumber){
            return visitOrder;
        }
        for(int i=0;i<mNodeNumber;i++){
            if(!visited[i]&&mNodes.get(i).state!=GNode.DELETED_STATE){
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
    private void dfsVisit(List<EdgeInfo> visitOrder, boolean visited[],int index) {
        GNode node=mNodes.get(index);
        GEdge edge=node.mNext;
        boolean flag=false;
        while(edge!=null){
            if(!visited[edge.mIndex]&&mNodes.get(edge.mIndex).state!=GNode.DELETED_STATE){
                visited[edge.mIndex]=true;
                visited[index]=true;
                visitOrder.add(new EdgeInfo(index,edge.mIndex));
                dfsVisit(visitOrder,visited,edge.mIndex);
                flag=true;
            }
            edge=edge.next;
        }
        if(!flag){
            visitOrder.add(new EdgeInfo(index,index));
            visited[index]=true;
        }
    }
    /**
     * breadth first search of graph
     * @param start start node's index
     */
    public List<EdgeInfo> bfsVisit(int start){
        List<EdgeInfo> visitOrder=new ArrayList<>(mNodes.size());
        boolean visited[]=new boolean[mNodeNumber];
        Arrays.fill(visited,false);
        bfsVisit(visitOrder,visited,start);
        if(visitOrder.size()==mNodeNumber){
            return visitOrder;
        }
        for(int i=0;i<mNodeNumber;i++){
            if(!visited[i]&&mNodes.get(i).state!=GNode.DELETED_STATE){
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
    private void bfsVisit(List<EdgeInfo> visitOrder, boolean visited[],int index){
        Queue<GNode> back=new LinkedList<>();
        back.add(mNodes.get(index));
        while (!back.isEmpty()){
            GNode node=back.poll();
            boolean flag=false;
            for(GEdge edge=node.mNext;edge!=null;edge=edge.next){
                if(!visited[edge.mIndex]&&mNodes.get(edge.mIndex).state!=GNode.DELETED_STATE) {
                    flag=true;
                    back.add(mNodes.get(edge.mIndex));
                    visitOrder.add(new EdgeInfo(node.mIndex,edge.mIndex));
                    visited[edge.mIndex] = true;
                    visited[index]=true;
                }
            }
            if(!flag){
                visited[index]=true;
                visitOrder.add(new EdgeInfo(index,index));
                continue;
            }
        }
    }

    public List<GNode> getmNodes() {
        return mNodes;
    }
}