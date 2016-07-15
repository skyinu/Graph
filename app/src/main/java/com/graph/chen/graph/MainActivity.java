package com.graph.chen.graph;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.graph.chen.graph.view.GraphSurfaceView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button createbtn,bfs_btn,dfs_btn,del_btn;
    private GraphSurfaceView mGraphSurfaceView;
    private int mNodeNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
    }
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
    }
    private void setDialogListener(AlertDialog.Builder dialog){
        dialog.setTitle("请输入要生成无向图的节点个数（2～10）").setIcon(android.R.drawable.ic_dialog_info);
        final EditText editText=new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog.setView(editText);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str=editText.getText().toString();
                if(str.equals("")||Integer.valueOf(str)>10||Integer.valueOf(str)<2){
                    Toast.makeText(MainActivity.this,"节点数目范围为2～10", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        dialog.setNegativeButton("取消",null);
        dialog.show();
    }
    private void readyForVisit(AlertDialog.Builder dialog) {
        dialog.setTitle("请输入搜索起点（0～"+mNodeNumber+"）").setIcon(android.R.drawable.ic_dialog_info);
        final EditText editText=new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog.setView(editText);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                if (str.equals("") || Integer.valueOf(str) > mNodeNumber || Integer.valueOf(str) < 0) {
                    Toast.makeText(MainActivity.this, "范围为0～" + mNodeNumber, Toast.LENGTH_SHORT).show();
                    return;
                }
                int start = Integer.valueOf(str);
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_btn:
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                setDialogListener(dialog);
                break;
            case R.id.dfs_btn:
                AlertDialog.Builder dialog1=new AlertDialog.Builder(this);
                break;
            case R.id.bfs_btn:
                AlertDialog.Builder dialog2=new AlertDialog.Builder(this);
                break;
            case R.id.del_btn:
                AlertDialog.Builder dialog3=new AlertDialog.Builder(this);
                break;
        }
    }
}
