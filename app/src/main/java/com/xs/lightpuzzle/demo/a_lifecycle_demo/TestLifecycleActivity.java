package com.xs.lightpuzzle.demo.a_lifecycle_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.TestActivity;

/**
 * Author: xs
 * Create on: 2019/07/15
 * Description: _
 */
public class TestLifecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_kotlin);
        Button btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestLifecycleActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

        getLifecycle().addObserver(new TestLifecycleObserver());
    }
}
