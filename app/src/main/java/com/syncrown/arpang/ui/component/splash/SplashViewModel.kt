package com.syncrown.arpang.ui.component.splash

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.ArPangApplication
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.RequestUserTokenRegDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.network.model.ResponseUserTokenRegDto
import com.syncrown.arpang.ui.commons.CommonFunc
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val arPangRepository: ArPangRepository = ArPangRepository()
    private val insertUserTokenResponseLiveData: LiveData<NetworkResult<ResponseUserTokenRegDto>> =
        arPangRepository.userTokenRegisterLiveDataRepository

    /**
     * =============================================================================================
     * 앱 업데이트 체크
     * =============================================================================================
     */
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(application)
    var onUpdateAvailableListener: (() -> Unit)? = null

    fun appVersion(context: Context): String? {
        try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(
                context.packageName, 0
            )

            return pInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun checkForUpdate() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) ||
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
            ) {
                // 업데이트가 가능할 경우 알림 콜백 호출
                onUpdateAvailableListener?.invoke()
            }
        }
    }

    private fun startImmediateUpdate(
        appUpdateInfo: AppUpdateInfo,
        activity: Activity
    ) {
        val appUpdateOptions = AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE)
        appUpdateManager.startUpdateFlow(
            appUpdateInfo,
            activity,
            appUpdateOptions
        )
    }

    private fun startFlexibleUpdate(
        appUpdateInfo: AppUpdateInfo,
        activity: Activity
    ) {
        val appUpdateOptions = AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
        appUpdateManager.startUpdateFlow(
            appUpdateInfo,
            activity,
            appUpdateOptions
        )
    }

    fun checkForUpdateAndStart(activity: Activity) {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // 유연한 업데이트가 가능할 때
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    startFlexibleUpdate(appUpdateInfo, activity)
                }
                // 즉시 업데이트가 가능할 때
                else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startImmediateUpdate(appUpdateInfo, activity)
                }
            }
        }
    }

    /**
     * =============================================================================================
     * 사용자 토큰 등록
     * =============================================================================================
     */
    fun insertUserToken(
        context: Context,
        userToken: String
    ) {
        viewModelScope.launch {
            val requestUserTokenRegDto = RequestUserTokenRegDto()
            requestUserTokenRegDto.apply {
                user_token = userToken

                app_id = "APP_ARPANG"

                device_id = CommonFunc.getDeviceUuid(context)

                if (AppDataPref.userId.isNotEmpty()) {
                    user_id = AppDataPref.userId
                }
            }

            arPangRepository.requestUserTokenRegister(requestUserTokenRegDto)
        }
    }

    fun insertUserTokenResponseLiveData(): LiveData<NetworkResult<ResponseUserTokenRegDto>> {
        return insertUserTokenResponseLiveData
    }

    /**
     * =============================================================================================
     * 로그인 - 자동로그인
     * =============================================================================================
     */
    private val loginResponseLiveData: LiveData<NetworkResult<ResponseLoginDto>> =
        arPangRepository.loginLiveDataRepository

    fun login(requestLoginDto: RequestLoginDto) {
        viewModelScope.launch {
            arPangRepository.requestLogin(requestLoginDto)
        }
    }

    fun loginResponseLiveData(): LiveData<NetworkResult<ResponseLoginDto>> {
        return loginResponseLiveData
    }
}