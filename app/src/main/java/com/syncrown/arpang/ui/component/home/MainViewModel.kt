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
    //TODO 용지연결페이지에서 배너를 눌러 메인의 탭4번으로 바로 이동할떄 체킹
    private val _onPaperGuideActivityClosed: MutableLiveData<String> = MutableLiveData("")
    val onPaperGuideActivityClosed: LiveData<String> get() = _onPaperGuideActivityClosed

    fun paperGuideActivityClosed(newMessage: String) {
        _onPaperGuideActivityClosed.postValue(newMessage)
    }


    /***********************************************************************************************
     * 홈
     **********************************************************************************************/
    //TODO 프린트가 연결되어있는지 여부 (메인액티비티, 홈프레그먼트 둘다 사용)
    private val _connectState = MutableLiveData<Boolean>()
    val connectStateShare: LiveData<Boolean> get() = _connectState

    fun updateConnectedState(newState: Boolean) {
        _connectState.value = newState
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