package com.xs.lightpuzzle.materials;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xs.lightpuzzle.R;

/**
 * Created by xs on 2018/11/2.
 */

public class MaterialListActivity extends AppCompatActivity {

    public static final String EXTRA_STATE = "state";
    public static final String EXTRA_PHOTO_NUM = "photo_num";

    public interface STATE {

        int NORMAL = 0;
        int RESULT = 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiallist);
    }
}
