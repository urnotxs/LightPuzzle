package com.xs.lightpuzzle.demo.a_egl_demo;

/**
 * Created by xs on 2019/1/24.
 */

public enum GlError {

    OK(0,"ok"),
    ConfigErr(101,"config not support");
    int code;
    String msg;
    GlError(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    public int value(){
        return code;
    }

    @Override
    public String toString() {
        return msg;
    }
}
