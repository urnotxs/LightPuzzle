package com.xs.lightpuzzle.puzzle.exchanged.subject;

import com.xs.lightpuzzle.puzzle.exchanged.observer.IObserver;

/**
 * Created by xs on 2018/11/30.
 */

public interface ISubject {

    // 添加观察者
    void addObserver(IObserver observer);

    // 移除观察者
    void deleteObserver(IObserver observer);

    // 当主题方法改变时,这个方法被调用,通知所有的观察者
    void notifyObserver();
}
