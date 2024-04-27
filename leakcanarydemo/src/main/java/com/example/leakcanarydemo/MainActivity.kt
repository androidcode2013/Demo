package com.example.leakcanarydemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.example.leakcanarydemo.R.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        findViewById<Button>(id.button).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        })

    }
}