package com.xs.lightpuzzle.puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.xs.lightpuzzle.puzzle.util.Utils;

import static com.xs.lightpuzzle.puzzle.PuzzlePage.REQ_CODE_EDIT_LABEL;
import static com.xs.lightpuzzle.puzzle.PuzzlePage.REQ_CODE_TRANSFORM_TEMPLATE;
import static com.xs.lightpuzzle.puzzle.PuzzlePage.REQ_CODE_EDIT_SIGNATURE;

/**
 * Created by xs on 2018/11/20.
 */

public class PuzzleActivity extends Activity {

    public static final String EXTRA_TEMPLATE_CATEGORY = "template_category";
    public static final String EXTRA_TEMPLATE_ID = "template_id";
    public static final String EXTRA_TEMPLATE_RATIO = "template_ratio";
    public static final String EXTRA_PHOTOS = "photos";

    private PuzzlePage mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(this);
        Glide.get(PuzzleActivity.this).clearMemory();
        mPage = new PuzzlePage(this, getIntent());
        setContentView(mPage);
    }

    @Override
    public void onBackPressed() {
        mPage.onBack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_TRANSFORM_TEMPLATE) {
            mPage.handleTransformTemplateResult(resultCode, data);
        } else if (requestCode == REQ_CODE_EDIT_SIGNATURE) {
            mPage.handleEditSignatureResult(resultCode, data);
        } else if (requestCode == REQ_CODE_EDIT_LABEL) {
            mPage.handleEditLabelResult(resultCode, data);
        }
    }
}
