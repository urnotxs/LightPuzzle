package com.xs.lightpuzzle.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.a_broadcast_receiver_demo.TestReceiverOne;
import com.xs.lightpuzzle.demo.a_circle_progress_bar_demo.TestCircleProgressBar;
import com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.AdjustmentVideoLayout;
import com.xs.lightpuzzle.demo.a_kotlin_demo.PracticeKotlinActivity;
import com.xs.lightpuzzle.demo.a_lifecycle_demo.TestLifecycleActivity;
import com.xs.lightpuzzle.demo.a_rxjava_demo.RxJavaPractice;
import com.xs.lightpuzzle.demo.a_egl_demo.TestEGLLayout;
import com.xs.lightpuzzle.demo.a_mediaplayer_demo.TestMediaPlayerLayout;
import com.xs.lightpuzzle.demo.a_mvp_demo.TestFrameLayout;
import com.xs.lightpuzzle.demo.a_observer_demo.ObserverTestLayout;
import com.xs.lightpuzzle.demo.a_opengl_surfaceview_demo.TestOpenGLLayout;
import com.xs.lightpuzzle.demo.a_recyclerview_demo.RecyclerViewTestLayout;
import com.xs.lightpuzzle.demo.a_service_demo.ServiceTestLayout;
import com.xs.lightpuzzle.demo.a_tactics_demo.TestTacticsBoard;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.util.ArrayList;

public class TestLayout extends ScrollView implements View.OnClickListener {

    private Context mContext;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams lParams;

    public TestLayout(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(Utils.getRealPixel(20), Utils.getRealPixel(20),
                Utils.getRealPixel(20), Utils.getRealPixel(20));
        FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(linearLayout, fParams);
        {
            lParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lParams.setMargins(0, 0, 0, Utils.getRealPixel(5));

            addButton(context, "KotlinPractice", R.id.demo_test_layout_kotlin);
            addButton(context, "Lifecycle", R.id.demo_test_layout_lifecycle);
            addButton(context, "RxJavaPractices", R.id.demo_test_layout_rx_java);
            addButton(context, "BroadCastReceiver", R.id.demo_test_layout_broadcast);
            addButton(context, "MVP架构", R.id.demo_test_layout_mvp);
            addButton(context, "观察者模式", R.id.demo_test_layout_observer);
            addButton(context, "进程间通信", R.id.demo_test_layout_service);
            addButton(context, "RecyclerView", R.id.demo_test_layout_recycler_view);
            addButton(context, "MediaPlayer", R.id.demo_test_layout_media_player);
            addButton(context, "EGL环境", R.id.demo_test_layout_egl);
            addButton(context, "战术板控件", R.id.demo_test_layout_tactics_board);
            addButton(context, "环形进度条嵌套控件", R.id.demo_test_layout_circle_progress);
            addButton(context, "OpenGL框架", R.id.demo_test_layout_gl_layout);
            addButton(context, "视频调整", R.id.demo_test_layout_adjustment_video);
            addButton(context, "算法测试", R.id.demo_test_layout_algorithm);
        }
    }

    private void addButton(Context context, String text, int viewId) {
        Button button = new Button(context);
        button.setBackgroundColor(0xFFBFF323);
        button.setTextColor(0xFFFFFFFF);
        button.setText(text);
        button.setId(viewId);
        button.setOnClickListener(this);
        linearLayout.addView(button, lParams);
    }

