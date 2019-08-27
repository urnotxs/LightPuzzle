package com.xs.lightpuzzle;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.liulishuo.filedownloader.FileDownloader;
import com.tencent.bugly.crashreport.CrashReport;
import com.xs.lightpuzzle.data.DataManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


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
//
//        Context context = getApplicationContext();
//        String packageName = context.getPackageName();
//        String processName = getProcessName(Process.myPid());
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        Log.e("urnot_xs", "processName: " + processName + " packageName: " + packageName);
        Log.e("CrashReport", "context: " + getApplicationContext() + " appId: da6e0833c3");
        CrashReport.initCrashReport(getApplicationContext(), "da6e0833c3", true);
        // Android P 需要进行适配(限制了明文流量的网络请求，非加密的流量请求都会被系统禁止掉)
    }

    public static Context getContext() {
        return INSTANCE.getApplicationContext();
    }

    private String getProcessName(int myPid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + myPid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName))
                processName = processName.trim();
            return processName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
