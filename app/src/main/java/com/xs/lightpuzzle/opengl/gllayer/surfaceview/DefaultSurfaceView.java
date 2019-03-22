package com.xs.lightpuzzle.opengl.gllayer.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xs.lightpuzzle.opengl.gllayer.renderer.DefaultRenderer;
import com.xs.lightpuzzle.opengl.gllayer.surfaceview.base.BaseSurfaceView;

/**
 * @author xs
 * @description
 * @since 2019/3/8
 */

public class DefaultSurfaceView extends BaseSurfaceView {

    public DefaultSurfaceView(Context context) {
        super(context);
        setRenderer();
    }

    @Override
    protected void setRenderer() {
        DefaultRenderer defaultRenderer = new DefaultRenderer(mContext);
        defaultRenderer.setBitmap(getBitmap());
        setRenderer(defaultRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void setEGLContextClientVersion() {
        setEGLContextClientVersion(2);
    }

    public Bitmap getBitmap() {
        String mPhotoDir = "/storage/emulated/0/DCIM/Camera/NA201707251526550026-00-000000000.jpg";
        return BitmapFactory.decodeFile(mPhotoDir);
    }
}
