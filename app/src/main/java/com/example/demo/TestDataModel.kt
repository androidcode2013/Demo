package com.example.demo

import android.app.Activity

class TestDataModel {

    var activitys = ArrayList<Activity>()
    companion object{
        @JvmStatic
        @Volatile
        var sinstant = TestDataModel()
        @JvmStatic
        fun getsInstant():TestDataModel{
            return sinstant
        }
    }
}