package com.xs.lightpuzzle.puzzle.a_observer_demo;

/**
 * Created by xs on 2018/11/30.
 */

public interface Observer {
    // 当主题状态改变时,更新通知
    public void update(int version);
}
