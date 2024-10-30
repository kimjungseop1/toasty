package com.syncrown.arpang.ui.component.home

import android.content.Context
import android.content.pm.PackageInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syncrown.arpang.ui.base.BaseViewModel

class MainViewModel : BaseViewModel() {
    /***********************************************************************************************
     * 메인
     **********************************************************************************************/
    private val _onPaperGuideActivityClosed : MutableLiveData<String> = MutableLiveData("")
    val onPaperGuideActivityClosed: LiveData<String> get() = _onPaperGuideActivityClosed

    fun paperGuideActivityClosed(newMessage: String) {
        _onPaperGuideActivityClosed.postValue(newMessage)
    }


    /***********************************************************************************************
     * 홈
     **********************************************************************************************/
    private val _updatePrintStatus = MutableLiveData<String>()
    val onUpdatePrinterStatus: LiveData<String> get() = _updatePrintStatus

    fun updatePrinterStatus(newData: String) {
        _updatePrintStatus.postValue(newData)
    }

    /***********************************************************************************************
     * 보관함
     **********************************************************************************************/


    /***********************************************************************************************
     * 공유공간
     **********************************************************************************************/


    /***********************************************************************************************
     * 더보기
     **********************************************************************************************/
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