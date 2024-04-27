package com.example.leakcanarydemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demo.TestDataModel

class TestActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        TestDataModel.sinstant.activitys.add(this)
    }
}