package com.xs.lightpuzzle;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.liulishuo.filedownloader.FileDownloader;
import com.xs.lightpuzzle.data.DataManager;


/**
 * Created by xs on 2018/11/2.
 */

public class LightPuzzleApplication extends Application {

    private static LightPuzzleApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        // 主要是添加下面这句代码
        MultiDex.install(this);
        INSTANCE = this;
        Utils.init(this); // 最强工具类
        FileDownloader.setup(this);
        DataManager.handleApplicationContext(this);
    }

    public static Context getContext() {
        return INSTANCE.getApplicationContext();
    }
}
