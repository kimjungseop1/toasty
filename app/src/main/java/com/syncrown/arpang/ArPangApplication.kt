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

    private val kakao_app_key = "80fe788ee827bf52b012dfb4c6b78a3a"

    private val OAUTH_CLIENT_ID = "qxjFTG0prIbVkN15juOp"
    private val OAUTH_CLIENT_SECRET = "7t2uWeM5zY"
    private val OAUTH_CLIENT_NAME = "Toasty"

    fun getGlobalApplication(): ArPangApplication? {
        checkNotNull(instance) { "instance is null" }
        return instance
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        Log.e("jung","getKeyHash : " + Utility.getKeyHash(this))

        KakaoSdk.init(this, kakao_app_key)

        NaverIdLoginSDK.initialize(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)

        FacebookSdk.sdkInitialize(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}