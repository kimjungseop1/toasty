package com.syncrown.arpang.ui.component.home.tab5_more.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestUserProfileDto
import com.syncrown.arpang.network.model.ResponseUserProfileDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AccountManageViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /**
     * =============================================================================================
     * 사용자 정보 가져오기
     * =============================================================================================
     */
    private val getUserProfileResponseLiveData: LiveData<NetworkResult<ResponseUserProfileDto>> =
        arPangRepository.userprofileLiveDataRepository

    fun getUserProfile(requestUserProfileDto: RequestUserProfileDto) {
        viewModelScope.launch {
            arPangRepository.requestUserProfile(requestUserProfileDto)
        }
    }

    fun getUserProfileResponseLiveData(): LiveData<NetworkResult<ResponseUserProfileDto>> {
        return getUserProfileResponseLiveData
    }
}