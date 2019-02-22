package com.xs.lightpuzzle.demo;

import android.app.Activity;
import android.os.Bundle;

import com.xs.lightpuzzle.demo.a_egl_demo.TestEGLLayout;
import com.xs.lightpuzzle.demo.a_mediaplayer_demo.TestMediaPlayerLayout;
import com.xs.lightpuzzle.demo.a_mvp_demo.TestFrameLayout;
import com.xs.lightpuzzle.demo.a_observer_demo.ObserverTestLayout;
import com.xs.lightpuzzle.demo.a_recyclerview_demo.RecyclerViewTestLayout;
import com.xs.lightpuzzle.demo.a_service_demo.ServiceTestLayout;
import com.xs.lightpuzzle.demo.a_tactics_demo.TestTacticsBoard;

/**
 * Created by xs on 2018/11/13.
 */

public class TestActivity extends Activity {
    // .puzzle.a_mvp_demo.TestActivity

    private TestMediaPlayerLayout mContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        testMvpDemo();
//        testObserverDemo();
//        testServiceDemo();
//        testRecyclerViewDemo();
//        testMediaPlayerDemo();
//        testEGLDemo();
        testTacticsBoard();
    }

    private void testTacticsBoard(){
        setContentView(new TestTacticsBoard(this));
    }

    private void testEGLDemo(){
        setContentView(new TestEGLLayout(this));
    }

    private void testMediaPlayerDemo(){
        mContainer = new TestMediaPlayerLayout(this);
        setContentView(mContainer);
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
        mContainer.destroy();
        super.onDestroy();
    }
}
