package com.xs.lightpuzzle.demo.a_lifecycle_demo;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Author: xs
 * Create on: 2019/07/15
 * Description: _
 */
public class TestLifecycleObserver implements DefaultLifecycleObserver {
    private static final String TAG = "TestLifecycleObserver";

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onCreate()");
        Log.d(TAG, "当前生命周期状态：" + owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onStart()");
        Log.d(TAG, "当前生命周期状态：" + owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onResume()");
        Log.d(TAG, "当前生命周期状态：" + owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onPause()");
        Log.d(TAG, "当前生命周期状态：" + owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onStop()");
        Log.d(TAG, "当前生命周期状态：" + owner.getLifecycle().getCurrentState().name());
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.d(TAG, "onDestroy()");
        Log.d(TAG, "当前生命周期状态：" + owner.getLifecycle().getCurrentState().name());
    }
}
