package com.xs.lightpuzzle.puzzle.a_service_demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.xs.lightpuzzle.puzzle.a_service_demo.ServiceTestLayout.CLIENT_SUB;

/**
 * Created by xs on 2019/1/7.
 */

public class TestService extends Service {
    public final static String TAG = "TestServiceTAG";

    private ITestClient mClient;

    private TestBinder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new TestBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 实现onBind()方法，返回Stub实例。
        Log.e(TAG, "onBind" + " : called");
        if (intent != null && intent.getExtras() != null){
            Messenger messenger = intent.getExtras().getParcelable(CLIENT_SUB);
            if (messenger != null){
                mClient = ITestClient.Stub.asInterface(messenger.getBinder());
            }
        }
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mClient = null;
        Log.e(TAG, "onUnbind" + " : called");
        System.exit(0);
        return super.onUnbind(intent);
    }


    /**
     * IService中有一个内部类Stub，它作为IService的一个代理对象，封装了整个通信的请求，简化了调用远程服务的过程。
     * TestService中创建一个Stub的实例，实现了我们获取数据的方法doTest()，里面返回了TestServcie的一个成员变量的值
     */
    private class TestBinder extends ITestService.Stub{

        @Override
        public void doTest(final String jsonPath) throws RemoteException {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 在另一个进程service里做相关处理
                    Log.e(TAG, "doTest" + jsonPath);
                    String savePath = "savePath";
                    sendMessageToClient(savePath);
                }
            }).start();
        }
    }

    private void sendMessageToClient(String savePath) {
        try {
            mClient.call(savePath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
