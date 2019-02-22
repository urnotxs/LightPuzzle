package com.xs.lightpuzzle.demo.a_observer_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

/**
 * @author xs
 * @description 观察者模式
 * @since 2019/11/30
 */

public class ObserverTestLayout extends FrameLayout {
    public final static String TAG = "OBSERVER";

    public ObserverTestLayout(@NonNull Context context) {
        super(context);

        // 创建主题(被观察者)
        MagazineSubject magazine = new MagazineSubject();
        // 创建三个不同的观察者
        CustomerObserver a = new CustomerObserver("A");
        CustomerObserver b = new CustomerObserver("B");
        CustomerObserver c = new CustomerObserver("C");
        // 将观察者注册到主题中
        magazine.addObserver(a);
        magazine.addObserver(b);
        magazine.addObserver(c);

        //更新主题的数据，当数据更新后，会自动通知所有已注册的观察者
        magazine.publish();
    }
}
