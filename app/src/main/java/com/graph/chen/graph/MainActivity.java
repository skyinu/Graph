package com.graph.chen.graph;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.graph.chen.graph.action.ActionListener;
import com.graph.chen.graph.action.ActionListenerImpl;
import com.graph.chen.graph.data.EdgeInfo;
import com.graph.chen.graph.view.GraphSurfaceView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ActionListener{
    private Button createbtn,bfs_btn,dfs_btn,del_btn;
    private GraphSurfaceView mGraphSurfaceView;
    /**
     * 记录节点总数
     */
    private int mNodeNumber;
    /**
     * 事件处理
     */
    private ActionListenerImpl mActionListener;
    private DrawStateBroadCast mBroadCast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        mActionListener =new ActionListenerImpl();
    }

    /**
     * 初始化组件
     */
    private void InitView() {
        createbtn= (Button) findViewById(R.id.create_btn);
        bfs_btn= (Button) findViewById(R.id.bfs_btn);
        dfs_btn= (Button) findViewById(R.id.dfs_btn);
        del_btn=(Button)findViewById(R.id.del_btn);
        createbtn.setOnClickListener(this);
        bfs_btn.setOnClickListener(this);
        dfs_btn.setOnClickListener(this);
        mGraphSurfaceView= (GraphSurfaceView) findViewById(R.id.surface_view);
        del_btn.setOnClickListener(this);

        mBroadCast=new DrawStateBroadCast();

        registerReceiver(mBroadCast,new IntentFilter("com.graph.chen.state"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadCast);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==RESULT_OK){
            final List<EdgeInfo> datas=data.getParcelableArrayListExtra("pair");
            mNodeNumber=data.getIntExtra("nodeNumber",2);
            mGraphSurfaceView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGraphSurfaceView.InitGraph(mNodeNumber,datas);
                }
            },200);
        }
    }

    @Override
    public void onClick(View v) {
        String title;
        AlertDialog.Builder dialog;
        switch (v.getId()){
            case R.id.create_btn:
                dialog=new AlertDialog.Builder(this);
                title="请输入要生成无向图的节点个数（2～10）";
                readForInputGraph(MainActivity.this,dialog,title);
                break;
            case R.id.dfs_btn:
                dialog=new AlertDialog.Builder(this);
                readForDfsVisit(mGraphSurfaceView,MainActivity.this,dialog,mNodeNumber);
                break;
            case R.id.bfs_btn:
                dialog=new AlertDialog.Builder(this);
                readForBfsVisit(mGraphSurfaceView,MainActivity.this,dialog,mNodeNumber);

                break;
            case R.id.del_btn:
                dialog=new AlertDialog.Builder(this);
                readForDelete(mGraphSurfaceView,MainActivity.this,dialog,mNodeNumber);
                break;
        }
    }

    @Override
    public void readForInputGraph(Context context, AlertDialog.Builder dialog, String title) {
        mActionListener.readForInputGraph(context,dialog,title);
    }

    @Override
    public void readForBfsVisit(GraphSurfaceView surfaceView, Context context, AlertDialog.Builder dialog, int number) {
        mActionListener.readForBfsVisit(surfaceView,context,dialog,number);

    }

    @Override
    public void readForDfsVisit(GraphSurfaceView surfaceView,Context context, AlertDialog.Builder dialog, int number) {
        mActionListener.readForDfsVisit(surfaceView,context,dialog,number);
    }

    @Override
    public void readForDelete(GraphSurfaceView surfaceView,Context context, AlertDialog.Builder dialog, int number) {
        mActionListener.readForDelete(surfaceView,context,dialog,number);
    }

    /**
     * 设置按钮状态
     * @param enabled
     */
    private void changeAllState(boolean enabled){
        bfs_btn.setEnabled(enabled);
        del_btn.setEnabled(enabled);
        dfs_btn.setEnabled(enabled);
        createbtn.setEnabled(enabled);
    }

    /**
     * 绘图状态广播的接收
     */
    public class DrawStateBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean flag=intent.getBooleanExtra("flag",true);
            changeAllState(flag);
        }

    }

}
