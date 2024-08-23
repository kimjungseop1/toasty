package com.syncrown.toasty.ui.component.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.syncrown.toasty.databinding.ActivitySplashBinding
import com.syncrown.toasty.ui.base.BaseActivity
import com.syncrown.toasty.ui.commons.DialogCommon
import com.syncrown.toasty.ui.component.home.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    private val dialogCommon = DialogCommon()

    override fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

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
            showUpdateVersion()
        }
    }

    private fun showUpdateVersion() {
        dialogCommon.showUpdatePopup(supportFragmentManager, {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, {
            finish()
        })
    }

    private fun showServerCheck() {
        dialogCommon.showServerCheckPopup(supportFragmentManager) {
            finish()
            Log.e(TAG, "app finish")
        }
    }
}