package com.xs.lightpuzzle.puzzle.a_mvp_demo;

import android.app.Activity;
import android.os.Bundle;

import com.xs.lightpuzzle.puzzle.a_observer_demo.ObserverTestLayout;

/**
 * Created by xs on 2018/11/13.
 */

public class TestActivity extends Activity {
    // .puzzle.a_mvp_demo.TestActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        testMvpDemo();
        testObserverDemo();
    }

    private void testMvpDemo(){
        setContentView(new TestFrameLayout(this));
    }

    private void testObserverDemo(){
        setContentView(new ObserverTestLayout(this));
    }

}
