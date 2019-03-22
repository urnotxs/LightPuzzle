package com.xs.lightpuzzle.opengl.gllayer.surfaceview.base;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * @author xs
 * @description 构造一个抽象函数，继承GLSurfaceView，并增加一些必须配置的参数函数，方便拓展
 * @since 2019/3/8
 */

public abstract class BaseSurfaceView extends GLSurfaceView{

    protected Context mContext;

    public BaseSurfaceView(Context context) {
        super(context);
        mContext = context;
        setEGLContextClientVersion();
    }

    //设置renderer
    protected abstract void setRenderer();
    //设置OpenGL版本号
    protected abstract void setEGLContextClientVersion();
}
