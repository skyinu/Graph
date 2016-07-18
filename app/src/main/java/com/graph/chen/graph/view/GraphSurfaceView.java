package com.graph.chen.graph.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.graph.chen.graph.R;
import com.graph.chen.graph.action.DrawImple;
import com.graph.chen.graph.data.EdgeInfo;
import com.graph.chen.graph.data.Graph;

import java.util.List;

/**
 * Created by chen on 2016/7/12.
 */
public class GraphSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    public static int mVisitNodeColor;
    public static int mNormalNodeColor;
    public static int mVisitEdgeColor;
    public static int mNormalEdgeColor;
    public static int mDeletedNodeColor;
    /**
     * 完成绘制动作的类
     */
    private DrawImple mDrawImple;


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
            int id=ta.getIndex(i);
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
        setKeepScreenOn(true);
        setFocusable(true);
        getHolder().addCallback(this);
        mDrawImple=new DrawImple();
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
        Log.e("tag","created");
        mDrawImple.startdrawThread(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("tag","destroyed");
        mDrawImple.endDrawThread();
    }

    /**
     * 初始化无向图
     * @param number
     */
    public void InitGraph(int number, List<EdgeInfo> infos){
        Graph graph=new Graph(infos,number);
        graph.calculateCoordiate(getContext(),getMeasuredWidth());
        mDrawImple.setmGraph(graph);
        mDrawImple.setmEdgeInfo(infos);
        mDrawImple.setmCommand(DrawImple.COMMAND_INIT);

        mDrawImple.setShouldDraw(true);
        Log.e("tag","dimension="+getMeasuredWidth()+"   "+getMeasuredHeight());

    }

    /**
     * 开始绘制BFS遍历的路径
     * @param start
     */
    public void BfsGraph(int start){
        mDrawImple.setmCommand(DrawImple.COMMAND_BFS);
        mDrawImple.setmStartNode(start);
    }
    /**
     * 开始绘制DFS遍历的路径
     * @param start
     */
    public void DfsGraph(int start){
        mDrawImple.setmCommand(DrawImple.COMMAND_DFS);
        mDrawImple.setmStartNode(start);
    }

    /**
     * 开始绘制删除节点的过程
     * @param delIndex
     */
    public void DeleteGraphNode(int delIndex){
        mDrawImple.setmCommand(DrawImple.COMMAND_DELETE);
        mDrawImple.setmStartNode(delIndex);
        mDrawImple.setHasChanged(true);
    }
}
