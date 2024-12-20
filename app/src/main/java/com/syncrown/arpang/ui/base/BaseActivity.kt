package com.syncrown.arpang.ui.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.ui.component.home.MainActivity
import com.syncrown.arpang.ui.component.login.LoginActivity

abstract class BaseActivity : AppCompatActivity() {
    val TAG: String = "jung"
    abstract fun observeViewModel()
    abstract fun initViewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 403 에러 발생시 로그인화면으로 이동
     */
    fun goLogin() {
        AppDataPref.clear(this)

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}