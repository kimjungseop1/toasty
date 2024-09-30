package com.syncrown.arpang

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.FacebookSdk
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK

class ArPangApplication : Application() {
    private var instance: ArPangApplication? = null
    companion object {
        private const val KAKAO_APP_KEY = "80fe788ee827bf52b012dfb4c6b78a3a"

        private const val OAUTH_CLIENT_ID = "qxjFTG0prIbVkN15juOp"
        private const val OAUTH_CLIENT_SECRET = "7t2uWeM5zY"
        private const val OAUTH_CLIENT_NAME = "Toasty"
    }

    fun getGlobalApplication(): ArPangApplication? {
        checkNotNull(instance) { "instance is null" }
        return instance
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        Log.e("jung","getKeyHash : " + Utility.getKeyHash(this))

        KakaoSdk.init(this, KAKAO_APP_KEY)

        NaverIdLoginSDK.initialize(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)

        FacebookSdk.sdkInitialize(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}