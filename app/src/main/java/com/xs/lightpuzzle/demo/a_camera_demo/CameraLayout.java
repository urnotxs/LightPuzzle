package com.xs.lightpuzzle.demo.a_camera_demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xs.lightpuzzle.constant.DirConstant;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xs on 2019/1/23.
 */

public class CameraLayout extends FrameLayout implements
        SurfaceHolder.Callback, Camera.PictureCallback, View.OnClickListener{

    private Context mContext;
    private Camera camera;
    private Camera.Parameters parameters;
    private SurfaceView mSurfaceView;
    private SurfaceHolder surfaceHolder;
    private LinearLayout mSurfaceContainer;
    private Button mShutterBtn; // 快门按钮

    private int orientationDegrees = 90;
    private String savePath;

    public CameraLayout(@NonNull Context context) {
        super(context);
        mContext = context;
        initView(mContext);
    }

    private void initView(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(linearLayout, flParams);
        {
            mSurfaceContainer = new LinearLayout(context);
            mSurfaceContainer.setBackgroundColor(Color.BLACK);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0, 1);
            linearLayout.addView(mSurfaceContainer, llParams);

            LinearLayout operationLayout = new LinearLayout(context);
            llParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.getRealPixel3(200));
            operationLayout.setBackgroundColor(Color.YELLOW);
            linearLayout.addView(operationLayout, llParams);
            {
                mShutterBtn = new Button(context);
                mShutterBtn.setText("拍照");
                mShutterBtn.setTextColor(Color.RED);
                mShutterBtn.setOnClickListener(this);
                llParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                llParams.gravity = Gravity.CENTER_HORIZONTAL;
                mShutterBtn.setPadding(20, 20, 20, 20);
                operationLayout.addView(mShutterBtn, llParams);
            }
        }
    }

    public void initSurface() {
        mSurfaceView = new SurfaceView(mContext);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mSurfaceContainer.addView(mSurfaceView, llParams);
        mSurfaceView.setOnClickListener(this);

        // 到SurfaceHolder,SurfaceHolder相当于一个监听器,可以通过CallBack来监听 SurfaceView上的变化。
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
        // 为了实现照片预览功能，需要将SurfaceHolder的类型设置为PUSH,这样画图缓存就由Camera类来管理，画图缓存是独立于Surface的
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        startOrientationChangeListener();
    }

    private final void startOrientationChangeListener() {

        OrientationEventListener mOrEventListener = new OrientationEventListener(mContext) {
            @Override
            public void onOrientationChanged(int rotation) {

                if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)) {

                    orientationDegrees = 90;
                } else if ((rotation > 45) && (rotation < 135)) {

                    orientationDegrees = 180;
                } else if ((rotation >= 135) && (rotation <= 225)) {

                    orientationDegrees = 270;
                } else if ((rotation > 225) && (rotation < 315)) {

                    orientationDegrees = 0;
                }
                // Log.e(TAG, "rotation："+rotation);
            }
        };
        mOrEventListener.enable();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("CameraLayout", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("CameraLayout", "surfaceDestroyed");

        // 当Surface被销毁的时候，该方法被调用
        // 在这里需要释放Camera资源
        releaseCamera();
    }

    private void initCamera() {
        // 判断是否有摄像头
        // 获取摄像头的个数
        // int numberOfCameras = Camera.getNumberOfCameras();
        // 后置摄像头
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        /**
         * 获取设备Camera特性参数。由于不同的设备Camera的特性是不同的，所以在设置时需要首先判断设备对应的特性，再加以设置
         */
        parameters = camera.getParameters();
        // 获取可预览的尺寸集合
        List<Size> supportedPreviewSizes = parameters
                .getSupportedPreviewSizes();
        Size previewSize = PreviewSizeUtil
                .getSupportSize(supportedPreviewSizes);
        // 获取可设置图片的大小集合
        List<Size> supportedPictureSizes = parameters
                .getSupportedPictureSizes();
        // 设置生成最大的图片
        // Size pictureSize =
        // PreviewSizeUtil.getSupportSize(supportedPictureSizes);
        Size pictureSize = supportedPictureSizes.get((supportedPictureSizes
                .size() - 1) / 2);
        // 获取可设置的帧数
        List<Integer> supportedPreviewFrameRates = parameters
                .getSupportedPreviewFrameRates();
        Integer frameRates = supportedPreviewFrameRates
                .get((supportedPreviewFrameRates.size() - 1) / 2);
        // 设置Camera的参数
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        // 设置帧数(每秒从摄像头里面获得几个画面)
        parameters.setPreviewFrameRate(frameRates);

        // 设置图片格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        // 设置照片质量
        parameters.setJpegQuality(100);

        // 首先获取系统设备支持的所有颜色特效，如果设备不支持颜色特性将返回一个null， 如果有符合我们的则设置
        List<String> colorEffects = parameters.getSupportedColorEffects();
        Iterator<String> colorItor = colorEffects.iterator();
        while (colorItor.hasNext()) {
            String currColor = colorItor.next();
            if (currColor.equals(Camera.Parameters.EFFECT_SOLARIZE)) {
                // parameters.setColorEffect(Camera.Parameters.EFFECT_AQUA);
                break;
            }
        }

        // 获取对焦模式
        List<String> focusModes = parameters.getSupportedFocusModes();
        // [auto, infinity, macro, continuous-video, continuous-picture, manual]
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // 设置自动对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        // 设置闪光灯自动开启
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
            // 自动闪光
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        }

        int orientationDegrees = PreviewSizeUtil.getCameraDisplayOrientation(
                (Activity) mContext, Camera.CameraInfo.CAMERA_FACING_BACK);
        // 设置相机镜头旋转角度(默认摄像头是横拍)
        camera.setDisplayOrientation(orientationDegrees);

        try {
            // 设置显示
            camera.setPreviewDisplay(surfaceHolder);

        } catch (IOException exception) {

            releaseCamera();
        }
        // 设置完成需要再次调用setParameter方法才能生效
        camera.setParameters(parameters);

        // 开始预览
        camera.startPreview();
    }

    private void releaseCamera() {
        if (camera != null) {

            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        savePath = Utils.getDefaultSavePath() + File.separator +
                DirConstant.PUZZLE_SAVE_START_NAME + System.currentTimeMillis() + ".jpg";

        File file = new File(savePath);
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Utils.fileScan(mContext, savePath);
        // 拍照后预览会停止，重新开启下
        camera.startPreview();
    }

    @Override
    public void onClick(View v) {
        if (v == mShutterBtn){
            if (parameters != null){
                // 设置照片在相册的旋转角度(默认摄像头是横放)
                // parameters.setRotation(orientationDegrees);
                parameters.setRotation(orientationDegrees);
                // 获取当前手机屏幕方向
                camera.setParameters(parameters);
            }
            /**
             * ShutterCallback shutter:按下快门的回调 PictureCallback raw:原始图像数据的回调
             * PictureCallback postview:压缩图像 PictureCallback
             * jpeg:压缩成jpg格式的图像数据的回调
             */
            camera.takePicture(null, null, null, this);
        }
        if(v == mSurfaceView){

        }
    }
}