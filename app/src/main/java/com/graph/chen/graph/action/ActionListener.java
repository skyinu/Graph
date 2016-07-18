package com.graph.chen.graph.action;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.graph.chen.graph.view.GraphSurfaceView;

/**
 * Created by chen on 2016/7/16.
 */
public interface ActionListener {
    void readForInputGraph(Context context, AlertDialog.Builder dialog, String title);
    void readForBfsVisit(GraphSurfaceView surfaceView, Context context, AlertDialog.Builder dialog, int number);
    void readForDfsVisit(GraphSurfaceView surfaceView,Context context, AlertDialog.Builder dialog,int number);
    void readForDelete(GraphSurfaceView surfaceView,Context context, AlertDialog.Builder dialog,int number);
}
