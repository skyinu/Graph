package com.graph.chen.graph.action;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.graph.chen.graph.data.EdgeInfo;
import com.graph.chen.graph.data.GEdge;
import com.graph.chen.graph.data.GNode;
import com.graph.chen.graph.data.Graph;
import com.graph.chen.graph.view.GraphSurfaceView;

import java.util.List;

/**
 * Created by chen on 2016/7/14.
 */
public class DrawImple extends Handler implements Runnable{
    /**
     * 绘图线程
     */
    private Thread mDrawThread;
    /**
     * 随着SurfaceView生命周期而变动
     */
    private boolean isSurfaceAlive;
    /**
     * 标志是否需要绘图
     */
    private boolean isShouldDraw;
    private boolean hasChanged;
    /**
     * 无向图
     */
    private Graph mGraph;
    private GraphSurfaceView mSurfaceView;
    /**
     * 创建无向图使用的边的信息
     */
    private List<EdgeInfo> mEdgeInfo;
    /**
     * 各种状态对应的画笔
     */
    private Paint mNormalNodePaint;
    private Paint mVisitedNodePaint;
    private Paint mNormalEdgePaint;
    private Paint mVisitedEdgePaint;
    private Paint mDeletedEdgePaint;
    private Paint mTextPaint;
    /**
     * 双缓冲使用的bitmap
     */
    private Bitmap mBackBitmap,mCurrentBitmap;
    /**
     * 对应缓冲图片的画家
     */
    private Canvas mBackCanvas,mCurrentCanvas;

