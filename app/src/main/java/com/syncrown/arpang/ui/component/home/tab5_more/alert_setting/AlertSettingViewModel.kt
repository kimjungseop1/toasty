package com.syncrown.arpang.ui.component.home.tab5_more.alert_setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestUserAlertListDto
import com.syncrown.arpang.network.model.RequestUserAlertSettingDto
import com.syncrown.arpang.network.model.ResponseUserAlertListDto
import com.syncrown.arpang.network.model.ResponseUserAlertSettingDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AlertSettingViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 사용자 알림 리스트
     **********************************************************************************************/
    private val userAlertListResponseLiveData: LiveData<NetworkResult<ResponseUserAlertListDto>> =
        arPangRepository.userAlertListLiveDataRepository

    fun userAlertList(requestUserAlertListDto: RequestUserAlertListDto) {
        viewModelScope.launch {
            arPangRepository.requestUserAlertList(requestUserAlertListDto)
        }
    }

    fun userAlertListResponseLiveData(): LiveData<NetworkResult<ResponseUserAlertListDto>> {
        return userAlertListResponseLiveData
    }

    /***********************************************************************************************
     * 사용자 알림 설정
     **********************************************************************************************/
    private val userAlertSettingResponseLiveData: LiveData<NetworkResult<ResponseUserAlertSettingDto>> =
        arPangRepository.userAlertSettingLiveDataRepository

    fun userAlertSetting(requestUserAlertSettingDto: RequestUserAlertSettingDto) {
        viewModelScope.launch {
            arPangRepository.requestUserAlertSetting(requestUserAlertSettingDto)
        }
    }

    fun userAlertSettingResponseLiveData(): LiveData<NetworkResult<ResponseUserAlertSettingDto>> {
        return userAlertSettingResponseLiveData
    }
}