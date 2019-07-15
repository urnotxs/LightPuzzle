package com.xs.lightpuzzle.demo.a_kotlin_demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xs.lightpuzzle.R
import kotlinx.android.synthetic.main.activity_practice_kotlin.*

/**
 * Author: xs
 * Create on: 2019/07/12
 * Description: _
 */
class PracticeKotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_kotlin)
        btnTest.setOnClickListener {
            txtTest.text = "kotlin测试成功"
        }
    }
}