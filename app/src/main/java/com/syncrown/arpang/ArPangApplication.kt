package com.syncrown.arpang

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import com.syncrown.arpang.db.push_db.PushMessageDatabase
import com.syncrown.arpang.ui.commons.FontManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        FontManager.init(this)

        FirebaseApp.initializeApp(this)

        FacebookSdk.setClientToken(getString(R.string.facebook_token))
        FacebookSdk.sdkInitialize(applicationContext)
        FacebookSdk.fullyInitialize()

        CoroutineScope(Dispatchers.IO).launch {
            val dao = PushMessageDatabase.getDatabase(applicationContext).pushMessageDao()

            //30일 지난 오래된 알림 메시지 삭제
            dao.deleteOldMessages()
        }
    }
}