    private byte mCommand;
    public static final byte COMMAND_INIT=0;
    public static final byte COMMAND_DELETE=1;
    public static final byte COMMAND_BFS=2;
    public static final byte COMMAND_DFS=3;
    /**
     * 遍历、删除的开始点
     */
    private GNode mStartNode;
    /**
     * 开始绘图线程并初始化画笔
     * @param surface
     */
    public void startdrawThread(GraphSurfaceView surface){
        this.mSurfaceView=surface;
        isSurfaceAlive=true;
        InitPaint();
        mBackBitmap=Bitmap.createBitmap(surface.getMeasuredWidth(),surface.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mBackCanvas=new Canvas(mBackBitmap);
        mCurrentBitmap=Bitmap.createBitmap(surface.getMeasuredWidth(),surface.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCurrentCanvas=new Canvas(mCurrentBitmap);
        mDrawThread=new Thread(this);
        mDrawThread.start();
        hasChanged=true;
    }

    /**
     * 初始化各种类型的画笔
     */
    private void InitPaint(){
        mNormalNodePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mVisitedNodePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalEdgePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mVisitedEdgePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mDeletedEdgePaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        mNormalNodePaint.setColor(GraphSurfaceView.mNormalNodeColor);
        mNormalNodePaint.setStyle(Paint.Style.STROKE);
        mNormalNodePaint.setStrokeWidth(5);


        mVisitedNodePaint.setColor(GraphSurfaceView.mVisitNodeColor);
        mVisitedNodePaint.setStyle(Paint.Style.STROKE);

        mNormalEdgePaint.setColor(GraphSurfaceView.mNormalEdgeColor);
        mNormalEdgePaint.setStrokeWidth(8);
        mVisitedEdgePaint.setColor(GraphSurfaceView.mVisitEdgeColor);
        mDeletedEdgePaint.setColor(GraphSurfaceView.mDeletedNodeColor);

        mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(15);
    }

    @Override
    public void run() {
        SurfaceHolder holder=mSurfaceView.getHolder();
        Canvas canvas=holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        holder.unlockCanvasAndPost(canvas);
        while(isSurfaceAlive){
            while (!isShouldDraw);
            prepareDraw(mCurrentCanvas);
            if(mCommand==COMMAND_INIT){
                drawInitGraphEdges(holder,mCurrentCanvas,false);
            }
            else{
                drawInitGraphEdges(holder,mCurrentCanvas,true);
                if(mCommand==COMMAND_DELETE){
                    
                }
                else{

                }
            }
            checkIfChanged(mBackCanvas);
            isShouldDraw=false;
        }
    }

    /**
     * 用于创建时绘制无向图的边
     * @param holder
     * @param mCurrentCanvas
     * @param instance
     */
    private void drawInitGraphEdges(SurfaceHolder holder, Canvas mCurrentCanvas, boolean instance){
        List<GNode> nodes=mGraph.getmNodes();
        for (EdgeInfo info:mEdgeInfo){
            drawEdge(holder,mCurrentCanvas,nodes.get(info.mStart),nodes.get(info.mEnd),instance);
        }
    }

    /**
     * 准备静态的背景,绘制于图片之上
     * @param canvas
     */
    private void prepareDraw(Canvas canvas){
        if(mCommand==COMMAND_INIT){
            canvas.drawColor(Color.WHITE);
            List<GNode> nodes = mGraph.getmNodes();
            for (GNode node : nodes) {
                drawNode(canvas, node);
                drawText(canvas,node, (char) ('0'+node.mIndex));
            }
        }
        else {
            canvas.drawBitmap(mBackBitmap,0,0,null);
        }
    }

    /**
     * 绘制双缓冲使用的图片
     * @param canvas
     */
    private void checkIfChanged(Canvas canvas) {
        if (!hasChanged){
            return;
        }
        canvas.drawColor(Color.WHITE);
        List<GNode> nodes=mGraph.getmNodes();
        for(GNode node:nodes){
            if(node.state==GNode.DELETED_STATE){
                drawNode(canvas,node);
                continue;
            }
            for(GEdge edge=node.mNext;edge!=null;edge=edge.next){
                GNode tn=nodes.get(edge.mIndex);
                if(tn.state!=GNode.DELETED_STATE){
                    drawEdge(null,mBackCanvas,node,tn,true);
                }
            }
        }
    }

    /**
     * 绘制边
     * @param holder
     * @param pCanvas
     * @param start
     * @param end
     * @param instant
     */
    private void drawEdge(SurfaceHolder holder, Canvas pCanvas, GNode start, GNode end, boolean instant){
        if(start.state==GNode.DELETED_STATE||end.state==GNode.DELETED_STATE){
            return;
        }
        if(instant){
            pCanvas.drawLine(start.mPosx,start.mPosy,end.mPosx,end.mPosy,mNormalEdgePaint);
            return;
        }
        float distance= (float) Math.sqrt((end.mPosx-start.mPosx)*(end.mPosx-start.mPosx)+
                (end.mPosy-start.mPosy)*(end.mPosy-start.mPosy));
        float xstep=(end.mPosx-start.mPosx)/distance;
        float ystep=(end.mPosy-start.mPosy)/distance;
        for(float step=0;step<distance;step+=2){
            float cx=start.mPosx + step * xstep;
            float cy=start.mPosy + step * ystep;
            Canvas canvas=holder.lockCanvas(new Rect((int)cx-10,(int)cy-10,(int)cx+10,(int)cy+10));
            try {
                if(start.state==GNode.NORMAL_STATE) {
                    pCanvas.drawPoint(cx,cy , mNormalEdgePaint);
                }
                else if(start.state==GNode.DELETED_STATE||end.state==GNode.DELETED_STATE){
                    pCanvas.drawPoint(cx, cy, mDeletedEdgePaint);
                }
                else{
                    pCanvas.drawPoint(cx, cy, mVisitedEdgePaint);
                }
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                if(canvas!=null){
                    canvas.drawBitmap(mCurrentBitmap,0,0,null );
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }


    /**
     * 绘制节点中间的节点索引号
     * @param canvas
     * @param node
     * @param index
     */
    private void drawText(Canvas canvas,GNode node,char index){
        Paint.FontMetrics metrics=mTextPaint.getFontMetrics();
        canvas.drawText(index+"",node.mPosx,node.mPosy,mTextPaint);
    }
    /**
     * 绘制节点
     * @param canvas
     * @param gNode
     */
    private void drawNode(Canvas canvas,GNode gNode){
        if(gNode.state==GNode.NORMAL_STATE) {
            canvas.drawCircle(gNode.mPosx, gNode.mPosy, 40, mNormalNodePaint);
        }
        else if(gNode.state==GNode.VISITED_STATE){
            canvas.drawCircle(gNode.mPosx,gNode.mPosy,5,mVisitedNodePaint);
        }
        else{
            canvas.drawCircle(gNode.mPosx,gNode.mPosy,5,mNormalNodePaint);
        }
    }

    /**
     * 结束绘图线程
     */
    public void endDrawThread(){
        isSurfaceAlive=false;
    }
    public void setmGraph(Graph mGraph) {
        this.mGraph = mGraph;
    }

    public void setShouldDraw(boolean shouldDraw) {
        isShouldDraw = shouldDraw;
    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    /**
     * 设置绘图的命令类型
     * @param mCommand
     */
    public void setmCommand(byte mCommand) {
        this.mCommand = mCommand;
    }

    public void setmStartNode(int index) {
        this.mStartNode = mGraph.getmNodes().get(index);
    }

    public void setmEdgeInfo(List<EdgeInfo> mEdgeInfo) {
        this.mEdgeInfo = mEdgeInfo;
    }
}
