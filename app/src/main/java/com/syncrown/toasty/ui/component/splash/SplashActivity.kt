package com.syncrown.toasty.ui.component.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.syncrown.toasty.AppDataPref
import com.syncrown.toasty.databinding.ActivitySplashBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.commons.SplashViewModelFactory
import com.syncrown.toasty.ui.component.home.MainActivity
import com.syncrown.toasty.ui.component.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashViewModel: SplashViewModel
    companion object {
        private const val REQUEST_UPDATE_CODE = 100
    }


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

        AppDataPref.init(this)
        AppDataPref.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = SplashViewModelFactory(application)
        splashViewModel = ViewModelProvider(this, factory).get(SplashViewModel::class.java)

        lifecycleScope.launch {
            delay(1500)

            //TODO 앱 업데이 체크후 팝업 발생
            splashViewModel.checkForUpdate()
            splashViewModel.onUpdateAvailableListener = {
                showUpdateVersion()
            }



            //임시
            goLogin()

        }
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showUpdateVersion() {
        dialogCommon.showUpdatePopup(supportFragmentManager, {
            //TODO cancel
        }, {
            //TODO  ok
            splashViewModel.checkForUpdateAndStart(this, REQUEST_UPDATE_CODE)
        })
    }

    private fun showServerCheck(date: String) {
        dialogCommon.showServerCheckPopup(
            supportFragmentManager,
            {
                //TODO close
            },
            date
        )

    }
}