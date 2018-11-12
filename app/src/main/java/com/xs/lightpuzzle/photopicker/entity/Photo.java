package com.xs.lightpuzzle.photopicker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xs on 2018/11/12.
 */

public class Photo implements Parcelable {

    private int id;
    private String path;

    public Photo() {
        // no-op by default
    }

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        path = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {

        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Photo)) {
            return false;
        }

        Photo photo = (Photo) obj;

        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
