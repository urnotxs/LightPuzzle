package com.xs.lightpuzzle.puzzle.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xs on 2018/11/12.
 * 该线程池用于拼图页面执行的任务，自开始进入拼图页创建，退出拼图页销毁
 * 用于执行很多短期异步任务
 */

public class PuzzleThreadPoolUtils {

    private static ExecutorService sInstance;

    public static ExecutorService getDefault() {
        if (sInstance == null) {
            synchronized (PuzzleThreadPoolUtils.class) {
                if (sInstance == null) {
                    sInstance = Executors.newCachedThreadPool();
                }
            }
        }
        return sInstance;
    }

    public static void shutdownNow() {
        getDefault().shutdownNow();
        sInstance = null;
    }
}
