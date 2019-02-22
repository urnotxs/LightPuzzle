package com.xs.lightpuzzle.demo.a_service_demo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * @author xs
 * @description 开新的进程保存图片，AIDL进程间通信
 * @since 2019/1/7
 *
 * 应用越做越大，内存越来越多，将一些独立的组件放到不同的进程，它就不占用主进程的内存空间了。
 * 要创建新的进程，只要在AndroidManifest.xml中定义 service 的时候增加 process 属性，该service就会在独立进程中运行。
 *
 * 首先需要创建一个aidl文件
 * AIDL:Android Interface Definition Language,即Android接口定义语言
 * Android系统中的进程之间不能共 享内存，因此，需要提供一些机制在不同进程之间进行数据通信。
 *
 * 创建完成后，需要rebuild一下工程，build成功后会在gen目录先生成对应的IService.java文件。
 * IService中有一个内部类Stub，它作为IService的一个代理对象，封装了整个通信的请求，简化了调用远程服务的过程。
 */

public class ServiceTestLayout extends FrameLayout {
    public final static String TAG = "TestServiceTAG";
    public final static String CLIENT_SUB = "clientStub";

    private Context mContext;
    private ServiceConnection mServiceConnection;

    public ServiceTestLayout(@NonNull Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        Button testButton = new Button(context);
        testButton.setBackgroundColor(0xFFBFF323);
        testButton.setTextColor(0xFFFFFFFF);
        testButton.setText("Service");
        testButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testAIDL();
//                testMessenger();
            }
        });

        FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(testButton, fParams);
    }

    private void testMessenger() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e(TAG, "onServiceConnected" + " : called");
                // onBind()返回的值及时这里的参数IBinder service，我们就可以用它来实例化Messenger
                Messenger mMessenger = new Messenger(service);

                Message message = Message.obtain();
                message.what = 0x01;
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "onServiceDisconnected" + " : called");
            }
        };

        // 启动服务，并且把客户端传过去，为了回调通信
        Intent intent = new Intent(mContext, TestMessengerService.class);
        mContext.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private void testAIDL() {
        // 首先创建一个ServiceConnection对象，然后将传回来的stub对象实例化
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e(TAG, "onServiceConnected" + " : called");

                // 获得IService实例之后我们就能直接调用doTest了获取远程服务的数据了
                ITestService testServiceAidl = ITestService.Stub.asInterface(service);

                try {
                    testServiceAidl.doTest("test");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "onServiceDisconnected" + " : called");
            }
        };

        ITestClient.Stub clientStub = new ITestClient.Stub() {
            @Override
            public void call(String savePath) throws RemoteException {
                Log.e(TAG, "ITestClient" + " : call");
                saveSuccess(savePath);
            }
        };
        // 启动服务，并且把客户端传过去，为了回调通信
        Intent intent = new Intent(mContext, TestService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CLIENT_SUB, new Messenger(clientStub));
        intent.putExtras(bundle);
        mContext.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    public void saveSuccess(final String savePath) {
        if (mServiceConnection != null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "saveSuccess" + " : success");
                }
            }, 100);
        }
        unBindService();
    }

    public void unBindService() {
        try {
            if (mServiceConnection != null) {
                mContext.unbindService(mServiceConnection);
                mServiceConnection = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
