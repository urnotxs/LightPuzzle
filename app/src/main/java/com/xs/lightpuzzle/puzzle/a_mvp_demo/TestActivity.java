package com.xs.lightpuzzle.puzzle.a_mvp_demo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by xs on 2018/11/13.
 */

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TestFrameLayout(this));
    }

}
