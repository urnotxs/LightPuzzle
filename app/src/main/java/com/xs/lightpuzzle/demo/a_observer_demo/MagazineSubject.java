package com.xs.lightpuzzle.demo.a_observer_demo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2018/11/30.
 */

public class MagazineSubject implements Subject {

    // 存放订阅者
    private List<Observer> observerList = new ArrayList<>();

    // 期刊版本
    private int version;

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        if (observerList.contains(observer)) {
            observerList.remove(observer);
        }
    }

    @Override
    public void notifyObserver() {
        for (Observer observer : observerList) {
            observer.update(version);
        }
    }

    // 该杂志发行了新版本
    public void publish() {
        // 新版本
        version++;
        // 信息更新完毕，通知所有观察者
        notifyObserver();
    }
}
