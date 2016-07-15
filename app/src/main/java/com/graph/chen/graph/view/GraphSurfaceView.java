package com.graph.chen.graph.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.graph.chen.graph.R;
import com.graph.chen.graph.action.DrawImple;

/**
 * Created by chen on 2016/7/12.
 */
public class GraphSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private static int mVisitNodeColor;
    private static int mNormalNodeColor;
    private static int mVisitEdgeColor;
    private static int mNormalEdgeColor;
    private static int mDeletedNodeColor;
    private DrawImple mDrawImple;

    private Point mLastClick;

    public GraphSurfaceView(Context context) {
        this(context,null);
    }

    public GraphSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GraphSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.GraphSurfaceView);
        int len=ta.length();
        for(int i=0;i<len;i++){
            int id=ta.getResourceId(i,0);
            switch (id){
                case R.styleable.GraphSurfaceView_normal_node_color:
                    mNormalNodeColor=ta.getColor(id,Color.RED);
                    break;
                case R.styleable.GraphSurfaceView_normal_edge_color:
                    mNormalEdgeColor=ta.getColor(id,Color.BLUE);
                    break;
                case R.styleable.GraphSurfaceView_visited_node_color:
                    mVisitNodeColor=ta.getColor(id,Color.GREEN);
                    break;
                case R.styleable.GraphSurfaceView_visited_edge_color:
                    mVisitEdgeColor=ta.getColor(id,Color.YELLOW);
                    break;
                case R.styleable.GraphSurfaceView_deleted_node_color:
                    mDeletedNodeColor=ta.getColor(id,Color.BLACK);
                    break;
            }
        }
        ta.recycle();
        getHolder().addCallback(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=getResources().getDisplayMetrics().widthPixels;
        int widthSpec=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec=MeasureSpec.getMode(heightMeasureSpec);
        if(widthSpec==MeasureSpec.EXACTLY&&heightSpec==MeasureSpec.EXACTLY){
            width=Math.min(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
            setMeasuredDimension(width,width);
        }
        else{
            setMeasuredDimension(width,width);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mDrawImple.endDrawThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
        }
        return super.onTouchEvent(event);
    }

    /**
     * 初始化无向图
     * @param number
     */
    public void InitGraph(int number){
    }
    public static int getmVisitNodeColor() {
        return mVisitNodeColor;
    }

    public static int getmNormalNodeColor() {
        return mNormalNodeColor;
    }

    public static int getmVisitEdgeColor() {
        return mVisitEdgeColor;
    }

    public static int getmNormalEdgeColor() {
        return mNormalEdgeColor;
    }

    public static int getmDeletedNodeColor() {
        return mDeletedNodeColor;
    }
}
