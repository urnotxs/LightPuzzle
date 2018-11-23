package com.xs.lightpuzzle.data.dao;

/**
 * Created by xs on 2018/11/2.
 *
 * 字体查询条件的对象
 * Builder构建类
 */

public class FontQuery {

    public static class Builder {

        private Boolean isDownloaded;
        private Integer type;
        private Boolean isOrderTypeAsc;

        public Builder() {
            // no-op by default
        }
        public Builder setDownloaded(Boolean isDownloaded) {
            this.isDownloaded = isDownloaded;
            return this;
        }

        public Builder setType(Integer type) {
            this.type = type;
            return this;
        }

        public Builder setOrderTypeAsc(Boolean isOrderTypeAsc) {
            this.isOrderTypeAsc = isOrderTypeAsc;
            return this;
        }

        public FontQuery build() {
            checkFields();
            FontQuery query = new FontQuery();
            if (isDownloaded != null) {
                query.setDownloaded(isDownloaded);
            }
            if (type != null) {
                query.setType(type);
            }
            if (isOrderTypeAsc != null) {
                query.setOrderTypeAsc(isOrderTypeAsc);
            }
            return query;
        }


        private void checkFields() {
            // TODO: 1/16/18 check fields
        }
    }

    private Boolean isDownloaded;
    private Integer type;
    private Boolean isOrderTypeAsc;

    public FontQuery() {
        // no-op by default
    }

    public Boolean getDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        isDownloaded = downloaded;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean isOrderTypeAsc() {
        return isOrderTypeAsc;
    }

    public void setOrderTypeAsc(Boolean orderTypeAsc) {
        isOrderTypeAsc = orderTypeAsc;
    }

}
