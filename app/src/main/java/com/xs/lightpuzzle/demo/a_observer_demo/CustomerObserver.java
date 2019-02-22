package com.xs.lightpuzzle.demo.a_observer_demo;

import android.util.Log;

import static com.xs.lightpuzzle.demo.a_observer_demo.ObserverTestLayout.TAG;


/**
 * Created by xs on 2018/11/30.
 */

public class CustomerObserver implements Observer {

    //订阅者名字
    private String name;
    private int version;

    public CustomerObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(int version) {
        this.version = version;
        Log.e(TAG, "该杂志出新版本了");
        this.buy();
    }

    public void buy() {
        Log.e(TAG, name + "购买了第" + version + "期的杂志!");
    }
}
