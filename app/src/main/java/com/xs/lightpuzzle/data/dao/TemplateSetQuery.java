package com.xs.lightpuzzle.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.xs.lightpuzzle.data.util.TemplateCategoryHelper;

/**
 * Created by xs on 2018/11/6.
 *
 * 模板查询条件的对象
 * Builder构建类
 */

public class TemplateSetQuery implements Parcelable {

    public static class Builder{

        private Integer category;
        private Integer count;
        private Integer photoNum;
        private Integer flag;

        public Builder setCategory(Integer category) {
            if (category != null && !TemplateCategoryHelper.isLegal(category)){
                throw new IllegalArgumentException("Category is illegal");
            }
            this.category = category;
            return this;
        }

        public Builder setCount(Integer count) {
            this.count = count;
            return this;
        }

        public Builder setPhotoNum(Integer photoNum) {
            this.photoNum = photoNum;
            return this;
        }

        public Builder setFlag(Integer flag) {
            this.flag = flag;
            return this;
        }

        public TemplateSetQuery build(){
            TemplateSetQuery query = new TemplateSetQuery();
            if (category != null){
                query.setCategory(category);
            }
            if (count != null){
                query.setCount(count);
            }
            if (photoNum != null){
                query.setPhotoNum(photoNum);
            }
            if (flag != null){
                query.setFlag(flag);
            }
            return query;
        }
    }

    private Integer category;
    private Integer count;
    private Integer photoNum;
    private Integer flag;

    public TemplateSetQuery() {
        // no-op by default
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPhotoNum() {
        return photoNum;
    }

    public void setPhotoNum(Integer photoNum) {
        this.photoNum = photoNum;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(category);
        dest.writeValue(count);
        dest.writeValue(photoNum);
        dest.writeValue(flag);
    }

    protected TemplateSetQuery(Parcel in) {
        category = (Integer) in.readValue(Integer.class.getClassLoader());
        count = (Integer) in.readValue(Integer.class.getClassLoader());
        photoNum = (Integer) in.readValue(Integer.class.getClassLoader());
        flag = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<TemplateSetQuery> CREATOR = new Creator<TemplateSetQuery>() {
        @Override
        public TemplateSetQuery createFromParcel(Parcel in) {
            return new TemplateSetQuery(in);
        }

        @Override
        public TemplateSetQuery[] newArray(int size) {
            return new TemplateSetQuery[size];
        }
    };
}
