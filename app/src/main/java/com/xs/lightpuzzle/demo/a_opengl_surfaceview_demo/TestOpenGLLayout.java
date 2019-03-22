package com.xs.lightpuzzle.demo.a_opengl_surfaceview_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.xs.lightpuzzle.opengl.gllayer.surfaceview.DefaultSurfaceView;
import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class TestOpenGLLayout extends FrameLayout {
    private String mPhotoDir = "/storage/emulated/0/DCIM/Camera/NA201707251526550026-00-000000000.jpg";

    public TestOpenGLLayout(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {

        DefaultSurfaceView surfaceView = new DefaultSurfaceView(context);
        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(
                Utils.getScreenW(),
                Utils.getScreenW());
        this.addView(surfaceView, flParams);
    }

}
