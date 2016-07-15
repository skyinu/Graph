package com.graph.chen.graph.action;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceHolder;

import com.graph.chen.graph.data.GNode;
import com.graph.chen.graph.data.Graph;
import com.graph.chen.graph.view.GraphSurfaceView;

/**
 * Created by chen on 2016/7/14.
 */
public class DrawImple extends Handler implements Runnable{
    private Thread mDrawThread;
    private boolean hasChanged;
    private Graph mGraph;
    private GraphSurfaceView mSurfaceView;
    private Paint mNormalPaint;
    private Paint mUnusalPaint;
    private Handler mHandler;
    private Bitmap mBackBitmap;
    private Canvas mCanvas;
    private DrawHandler mDrawHandler;
    public static final byte NORMAL_DRAW=0;
    public static final byte VISIT_DRAW=1;
    public static final byte DELETE_DRAW=2;
    public void startdrawThread(GraphSurfaceView surface,Graph graph){
        this.mGraph=graph;
        this.mSurfaceView=surface;
        mNormalPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnusalPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackBitmap=Bitmap.createBitmap(surface.getMeasuredWidth(),surface.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas(mBackBitmap);
        mDrawThread=new Thread(this);
        mDrawThread.start();
        hasChanged=true;
        HandlerThread handlerThread=new HandlerThread("draw");
        handlerThread.start();
        mDrawHandler=new DrawHandler(handlerThread.getLooper());
    }

    public void endDrawThread(){


    }
    @Override
    public void run() {
    }

    class DrawHandler extends Handler{
        public DrawHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NORMAL_DRAW:
                    break;
                case VISIT_DRAW:
                    break;
                case DELETE_DRAW:
                    break;
            }
        }
    }
    private void checkIfChanged(Canvas canvas) {
        if (!hasChanged){
            return;
        }
        for(GNode gnode:mGraph.getmNodes()){
            switch (gnode.state){
                case GNode.NORMAL_STATE:
                    mNormalPaint.setColor(mSurfaceView.getmNormalNodeColor());
                    canvas.drawCircle( gnode.mPosx,gnode.mPosy,GNode.radius,mNormalPaint);
                    break;
                case GNode.VISITED_STATE:
                    mNormalPaint.setColor(mSurfaceView.getmVisitNodeColor());
                    canvas.drawCircle( gnode.mPosx,gnode.mPosy,GNode.radius,mNormalPaint);
                    break;
                case GNode.DELETED_STATE:
                    mNormalPaint.setColor(mSurfaceView.getmDeletedNodeColor());
                    canvas.drawCircle( gnode.mPosx,gnode.mPosy,GNode.radius,mNormalPaint);
                    break;
            }
        }
    }
}
