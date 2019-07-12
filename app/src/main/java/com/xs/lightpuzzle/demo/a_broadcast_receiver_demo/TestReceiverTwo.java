package com.xs.lightpuzzle.demo.a_broadcast_receiver_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Author: xs
 * Create on: 2019/06/13
 * Description: _
 */
public class TestReceiverTwo extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = getResultData();
        Log.e("urnot_xs", "TestReceiverTwo :" + message);
        abortBroadcast(); // 终止广播
    }
}
