package com.xs.lightpuzzle.demo.a_tactics_demo;

import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * @author xs
 * @description 弱引用
 * @since 2019/2/21
 */

public abstract class WeakHandler<T> extends Handler {
    private WeakReference<T> owner;

    public WeakHandler(T owner) {
        this.owner = new WeakReference<>(owner);
    }

    public T getOwner(){
        return owner.get();
    }
}