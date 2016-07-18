package com.graph.chen.graph.action;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.graph.chen.graph.InputGraphActivity;
import com.graph.chen.graph.view.GraphSurfaceView;

/**
 * Created by chen on 2016/7/16.
 */
public class ActionListenerImpl implements ActionListener {
    @Override
    public void readForInputGraph(final Context context, AlertDialog.Builder dialog, String title) {
        final EditText editText=configDialog(context,dialog,title);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str=editText.getText().toString();
                if(str.equals("")||Integer.valueOf(str)>10||Integer.valueOf(str)<2){
                    Toast.makeText(context,"节点数目范围为2～10", Toast.LENGTH_SHORT).show();
                    return;
                }
                int nodeNumber=Integer.valueOf(str.trim());
                Intent intent=new Intent(context,InputGraphActivity.class);
                intent.putExtra("nodeNumber",nodeNumber);
                ((Activity)context).startActivityForResult(intent,0);
            }
        });
        dialog.show();
    }

    @Override
    public void readForBfsVisit(final GraphSurfaceView surfaceView, final Context context, AlertDialog.Builder dialog, final int number) {
        int limit=number==0?number-1:number;
        String title="请输入搜索起点（0～"+limit+"）";
        final EditText editText=configDialog(context,dialog,title);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                if (str.equals("") || number<=0
                        ||Integer.valueOf(str) > number || Integer.valueOf(str) < 0) {
                    Toast.makeText(context, "范围为0～" + number, Toast.LENGTH_SHORT).show();
                    editText.setTag(1);
                }
            }
        });
        dialog.show();
    }

    @Override
    public void readForDfsVisit(GraphSurfaceView surfaceView, final Context context, AlertDialog.Builder dialog, final int number) {
        int limit=number==0?number-1:number;
        String title="请输入搜索起点（0～"+limit+"）";
        final EditText editText=configDialog(context,dialog,title);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                if (str.equals("") || number<=0
                        ||Integer.valueOf(str) > number || Integer.valueOf(str) < 0) {
                    Toast.makeText(context, "范围为0～" + number, Toast.LENGTH_SHORT).show();
                }
                int start=Integer.valueOf(str);
            }
        });
        dialog.show();
    }

    @Override
    public void readForDelete(GraphSurfaceView surfaceView, final Context context, AlertDialog.Builder dialog, final int number) {
        int limit=number==0?number-1:number;
        String title="请输入要删除节点的索引（0～"+limit+"）";
        final EditText editText=configDialog(context,dialog,title);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                if (str.equals("") || number<=0
                        ||Integer.valueOf(str) > number || Integer.valueOf(str) < 0) {
                    Toast.makeText(context, "范围为0～" + number, Toast.LENGTH_SHORT).show();
                }
                int start=Integer.valueOf(str);
            }
        });
        dialog.show();
    }
    /**
     * 设置dialog的对话框:标题、布局
     * @param dialog
     * @param title
     * @return
     */
    private EditText configDialog(Context context,AlertDialog.Builder dialog, String title){
        dialog.setTitle(title).setIcon(android.R.drawable.ic_dialog_info);
        final EditText editText=new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog.setView(editText);
        dialog.setNegativeButton("取消", null);
        return editText;
    }
}
