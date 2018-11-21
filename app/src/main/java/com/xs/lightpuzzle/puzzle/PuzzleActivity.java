package com.xs.lightpuzzle.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.xs.lightpuzzle.puzzle.util.Utils;

/**
 * Created by xs on 2018/11/20.
 */

public class PuzzleActivity extends Activity {

    public static final String EXTRA_TEMPLATE_CATEGORY = "template_category";
    public static final String EXTRA_TEMPLATE_ID = "template_id";
    public static final String EXTRA_PHOTOS = "photos";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(this);
        Glide.get(PuzzleActivity.this).clearMemory();
        setContentView(new PuzzlePage(this, getIntent()));
    }
}
