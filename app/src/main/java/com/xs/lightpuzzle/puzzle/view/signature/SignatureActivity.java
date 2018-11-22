package com.xs.lightpuzzle.puzzle.view.signature;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by xs on 2018/11/22.
 */

public class SignatureActivity extends AppCompatActivity {

    private SignaturePage mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = new SignaturePage(this, getIntent());
        setContentView(mPage);
    }
}
