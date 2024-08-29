package com.syncrown.toasty.ui.component.splash

import android.content.Context
import android.content.pm.PackageInfo
import com.syncrown.toasty.network.ToastyRepository
import com.syncrown.toasty.ui.base.BaseViewModel

class SplashViewModel: BaseViewModel() {
//    private var toastyRepository: ToastyRepository = ToastyRepository() //TODO baseurl 없으면 주석 유지

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
}