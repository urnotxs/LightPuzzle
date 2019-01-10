package com.xs.lightpuzzle.data.entity;

import com.xs.lightpuzzle.data.util.IntegerBitConverter;
import com.xs.lightpuzzle.data.util.IntegerNodeMapConverter;
import com.xs.lightpuzzle.data.util.IntegerStringMapConverter;
import com.xs.lightpuzzle.data.util.SetConverter;
import com.xs.lightpuzzle.yszx.FlagHelper;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Map;
import java.util.Set;

/**
 * Created by xs on 2018/11/6.
 *
 * 模板集，包括长图模板含多个子模板
 */
@Entity
public class TemplateSet {

    /**
     * 长图子模板链接
     */
    public static class Node {
        private final int category;
        private final String id;

        public Node(int category, String id) {
            this.category = category;
            this.id = id;
        }

        public int getCategory() {
            return category;
        }

        public String getId() {
            return id;
        }
    }

    public interface FLAG {
        // 0...001
        int DOWNLOADED = 1;
        // 0...010
        int UNUSED = 1 << 1;
        // 0...100
        int HISTORY = 1 << 2;
        // 0..1000
        int LIKE = 1 << 3;
    }

    @Id
    private Long localId;

    @NotNull
    @Index
    private int category; // 分类

    @NotNull
    private String id; // 模板id

    private String name; // 名字
    private int proportion;
    /**
     * 下载URL
     */
    private String url; // 模板下载路径，zip包

    /**
     * 缩略图URL
     */
    private String thumbUrl; // 默认封面图下载路径
    private String thumbFileName; // 默认封面图本地文件名

    /**
     * 最少图片数
     */
    private int minPhotoNum; // 模板的最小图片数
    /**
     * 最多图片数
     */
    private int maxPhotoNum; // 模板的最大图片数

    @Transient
    private Map<Integer, Template> templateMap; // 图片张数对应的模板Map列表

    /**
     * 对应图片张数的模板缩略图，Key:图片张数，value：缩略图文件名
     */
    @Convert(converter = IntegerStringMapConverter.class, columnType = String.class)
    private Map<Integer, String> thumbFileNameMap; // 图片张数对应的封面图本地文件名

    @Convert(converter = SetConverter._String.class, columnType = String.class)
    private Set<String> attachedFontIdSet; // 模板自带文字字体id

    @Convert(converter = IntegerNodeMapConverter.class, columnType = String.class)
    private Map<Integer, Node> attachedNodeMap; // 长图子模板

    private long order; // 顺序
    private float uiRatio;  // 宽高比

    // --- Local

    private String dirPath; // 本地存放路径

    private long downloadedOrder; // 下载列表顺序
    private long historyOrder; // 历史列表的顺序
    private long likeOrder; // 喜欢列表的顺序

    @Convert(converter = IntegerBitConverter.class, columnType = String.class)
    private int flag;

    public TemplateSet() {
        // no-op by default
    }

