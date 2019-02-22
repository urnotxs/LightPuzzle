package com.xs.lightpuzzle.demo.a_camera_demo;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xs.lightpuzzle.puzzle.util.Utils;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by xs on 2019/1/23.
 */
@RuntimePermissions
public class CameraActivity extends Activity {

    private CameraLayout mContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testCameraDemo();
        CameraActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.CAMERA})
    public void requestPermission() {
        mContainer.initSurface();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        CameraActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void testCameraDemo() {
        Utils.init(this);
        mContainer = new CameraLayout(this);
        setContentView(mContainer);
    }
}
