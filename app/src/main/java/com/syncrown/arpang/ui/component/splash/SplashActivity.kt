package com.syncrown.arpang.ui.component.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivitySplashBinding
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.DialogCommon
import com.syncrown.arpang.ui.commons.SplashViewModelFactory
import com.syncrown.arpang.ui.component.home.MainActivity
import com.syncrown.arpang.ui.component.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashViewModel: SplashViewModel

    companion object {
        private const val REQUEST_UPDATE_CODE = 100
        private const val REQUEST_PERMISSION_CODE = 101
    }

    private val dialogCommon = DialogCommon()

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.CAMERA
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

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

        if (checkAndRequestPermissions()) {
            proceedToNextStep()
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        return if (deniedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                deniedPermissions.toTypedArray(),
                REQUEST_PERMISSION_CODE
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            val allPermissionsGranted =
                grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }

            if (allPermissionsGranted) {
                proceedToNextStep()
            } else {
                if (permissions.isNotEmpty() && !ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        permissions[0]
                    )
                ) {
                    // "다시 묻지 않음"을 선택한 경우 설정으로 안내
                    showPermissionDeniedDialog()
                } else {
                    // 단순 권한 거부
                    showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        val dialogCommon = DialogCommon()
        dialogCommon.showPermissionPopup(supportFragmentManager, {
            finish()
        }, {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = android.net.Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        })
    }

    private fun proceedToNextStep() {
        lifecycleScope.launch {
            delay(1500)

            // 앱 업데이트 체크
            splashViewModel.checkForUpdate()
            splashViewModel.onUpdateAvailableListener = {
                showUpdateVersion()
            }

            // 임시로 로그인 화면으로 이동
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