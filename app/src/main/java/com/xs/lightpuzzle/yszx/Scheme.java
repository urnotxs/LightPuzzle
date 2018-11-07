package com.xs.lightpuzzle.yszx;

/**
 * Created by xs on 2018/11/7.
 */

public enum Scheme {

    HTTP("http"),
    HTTPS("https"),
    FILE("file"),
    CONTENT("content"),
    ASSETS("assets"),
    DRAWABLE("drawable"),
    UNKNOWN("");

    private String scheme;
    private String uriPrefix;

    Scheme(String scheme) {
        this.scheme = scheme;
        this.uriPrefix = scheme + "://";
    }

    public String wrap(String path) {
        return this.uriPrefix + path;
    }
}
