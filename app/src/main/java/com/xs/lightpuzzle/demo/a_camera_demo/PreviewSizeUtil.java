package com.xs.lightpuzzle.demo.a_camera_demo;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.Surface;

import java.util.Iterator;
import java.util.List;

/**
 * Created by xs on 2019/1/23.
 */

public class PreviewSizeUtil {
    /**
     * 获取最大的预览/图片尺寸
     *
     * @return 预览尺寸集合
     */
    public static Size getSupportSize(List<Size> listSize) {

        if (listSize == null || listSize.size() <= 0) {

            return null;
        }
        Size  largestSize = listSize.get(0);
        if (listSize.size() > 1) {
            Iterator<Camera.Size> iterator = listSize.iterator();
            while (iterator.hasNext()) {
                Camera.Size size = iterator.next();
                if (size.width > largestSize.width && size.height > largestSize.height) {
                    largestSize = size;
                }
            }
        }
        return largestSize;

    }


    /**
     * 获取拍照时偏移方向的角度(此方法在手机方向没有锁定，为自由切换时易用)
     *
     * @param activity
     * @param cameraId 开启相机的id(0.后相机,1.前相机)
     * @return 偏移的角度
     */
    public static int getCameraDisplayOrientation(Activity activity,
                                                  int cameraId) {

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }
}
