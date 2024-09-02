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



        // test flag start ---
        var isUpdate = false // 버전 체크
        var isSystemCheck = false // 시스템 점검중
        var isLogin = false // 로그인 되어있는가
        var isEmailJoin = true // 이미 가입되어있는 이메일인가
        var isArGuide = true // AR 영상 인쇄 카테고리 최초 선택 가이드보여줄것인지
        var isCatridge = true // 용지가 장착되어있는가
        // --- test flag end


        fun init(activity: Activity?) {
            val manager = activity!!.packageManager
            val packageName = activity.packageName
            val preferences = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)

            AccessToken = preferences.getString("accessNToken", null).toString()
            isMainEvent = preferences.getBoolean("isMainEvent", true)
        }

        @SuppressLint("CommitPrefEdits")
        fun save(activity: Activity?) {
            val manager = activity!!.packageManager
            val packageName = activity.packageName
            val pref = activity.getSharedPreferences(packageName, Context.MODE_PRIVATE)

            val editor = pref.edit()
            editor.putString("accessNToken", AccessToken)
            editor.putBoolean("isMainEvent", isMainEvent)
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
            Log.i(TAG, "accessNToken = $AccessToken")
            Log.i(TAG, "=================================================")
        }
    }
}