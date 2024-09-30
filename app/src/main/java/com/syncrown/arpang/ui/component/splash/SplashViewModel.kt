package com.syncrown.arpang.ui.component.splash

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import androidx.lifecycle.AndroidViewModel
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task

class SplashViewModel(application: Application) : AndroidViewModel(application) {
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
        activity: Activity,
        requestCode: Int
    ) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.IMMEDIATE,
            activity,
            requestCode
        )
    }

    private fun startFlexibleUpdate(
        appUpdateInfo: AppUpdateInfo,
        activity: Activity,
        requestCode: Int
    ) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.FLEXIBLE,
            activity,
            requestCode
        )
    }

    fun checkForUpdateAndStart(activity: Activity, requestUpdateCode: Int) {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // 유연한 업데이트가 가능할 때
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    startFlexibleUpdate(appUpdateInfo, activity, requestUpdateCode)
                }
                // 즉시 업데이트가 가능할 때
                else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startImmediateUpdate(appUpdateInfo, activity, requestUpdateCode)
                }
            }
        }
    }

}