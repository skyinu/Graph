package com.graph.chen.graph.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by chen on 2016/7/18.
 */
public class BitmapSaveUtil {
    /**
     * 用于保存图片
     * @param bitmap
     * @param path
     * @param format
     */
    public static void saveToStorage(Bitmap bitmap,String path,String format){
        String p=Environment.getExternalStorageDirectory().getAbsolutePath();
        p=p+ File.separator+System.currentTimeMillis()+format;
        if(path==null){
            path=p;
        }
        File file=new File(path);
        try {
            file.createNewFile();
            FileOutputStream out=new FileOutputStream(file);
            if(format.equals(".jpg")){
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            }
            else if(format.equals(".png")){
                bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
