package com.example.demo

import android.app.Activity
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.squareup.leakcanary.RefWatcher

class TestActivity: ComponentActivity() {
    var TAG:String = "TestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate")
        setContent { 
            Text(text = "TestActivity")
        }
        TestDataModel.sinstant.activitys.add(this)
        val refWatcher: RefWatcher? = TestApp.getRefWatcher(this)
        refWatcher?.watch(this);
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }
}

@Composable
fun SimpleText() {
    Text("Hello World")
}