    @Generated(hash = 269154088)
    public TemplateSet(Long localId, int category, @NotNull String id, String name, int proportion, String url,
                       String thumbUrl, String thumbFileName, int minPhotoNum, int maxPhotoNum,
                       Map<Integer, String> thumbFileNameMap, Set<String> attachedFontIdSet,
                       Map<Integer, Node> attachedNodeMap, long order, float uiRatio, String dirPath, long downloadedOrder,
                       long historyOrder, long likeOrder, int flag) {
        this.localId = localId;
        this.category = category;
        this.id = id;
        this.name = name;
        this.proportion = proportion;
        this.url = url;
        this.thumbUrl = thumbUrl;
        this.thumbFileName = thumbFileName;
        this.minPhotoNum = minPhotoNum;
        this.maxPhotoNum = maxPhotoNum;
        this.thumbFileNameMap = thumbFileNameMap;
        this.attachedFontIdSet = attachedFontIdSet;
        this.attachedNodeMap = attachedNodeMap;
        this.order = order;
        this.uiRatio = uiRatio;
        this.dirPath = dirPath;
        this.downloadedOrder = downloadedOrder;
        this.historyOrder = historyOrder;
        this.likeOrder = likeOrder;
        this.flag = flag;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
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

    public String getThumbFileName() {
        return thumbFileName;
    }

    public void setThumbFileName(String thumbFileName) {
        this.thumbFileName = thumbFileName;
    }

    public int getMinPhotoNum() {
        return minPhotoNum;
    }

    public void setMinPhotoNum(int minPhotoNum) {
        this.minPhotoNum = minPhotoNum;
    }

    public int getMaxPhotoNum() {
        return maxPhotoNum;
    }

    public void setMaxPhotoNum(int maxPhotoNum) {
        this.maxPhotoNum = maxPhotoNum;
    }

    public Map<Integer, Template> getTemplateMap() {
        return templateMap;
    }

    public void setTemplateMap(Map<Integer, Template> templateMap) {
        this.templateMap = templateMap;
    }

    public Map<Integer, String> getThumbFileNameMap() {
        return thumbFileNameMap;
    }

    public void setThumbFileNameMap(Map<Integer, String> thumbFileNameMap) {
        this.thumbFileNameMap = thumbFileNameMap;
    }

    public float getUiRatio() {
        return uiRatio;
    }

    public void setUiRatio(float uiRatio) {
        this.uiRatio = uiRatio;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public long getDownloadedOrder() {
        return downloadedOrder;
    }

    public void setDownloadedOrder(long downloadedOrder) {
        this.downloadedOrder = downloadedOrder;
    }

    public long getHistoryOrder() {
        return historyOrder;
    }

    public void setHistoryOrder(long historyOrder) {
        this.historyOrder = historyOrder;
    }

    public long getLikeOrder() {
        return likeOrder;
    }

    public void setLikeOrder(long likeOrder) {
        this.likeOrder = likeOrder;
    }

    // ---other method
    public void setDownloaded(boolean isDownloaded) {
        downloadedOrder = setFlagOrder(isDownloaded(), downloadedOrder, isDownloaded, true);
        flag = FlagHelper.toggle(FLAG.DOWNLOADED, flag);
        setUnused(isDownloaded);
    }

    private long setFlagOrder(boolean currOn, long currOrder, boolean toOn, boolean isKeepCurrOrder) {
        if (currOn) {
            if (toOn) { // keep current order
                return isKeepCurrOrder ? currOrder : System.currentTimeMillis();
            } else {
                return 0;
            }
        } else {
            return toOn ? System.currentTimeMillis() : 0;
        }
    }

    public boolean isDownloaded() {
        return FlagHelper.isOn(FLAG.DOWNLOADED, flag);
    }

    public void setUnused(boolean isUnused) {
        flag = FlagHelper.toggle(FLAG.UNUSED, flag, isUnused);
    }

    public boolean isUnused() {
        return FlagHelper.isOn(FLAG.UNUSED, flag);
    }

    public void setLike(boolean isLike) {
        likeOrder = setFlagOrder(isLike(), likeOrder, isLike, false);
        flag = FlagHelper.toggle(FLAG.LIKE, flag, isLike);
    }

    public boolean isLike() {
        return FlagHelper.isOn(FLAG.LIKE, flag);
    }

    public void setHistory(boolean isHistory) {
        historyOrder = setFlagOrder(isHistory(), historyOrder, isHistory, false);
        flag = FlagHelper.toggle(FLAG.HISTORY, flag, isHistory);
    }

    public boolean isHistory() {
        return FlagHelper.isOn(FLAG.HISTORY, flag);
    }
    // --- other method

    public Set<String> getAttachedFontIdSet() {
        return this.attachedFontIdSet;
    }

    public void setAttachedFontIdSet(Set<String> attachedFontIdSet) {
        this.attachedFontIdSet = attachedFontIdSet;
    }

    public Map<Integer, Node> getAttachedNodeMap() {
        return this.attachedNodeMap;
    }

    public void setAttachedNodeMap(Map<Integer, Node> attachedNodeMap) {
        this.attachedNodeMap = attachedNodeMap;
    }

    // --- Override
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TemplateSet that = (TemplateSet) obj;

        return category == that.category
                && (id != null ? id.equals(that.id) : that.id == null);
    }

    @Override
    public int hashCode() {
        int result = category;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
    // --- Override
}
