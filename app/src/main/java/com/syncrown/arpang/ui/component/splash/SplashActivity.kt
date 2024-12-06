package com.syncrown.arpang.ui.component.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.messaging.FirebaseMessaging
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.databinding.ActivitySplashBinding
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.ui.base.BaseActivity
import com.syncrown.arpang.ui.commons.DialogCommon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    companion object {
        private const val REQUEST_UPDATE_CODE = 100
        private const val REQUEST_PERMISSION_CODE = 101
    }

    private val dialogCommon = DialogCommon()

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.CAMERA
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

    @SuppressLint("SetTextI18n")
    override fun observeViewModel() {
        lifecycleScope.launch {
            splashViewModel.insertUserTokenResponseLiveData().observe(this@SplashActivity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        Log.e(TAG, "토큰 전송 완료")
                    }

                    is NetworkResult.NetCode -> {
                        Log.e("jung","실패 : ${result.message}")
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "오류 : ${result.message}")
                    }
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.versionTxt.text = "v" + splashViewModel.appVersion(this@SplashActivity)
            }
        }

        splashViewModel.loginResponseLiveData().observe(this) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data.let { data ->
                        when (data?.msgCode) {
                            "SUCCESS" -> {
                                AppDataPref.save(this)
                                goMain()
                            }

                            "FAIL" -> { Log.e("jung","Consent FAIL") }
                            "NONE_ID" -> { Log.e("jung","Consent NONE_ID") }
                            else -> {}
                        }
                    }
                }

                is NetworkResult.NetCode -> {
                    Log.e("jung","실패 : ${result.message}")
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "오류 : ${result.message}")
                }
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
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("jung", "fcm token : ${task.result}")
                    splashViewModel.insertUserToken(this@SplashActivity, task.result)
                }
            }

            delay(1500)

            // 앱 업데이트 체크
            splashViewModel.checkForUpdate()
            splashViewModel.onUpdateAvailableListener = {
                showUpdateVersion()
            }

            // userid가 널이면 로그인화면으로 널이 아니면 로그인
            if (AppDataPref.userId.isEmpty()) {
                goLogin()
            } else {
                val requestLoginDto = RequestLoginDto()
                requestLoginDto.user_id = AppDataPref.userId
                splashViewModel.login(requestLoginDto)
            }
        }
    }

    private fun showUpdateVersion() {
        dialogCommon.showUpdatePopup(supportFragmentManager, {
            //TODO cancel
        }, {
            //TODO  ok
            splashViewModel.checkForUpdateAndStart(this)
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