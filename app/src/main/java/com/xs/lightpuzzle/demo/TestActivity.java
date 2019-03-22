package com.xs.lightpuzzle.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.a_circle_progress_bar_demo.TestCircleProgressBar;
import com.xs.lightpuzzle.demo.a_demo_adjustment_video_view.AdjustmentVideoLayout;
import com.xs.lightpuzzle.demo.a_egl_demo.TestEGLLayout;
import com.xs.lightpuzzle.demo.a_mediaplayer_demo.TestMediaPlayerLayout;
import com.xs.lightpuzzle.demo.a_mvp_demo.TestFrameLayout;
import com.xs.lightpuzzle.demo.a_observer_demo.ObserverTestLayout;
import com.xs.lightpuzzle.demo.a_opengl_surfaceview_demo.TestOpenGLLayout;
import com.xs.lightpuzzle.demo.a_recyclerview_demo.RecyclerViewTestLayout;
import com.xs.lightpuzzle.demo.a_service_demo.ServiceTestLayout;
import com.xs.lightpuzzle.demo.a_tactics_demo.TestTacticsBoard;
import com.xs.lightpuzzle.opengl.gllayer.filter.base.IFilter;
import com.xs.lightpuzzle.puzzle.util.Utils;

import java.util.ArrayList;

/**
 * Created by xs on 2018/11/13.
 */

public class TestActivity extends Activity {
    // .puzzle.a_mvp_demo.TestActivity

//    private TestMediaPlayerLayout mContainer;
    private TestLayout mContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(this);
        initLayout();
//        testMvpDemo();
//        testObserverDemo();
//        testServiceDemo();
//        testRecyclerViewDemo();
//        testMediaPlayerDemo();
//        testEGLDemo();
//        testTacticsBoard();
//        testCircleProgressBar();
//        testOpenGLLayout();
//        testAdjustmentVideoLayout();
//        testAlgorithm();
    }

    private void initLayout() {
        mContainer = new TestLayout(this);
        setContentView(mContainer);
    }


    private void testAlgorithm() {
        lengthOfLongestSubstring("bbbbb");
        lengthOfLongestSubstring("abcabcbb");
        lengthOfLongestSubstring("pwwkew");
        lengthOfLongestSubstring("ckilbkd");
    }


    private void testAdjustmentVideoLayout() {
        Utils.init(this);
        setContentView(new AdjustmentVideoLayout(this));
    }

    private void testOpenGLLayout() {
        Utils.init(this);
        setContentView(new TestOpenGLLayout(this));
    }

    private void testCircleProgressBar(){
        Utils.init(this);
        setContentView(new TestCircleProgressBar(this));
    }
    private void testTacticsBoard(){
        setContentView(new TestTacticsBoard(this));
    }

    private void testEGLDemo(){
        setContentView(new TestEGLLayout(this));
    }

    private void testMediaPlayerDemo(){
//        mContainer = new TestMediaPlayerLayout(this);
//        setContentView(mContainer);
    }

    private void testServiceDemo(){
        setContentView(new ServiceTestLayout(this));
    }

    private void testMvpDemo(){
        setContentView(new TestFrameLayout(this));
    }

    private void testObserverDemo(){
        setContentView(new ObserverTestLayout(this));
    }

    private void testRecyclerViewDemo(){
        setContentView(new RecyclerViewTestLayout(this));
    }

    @Override
    protected void onDestroy() {
        mContainer.close();
        super.onDestroy();
    }

    /**
     * 无重复字符的最长子串
     * @param input
     * @return
     */

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
}
