package com.graph.chen.graph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.graph.chen.graph.data.EdgeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2015/7/31.
 */
public class InputGraphActivity extends Activity {
    private EditText etbegin,etend;
    private TextView tvshow;
    private int mNumber =0;
    /**
     * amount node
     */
    private int nodenumber;
    private boolean isConfirm=false;
    private List<EdgeInfo> nodePairList=new ArrayList<>();
    private List<String> tvtext=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_layout);
        tvshow= (TextView) findViewById(R.id.show_text);
        etend= (EditText) findViewById(R.id.end_edit);
        etbegin= (EditText) findViewById(R.id.begin_edit);
        nodenumber=getIntent().getIntExtra("amountnode",2);
    }

    public void btn_clicked(View view){
        switch (view.getId()){
            case R.id.pre_btn:
                subEdges();
                break;
            case R.id.comfirm_btn:
                isConfirm=true;
                addEdges();
                Intent intent=new Intent();
                intent.putParcelableArrayListExtra("pair", (ArrayList<? extends Parcelable>) nodePairList);
                setResult(0, intent);
                finish();
                break;
            case R.id.goon_btn:
                addEdges();
                break;
        }
    }
    //删除弧
    private void subEdges() {
        if(mNumber ==0)
            return;
        mNumber--;
        nodePairList.remove(mNumber);
        tvtext.remove(mNumber);
        tvshow.setText("");
        if(mNumber >0) {
            for (int i = 0; i< mNumber; i++){
                tvshow.append(tvtext.get(i));
            }
        }
    }
    //添加弧
    private void addEdges() {
        String bestr=etbegin.getText().toString();
        String endstr=etend.getText().toString();
        if(bestr.equals("")||endstr.equals("")){
            if(!isConfirm)
                Toast.makeText(InputGraphActivity.this, "不允许为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int bx=Integer.valueOf(bestr);
        int ey=Integer.valueOf(endstr);
        if(bx>=nodenumber||bx<0||ey>=nodenumber||ey<0){
            Toast.makeText(InputGraphActivity.this,"节点下标的范围为0～"+(nodenumber-1),Toast.LENGTH_SHORT).show();
            return;
        }
        EdgeInfo pair=new EdgeInfo(bx,ey);
        nodePairList.add(pair);
        tvtext.add(bestr + "<-->" + endstr+"\n");
        tvshow.append(tvtext.get(mNumber));
        etbegin.setText("");
        etend.setText("");
        etbegin.requestFocus();
        mNumber++;
    }
    @Override
    public void onBackPressed() {
        isConfirm=true;
        addEdges();
        Intent intent=new Intent();
        intent.putParcelableArrayListExtra("pair", (ArrayList<? extends Parcelable>) nodePairList);
        setResult(0, intent);
        super.onBackPressed();
    }
}
