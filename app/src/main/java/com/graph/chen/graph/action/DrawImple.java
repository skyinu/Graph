package com.graph.chen.graph.action;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.graph.chen.graph.data.EdgeInfo;
import com.graph.chen.graph.data.GEdge;
import com.graph.chen.graph.data.GNode;
import com.graph.chen.graph.data.Graph;
import com.graph.chen.graph.util.BitmapSaveUtil;
import com.graph.chen.graph.view.GraphSurfaceView;

import java.util.List;

/**
 * Created by chen on 2016/7/14.
 */
public class DrawImple extends Handler implements Runnable {
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
    private Paint mDeletedNodePaint;
    private Paint mNormalEdgePaint;
    private Paint mVisitedEdgePaint;
    private Paint mDeletedEdgePaint;
    private Paint mTextPaint;
    /**
     * 双缓冲使用的bitmap
     */
    private Bitmap mBackBitmap, mCurrentBitmap;
    /**
     * 对应缓冲图片的画家
     */
    private Canvas mBackCanvas, mCurrentCanvas;

    private byte mCommand;
    public static final byte COMMAND_INIT = 0;
    public static final byte COMMAND_DELETE = 1;
    public static final byte COMMAND_BFS = 2;
    public static final byte COMMAND_DFS = 3;
    /**
     * 遍历、删除的开始点
     */
    private GNode mStartNode;

