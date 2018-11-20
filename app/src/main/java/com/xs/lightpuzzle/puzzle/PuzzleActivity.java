package com.xs.lightpuzzle.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * Created by xs on 2018/11/20.
 */

public class PuzzleActivity extends Activity {

    public static final String EXTRA_TEMPLATE_CATEGORY = "template_category";
    public static final String EXTRA_TEMPLATE_ID = "template_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(this);
        setContentView(new PuzzlePage(this));
    }
}
