package com.syncrown.toasty

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log

class AppDataPref {
    companion object {
        private var TAG: String = "jung"

        var AccessToken = ""

        var isMainEvent = true // 메인화면에서 이벤트팝업 더이상 보지않기
        var isArGuide = true // AR 영상 인쇄 카테고리 최초 선택 가이드보여줄것인지


        fun init(activity: Activity?) {
            val manager = activity!!.packageManager
            val packageName = activity.packageName
            val preferences = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)

            AccessToken = preferences.getString("accessToken", null).toString()
            isMainEvent = preferences.getBoolean("isMainEvent", true)
            isArGuide = preferences.getBoolean("isArGuide", true)
        }

        @SuppressLint("CommitPrefEdits")
        fun save(activity: Activity?) {
            val manager = activity!!.packageManager
            val packageName = activity.packageName
            val pref = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)

            val editor = pref.edit()
            editor.putString("accessToken", AccessToken)
            editor.putBoolean("isMainEvent", isMainEvent)
            editor.putBoolean("isArGuide", isArGuide)

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
            Log.i(TAG, "isArGuide = $isArGuide")
            Log.i(TAG, "=================================================")
        }
    }
}