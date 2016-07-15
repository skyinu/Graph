package com.graph.chen.graph.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chen on 2016/7/12.
 * the data structure used to create graph
 */
public class EdgeInfo implements Parcelable{
    public int mStart;
    public int mEnd;

    public EdgeInfo(int mStart, int mEnd) {
        this.mStart = mStart;
        this.mEnd = mEnd;
    }

    protected EdgeInfo(Parcel in) {
        mStart = in.readInt();
        mEnd = in.readInt();
    }

    public static final Creator<EdgeInfo> CREATOR = new Creator<EdgeInfo>() {
        @Override
        public EdgeInfo createFromParcel(Parcel in) {
            return new EdgeInfo(in);
        }

        @Override
        public EdgeInfo[] newArray(int size) {
            return new EdgeInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mStart);
        dest.writeInt(mEnd);
    }
}
