package com.xs.lightpuzzle.puzzle.view.signature;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by xs on 2018/11/22.
 */

public class SignatureActivity extends AppCompatActivity {

    public static final String SIGNATURE_PATH = "signature_path";
    public static final String SIGNATURE_HAS_HISTORY = "signature_has_history";


    private SignaturePage mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = new SignaturePage(this, getIntent(), new PageListener() {
            @Override
            public void onClickOkBtn(String picPath) {
                Intent data = new Intent();
                data.putExtra(SIGNATURE_PATH, picPath);
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onClickBackBtn(boolean hasHistory) {
                Intent data = new Intent();
                data.putExtra(SIGNATURE_HAS_HISTORY, hasHistory);
                setResult(Activity.RESULT_OK, data);
                finish();

            }
        });
        setContentView(mPage);
    }

    public interface PageListener {
        void onClickOkBtn(String picPath);

        void onClickBackBtn(boolean hasHistory);
    }
}
