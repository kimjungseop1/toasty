package com.syncrown.toasty.ui.component.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.syncrown.toasty.AppData
import com.syncrown.toasty.databinding.ActivitySplashBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.component.home.MainActivity
import com.syncrown.toasty.ui.component.login.main.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    private val dialogCommon = DialogCommon()

    @SuppressLint("SetTextI18n")
    override fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.versionTxt.text = "v" + splashViewModel.appVersion(this@SplashActivity)
            }
        }
    }

    override fun initViewBinding() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(1500)

            if (AppData.isUpdate) {
                showUpdateVersion()
                return@launch
            }

            if (AppData.isSystemCheck) {
                showServerCheck("9월30일")
                return@launch
            }

            if (AppData.isLogin) {
                goMain()
            } else {
                goLogin()
            }

        }
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showUpdateVersion() {
        dialogCommon.showUpdatePopup(supportFragmentManager, {

        }, {

        })
    }

    private fun showServerCheck(date: String) {
        dialogCommon.showServerCheckPopup(
            supportFragmentManager,
            {

            },
            date
        )

    }
}