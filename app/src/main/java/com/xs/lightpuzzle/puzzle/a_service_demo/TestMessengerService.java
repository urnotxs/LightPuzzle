package com.xs.lightpuzzle.puzzle.a_service_demo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by xs on 2019/1/9.
 */

public class TestMessengerService extends Service {
    public final static String TAG = "TestServiceTAG";

    /**
     * 首先定义一个Handler来初始化一个Messenger，然后实现handleMessage方法
     */
    @SuppressLint("HandlerLeak")
    private Messenger messenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    Log.e(TAG, "handleMessage" + " : called");
                    break;
            }
        }
    });
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind" + " : called");
        return messenger.getBinder(); // 将messenger返回给客户端
    }
}
