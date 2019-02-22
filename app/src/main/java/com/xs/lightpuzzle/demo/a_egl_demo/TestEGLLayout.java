package com.xs.lightpuzzle.demo.a_egl_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xs.lightpuzzle.R;

import static android.os.Looper.getMainLooper;

/**
 * @author xs
 * @description 创建 EGL 环境，无需 GLSurfaceView 便可运用 OpenGL进行图层绘制
 * @since 2019/1/24
 */

public class TestEGLLayout extends FrameLayout implements View.OnClickListener {

    private String mPhotoDir = "/storage/emulated/0/DCIM/Camera/NA201707251526550026-00-000000000.jpg";

    private Context mContext;
    private ImageView mPhotoView;
    private GLES20BackEvn mBackEnv;
    private int mBmpWidth,mBmpHeight;
    private Bitmap mBitmap;

    public TestEGLLayout(@NonNull Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(linearLayout, flParams);
        {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            Button playMusicBtn = addButton(context, "选择图片", R.id.demo_egl_choose_photo);
            linearLayout.addView(playMusicBtn, lParams);

            lParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPhotoView = new ImageView(context);
            mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mPhotoView.setVisibility(GONE);
            linearLayout.addView(mPhotoView, lParams);

        }
    }

    private Button addButton(Context context, String text, int viewId) {
        Button button = new Button(context);
        button.setBackgroundColor(0xFFBFF323);
        button.setTextColor(0xFFFFFFFF);
        button.setText(text);
        button.setId(viewId);
        button.setOnClickListener(this);
        return button;
    }

    @Override
    public void onClick(View v) {
        // 读取路径图片
        mBitmap = BitmapFactory.decodeFile(mPhotoDir);
        mBmpWidth = mBitmap.getWidth();
        mBmpHeight = mBitmap.getHeight();
        generateBitmapByEGL();
    }

    private void generateBitmapByEGL() {
        mBackEnv = new GLES20BackEvn(mBmpWidth, mBmpHeight);
        mBackEnv.setThreadOwner(getMainLooper().getThread().getName());
        mBackEnv.setBitmap(mBitmap);
        mBackEnv.initFilter(mContext);
        Bitmap bitmap = mBackEnv.getBitmap();
        if (bitmap != null){
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    bitmap.getWidth(), bitmap.getHeight());
            mPhotoView.setLayoutParams(lParams);
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setVisibility(VISIBLE);
        }
    }
}
