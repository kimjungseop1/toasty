package com.syncrown.arpang

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log

class AppDataPref {
    companion object {
        private var TAG: String = "jung"

        var AccessToken = ""

        var isMainEvent = true // 메인화면에서 이벤트팝업 더이상 보지않기


        fun init(activity: Activity?) {
            val manager = activity!!.packageManager
            val packageName = activity.packageName
            val preferences = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)

            AccessToken = preferences.getString("accessToken", null).toString()
            isMainEvent = preferences.getBoolean("isMainEvent", true)
        }

        @SuppressLint("CommitPrefEdits")
        fun save(activity: Activity?) {
            val manager = activity!!.packageManager
            val packageName = activity.packageName
            val pref = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)

            val editor = pref.edit()
            editor.putString("accessToken", AccessToken)
            editor.putBoolean("isMainEvent", isMainEvent)

            editor.apply()
        }

        fun clear(activity: Activity) {
            val packageName = activity.packageName
            val pref = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.clear()
            editor.apply()
        }

        fun show() {
            Log.i(TAG, "=================================================")
            Log.i(TAG, "accessToken = $AccessToken")
            Log.i(TAG, "=================================================")
        }
    }
}