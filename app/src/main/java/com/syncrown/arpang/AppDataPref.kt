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

        var isArGuideFirst = true // AR영상인쇄 처음 클릭시 가이드화면부터 보여주도록

        var isArPrintPreView = true // ar인쇄 미리보기
        var isTwoCutPreView = true // 인생두컷 미리보기
        var isFreePreView = true // 자유인쇄 미리보기
        var isLabelPreView = true // 라벨프린트 미리보기
        var isFestivalPreView = true // 행사스티커 미리보기

        fun init(activity: Activity?) {
            val manager = activity!!.packageManager
            val packageName = activity.packageName
            val preferences = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)

            AccessToken = preferences.getString("accessToken", null).toString()
            isMainEvent = preferences.getBoolean("isMainEvent", true)

            isArGuideFirst = preferences.getBoolean("isArGuideFirst", true)

            isArPrintPreView = preferences.getBoolean("isArPrintPreView", true)
            isTwoCutPreView = preferences.getBoolean("isTwoCutPreView", true)
            isFreePreView = preferences.getBoolean("isFreePreView", true)
            isLabelPreView = preferences.getBoolean("isLabelPreView", true)
            isFestivalPreView = preferences.getBoolean("isFestivalPreView", true)
        }

        @SuppressLint("CommitPrefEdits")
        fun save(activity: Activity?) {
            val manager = activity!!.packageManager
            val packageName = activity.packageName
            val pref = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)

            val editor = pref.edit()
            editor.putString("accessToken", AccessToken)
            editor.putBoolean("isMainEvent", isMainEvent)

            editor.putBoolean("isArGuideFirst", isArGuideFirst)

            editor.putBoolean("isArPrintPreView", isArPrintPreView)
            editor.putBoolean("isTwoCutPreView", isTwoCutPreView)
            editor.putBoolean("isFreePreView", isFreePreView)
            editor.putBoolean("isLabelPreView", isLabelPreView)
            editor.putBoolean("isFestivalPreView", isFestivalPreView)

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