    private boolean isOpenLayout;
    @Override
    public void onClick(View v) {
        isOpenLayout = true;
        this.removeAllViews();
        setBackgroundColor(0xFFFFFFFF);
        switch (v.getId()){
            case R.id.demo_test_layout_mvp:
                testMvpDemo();
                break;
            case R.id.demo_test_layout_observer:
                testObserverDemo();
                break;
            case R.id.demo_test_layout_service:
                testServiceDemo();
                break;
            case R.id.demo_test_layout_recycler_view:
                testRecyclerViewDemo();
                break;
            case R.id.demo_test_layout_media_player:
                testMediaPlayerDemo();
                break;
            case R.id.demo_test_layout_egl:
                testEGLDemo();
                break;
            case R.id.demo_test_layout_tactics_board:
                testTacticsBoard();
                break;
            case R.id.demo_test_layout_circle_progress:
                testCircleProgressBar();
                break;
            case R.id.demo_test_layout_gl_layout:
                testOpenGLLayout();
                break;
            case R.id.demo_test_layout_adjustment_video:
                testAdjustmentVideoLayout();
                break;
            case R.id.demo_test_layout_algorithm:
                testAlgorithm();
                break;
            case R.id.demo_test_layout_broadcast:
                testBroadCastReceiver();
                break;
            case R.id.demo_test_layout_rx_java:
                testRxJavaDemo();
                break;
            case R.id.demo_test_layout_lifecycle:
                Intent intent = new Intent(mContext, TestLifecycleActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.demo_test_layout_kotlin:
                Intent intent2 = new Intent(mContext, PracticeKotlinActivity.class);
                mContext.startActivity(intent2);
                break;
        }
    }

    private void testMvpDemo(){
        this.addView(new TestFrameLayout(mContext));
    }

    private void testObserverDemo(){
        this.addView(new ObserverTestLayout(mContext));
    }

    private void testServiceDemo(){
        this.addView(new ServiceTestLayout(mContext));
    }

    private void testRecyclerViewDemo(){
        this.addView(new RecyclerViewTestLayout(mContext));
    }

    private TestMediaPlayerLayout layout;
    private void testMediaPlayerDemo(){
        layout = new TestMediaPlayerLayout(mContext);
        this.addView(layout);
    }

    private void testEGLDemo(){
        this.addView(new TestEGLLayout(mContext));
    }

    private void testTacticsBoard(){
        this.addView(new TestTacticsBoard(mContext));
    }

    private void testCircleProgressBar(){
        this.addView(new TestCircleProgressBar(mContext));
    }

    private void testOpenGLLayout() {
        this.addView(new TestOpenGLLayout(mContext));
    }

    private void testAdjustmentVideoLayout() {
        this.addView(new AdjustmentVideoLayout(mContext));
    }

    private void testBroadCastReceiver() {
        // 动态注册
//        IntentFilter intentFilter = new IntentFilter("com.example.broadcast");
//        TestReceiverOne receiverOne = new TestReceiverOne();
//        intentFilter.setPriority(1000);
//        mContext.registerReceiver(receiverOne, intentFilter);
//        TestReceiverTwo receiverTwo = new TestReceiverTwo();
//        intentFilter.setPriority(0);
//        mContext.registerReceiver(receiverTwo, intentFilter);
//        TestReceiverThree receiverThree = new TestReceiverThree();
//        intentFilter.setPriority(-1000);
//        mContext.registerReceiver(receiverThree, intentFilter);
        Intent intent = new Intent();
        intent.setAction("com.example.broadcast");
        intent.setComponent(new ComponentName(mContext, TestReceiverOne.class)); // 静态注册需setComponent
//        mContext.sendOrderedBroadcast(intent, null); // 有序广播
        mContext.sendBroadcast(intent); // 无序广播
    }

    private void testRxJavaDemo() {
        RxJavaPractice.testZip();
    }

    private void testAlgorithm() {
        lengthOfLongestSubstring("bbbbb");
        lengthOfLongestSubstring("abcabcbb");
        lengthOfLongestSubstring("pwwkew");
        lengthOfLongestSubstring("ckilbkd");
    }

    /**
     * 求无重叠字符的最长子字符串的字符个数
     * @param input 输入字符窜
     * @return
     */
    public int lengthOfLongestSubstring(String input) {
        ArrayList<Character> list = new ArrayList<>();
        char[] array = input.toCharArray();
        ArrayList<Integer> slipList = new ArrayList<>(); // 记录分段的最后位置索引
        if (array.length <= 1){
            return array.length;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == array[i + 1]) {
                slipList.add(i);
            }
        }
        slipList.add(array.length-1);
        Log.e("Algorithm", "slipList : " + slipList.toString());

        int maxLength = 0;
        int lastSplit = -1;
        int[] startEnd = new int[2];
        for (Integer slip : slipList) {
            int offset = slip - lastSplit;
            if (offset > maxLength) {
                maxLength = offset;
                startEnd[0] = lastSplit + 1;
                startEnd[1] = slip;
            }
            lastSplit = slip;
        }
        Log.e("Algorithm", " start : " + startEnd[0] + " end : " + startEnd[1] + " length : " + maxLength);
        // 已获取到最长不重复字符串
        // 求该字符串包含的字母数目
        for (int i = startEnd[0]; i <= startEnd[1]; i++) {
            if (!list.contains(array[i])){
                list.add(array[i]);
            }
        }
        Log.e("Algorithm", "list : " + list.toString());
        return list.size();

    }

    public void close(){
        if (layout != null){
            layout.destroy();
        }
    }

    public boolean isOpenLayout() {
        return isOpenLayout;
    }
}