    /**
     * 开始绘图线程并初始化画笔
     *
     * @param surface
     */
    public void startdrawThread(GraphSurfaceView surface) {
        this.mSurfaceView = surface;
        isSurfaceAlive = true;
        isShouldDraw = false;
        InitPaint();
        mBackBitmap = Bitmap.createBitmap(surface.getMeasuredWidth(), surface.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mBackCanvas = new Canvas(mBackBitmap);
        mCurrentBitmap = Bitmap.createBitmap(surface.getMeasuredWidth(), surface.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCurrentCanvas = new Canvas(mCurrentBitmap);
        mDrawThread = new Thread(this);
        mDrawThread.start();
        hasChanged = true;

    }

    /**
     * 初始化各种类型的画笔
     */
    private void InitPaint() {
        mNormalNodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVisitedNodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDeletedNodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalEdgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVisitedEdgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDeletedEdgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mNormalNodePaint.setColor(GraphSurfaceView.mNormalNodeColor);
        mNormalNodePaint.setStyle(Paint.Style.STROKE);
        mNormalNodePaint.setStrokeWidth(5);


        mVisitedNodePaint.setColor(GraphSurfaceView.mVisitNodeColor);
        mVisitedNodePaint.setStyle(Paint.Style.STROKE);
        mVisitedNodePaint.setStrokeWidth(5);

        mDeletedNodePaint.setStyle(Paint.Style.STROKE);
        mDeletedNodePaint.setColor(GraphSurfaceView.mDeletedNodeColor);
        mDeletedNodePaint.setStrokeWidth(5);

        mNormalEdgePaint.setColor(GraphSurfaceView.mNormalEdgeColor);
        mNormalEdgePaint.setStrokeWidth(8);

        mVisitedEdgePaint.setColor(GraphSurfaceView.mVisitEdgeColor);
        mVisitedEdgePaint.setStrokeWidth(8);

        mDeletedEdgePaint.setColor(Color.WHITE);
        mDeletedEdgePaint.setStrokeWidth(8);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(50);
    }

    @Override
    public void run() {
        SurfaceHolder holder = mSurfaceView.getHolder();
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        holder.unlockCanvasAndPost(canvas);
        while (isSurfaceAlive) {
            while (!isShouldDraw) {
            }
            if (!isSurfaceAlive)
                return;
            Intent intent = new Intent("com.graph.chen.state");
            intent.putExtra("flag", false);
            mSurfaceView.getContext().sendBroadcast(intent);
            prepareDraw(mCurrentCanvas);

            if (mCommand == COMMAND_INIT) {
                drawInitGraphEdges(holder, mCurrentCanvas, false);
            } else {
                if (mCommand == COMMAND_DELETE) {
                    drawDeletedEdgesAndNode(holder, mStartNode);
                } else if (mCommand == COMMAND_BFS) {
                    List<EdgeInfo> path = mGraph.bfsVisit(mStartNode.mIndex);
                    drawVisitedPath(holder, path);
                } else {
                    List<EdgeInfo> path = mGraph.dfsVisit(mStartNode.mIndex);
                    drawVisitedPath(holder, path);
                }
            }
            checkIfChanged(mBackCanvas);
            intent = new Intent("com.graph.chen.state");
            intent.putExtra("flag", true);
            mSurfaceView.getContext().sendBroadcast(intent);
            BitmapSaveUtil.saveToStorage(mCurrentBitmap, null, ".jpg");
            BitmapSaveUtil.saveToStorage(mBackBitmap, null, ".png");
            isShouldDraw = false;

        }
    }

    private void drawVisitedPath(SurfaceHolder holder, List<EdgeInfo> path) {
        Log.e("tag", "path=" + path.toString());
        List<GNode> nodes = mGraph.getmNodes();
        int len = path.size();
        if (len == 0)
            return;

        for (int i = 0; i < len; i++) {
            EdgeInfo info = path.get(i);
            GNode sn = nodes.get(info.mStart);
            GNode en = nodes.get(info.mEnd);

            sn.state = GNode.VISITED_STATE;
            drawNode(mCurrentCanvas, sn);
            Canvas canvas = holder.lockCanvas();
            canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
            holder.unlockCanvasAndPost(canvas);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sn.state = GNode.NORMAL_STATE;

            en.state = GNode.VISITED_STATE;
            if (sn.mIndex != en.mIndex) {
                drawEdge(holder, mCurrentCanvas, sn, en, false);
                drawNode(mCurrentCanvas, en);
            }
            en.state = GNode.NORMAL_STATE;

            canvas = holder.lockCanvas();
            canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
            holder.unlockCanvasAndPost(canvas);
        }


    }

    /**
     * 删除一个节点时的处理函数
     *
     * @param holder
     * @param delNode 为待删除的节点
     */
    private void drawDeletedEdgesAndNode(SurfaceHolder holder, GNode delNode) {
        List<GNode> nodes = mGraph.getmNodes();
        delNode.state = GNode.DELETED_STATE;
        for (GEdge edge = delNode.mNext; edge != null; edge = edge.next) {
            GNode tn = nodes.get(edge.mIndex);
            if (tn.state != GNode.DELETED_STATE) {
                drawEdge(holder, mCurrentCanvas, tn, delNode, false);
                drawNode(mCurrentCanvas, tn);
            }
        }

        Canvas canvas = holder.lockCanvas();
        drawNode(mCurrentCanvas, delNode);
        if (canvas != null) {
            canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 用于创建时绘制无向图的边
     *
     * @param holder
     * @param mCurrentCanvas
     * @param instance
     */
    private void drawInitGraphEdges(SurfaceHolder holder, Canvas mCurrentCanvas, boolean instance) {
        List<GNode> nodes = mGraph.getmNodes();
        for (EdgeInfo info : mEdgeInfo) {
            drawEdge(holder, mCurrentCanvas, nodes.get(info.mStart), nodes.get(info.mEnd), instance);
        }
    }

    /**
     * 准备静态的背景,绘制于图片之上
     *
     * @param canvas
     */
    private void prepareDraw(Canvas canvas) {
        if (mCommand == COMMAND_INIT) {
            canvas.drawColor(Color.WHITE);
            List<GNode> nodes = mGraph.getmNodes();
            for (GNode node : nodes) {
                drawNode(canvas, node);
            }
        } else {
            canvas.drawBitmap(mBackBitmap, 0, 0, null);
        }
    }

    /**
     * 绘制双缓冲使用的图片
     *
     * @param canvas
     */
    private void checkIfChanged(Canvas canvas) {
        if (!hasChanged) {
            return;
        }
        canvas.drawColor(Color.WHITE);
        List<GNode> nodes = mGraph.getmNodes();
        for (GNode node : nodes) {
            drawNode(canvas, node);
            if (node.state == GNode.DELETED_STATE) {
                continue;
            }
            for (GEdge edge = node.mNext; edge != null; edge = edge.next) {
                GNode tn = nodes.get(edge.mIndex);
                if (tn.state != GNode.DELETED_STATE) {
                    drawEdge(null, mBackCanvas, node, tn, true);
                }
            }
        }
    }


    /**
     * 绘制边
     *
     * @param holder
     * @param pCanvas
     * @param start
     * @param end
     * @param instant
     */
    private void drawEdge(SurfaceHolder holder, Canvas pCanvas, GNode start, GNode end, boolean instant) {
        if ((start.state == GNode.DELETED_STATE || end.state == GNode.DELETED_STATE) && mCommand != COMMAND_DELETE) {
            return;
        }
        if (instant) {
            pCanvas.drawLine(start.mPosx, start.mPosy, end.mPosx, end.mPosy, mNormalEdgePaint);
            return;
        }
        float distance = (float) Math.sqrt((end.mPosx - start.mPosx) * (end.mPosx - start.mPosx) +
                (end.mPosy - start.mPosy) * (end.mPosy - start.mPosy));
        float xstep = (end.mPosx - start.mPosx) / distance;
        float ystep = (end.mPosy - start.mPosy) / distance;
        int time = (int) Math.max(200 / distance, 5) / 2;
        for (float step = 0; step < distance; step += 2) {
            float cx = start.mPosx + step * xstep;
            float cy = start.mPosy + step * ystep;
            Canvas canvas = holder.lockCanvas();
            try {
                Thread.sleep(time);
                if (start.state == GNode.NORMAL_STATE && end.state == GNode.NORMAL_STATE) {
                    pCanvas.drawPoint(cx, cy, mNormalEdgePaint);
                } else if (start.state == GNode.DELETED_STATE || end.state == GNode.DELETED_STATE) {
                    pCanvas.drawPoint(cx, cy, mDeletedEdgePaint);
                } else {
                    pCanvas.drawPoint(cx, cy, mVisitedEdgePaint);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    canvas.drawBitmap(mCurrentBitmap, 0, 0, null);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }


    /**
     * 绘制节点中间的节点索引号
     *
     * @param canvas
     * @param node
     * @param index
     */
    private void drawText(Canvas canvas, GNode node, char index) {
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        int height = (int) (metrics.descent);
        int width = (int) mTextPaint.measureText(index + "");
        canvas.drawText(index + "", node.mPosx - width / 2, node.mPosy + height, mTextPaint);
    }

    /**
     * 绘制节点
     *
     * @param canvas
     * @param gNode
     */
    private void drawNode(Canvas canvas, GNode gNode) {
        if (gNode.state == GNode.NORMAL_STATE) {
            canvas.drawCircle(gNode.mPosx, gNode.mPosy, GNode.radius, mNormalNodePaint);
        } else if (gNode.state == GNode.VISITED_STATE) {
            canvas.drawCircle(gNode.mPosx, gNode.mPosy, GNode.radius, mVisitedNodePaint);
        } else {
            canvas.drawCircle(gNode.mPosx, gNode.mPosy, GNode.radius, mDeletedNodePaint);
        }
        drawText(canvas, gNode, (char) ('0' + gNode.mIndex));
    }

    /**
     * 结束绘图线程
     */
    public void endDrawThread() {
        isSurfaceAlive = false;
        isShouldDraw = true;
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
     *
     * @param mCommand
     */
    public void setmCommand(byte mCommand) {
        this.mCommand = mCommand;
    }

    /**
     * 设置遍历、删除的起点
     *
     * @param index
     */
    public void setmStartNode(int index) {
        this.mStartNode = mGraph.getmNodes().get(index);
        if (mStartNode.state == GNode.DELETED_STATE) {
            Toast.makeText(mSurfaceView.getContext(), "节点已删除", Toast.LENGTH_SHORT).show();
            return;
        }
        setShouldDraw(true);
    }

    public void setmEdgeInfo(List<EdgeInfo> mEdgeInfo) {
        this.mEdgeInfo = mEdgeInfo;
    }

}
