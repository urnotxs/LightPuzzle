package com.xs.lightpuzzle.data.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.File;

/**
 * Created by xs on 2018/11/2.
 */
@Entity
public class Font {
    @Id
    private Long localId;

    @Unique
    private String id;
    private String name; // 字体名
    private int type;
    private int size;
    private String url; // 字体jar包的下载网址
    private String thumbUrl; // 字体封面图的网址
    private String fileName; // 字体文件名
    private String thumbFileName; // 字体封面文件名
    private long order; // 顺序

    private String dirPath;
    private boolean isDownloaded; // 是否已经下载改字体

    public Font() {
        // no-op by default
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Font)) {
            return false;
        } else {
            if (obj == this) {
                return true;
            } else {
                Font another = (Font) obj;
                String _id = another.getId();
                return !TextUtils.isEmpty(_id) && _id.equals(id);
            }
        }
    }

    @Override
    public int hashCode() {
        // TODO: 1/17/18 add category value
        return id.hashCode();
    }

    @Generated(hash = 1478787200)
    public Font(Long localId, String id, String name, int type, int size,
                String url, String thumbUrl, String fileName, String thumbFileName,
                long order, String dirPath, boolean isDownloaded) {
        this.localId = localId;
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.url = url;
        this.thumbUrl = thumbUrl;
        this.fileName = fileName;
        this.thumbFileName = thumbFileName;
        this.order = order;
        this.dirPath = dirPath;
        this.isDownloaded = isDownloaded;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbFileName() {
        return thumbFileName;
    }

    public void setThumbFileName(String thumbFileName) {
        this.thumbFileName = thumbFileName;
    }

    public String getThumbFilePath() {
        return getDirPath() + File.separator + getThumbFileName();
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getFilePath() {
        return getDirPath() + File.separator + getFileName();
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public boolean getIsDownloaded() {
        return this.isDownloaded;
    }

    public void setIsDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }
}
