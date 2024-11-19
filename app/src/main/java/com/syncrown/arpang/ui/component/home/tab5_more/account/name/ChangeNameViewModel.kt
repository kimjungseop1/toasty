package com.syncrown.arpang.ui.component.home.tab5_more.account.name

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCheckNickNameDto
import com.syncrown.arpang.network.model.RequestUpdateProfileDto
import com.syncrown.arpang.network.model.ResponseCheckNickNameDto
import com.syncrown.arpang.network.model.ResponseUpdateProfileDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ChangeNameViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /**
     * =============================================================================================
     * 닉네임 중복 체크
     * =============================================================================================
     */
    private val checkNickNameResponseLiveData: LiveData<NetworkResult<ResponseCheckNickNameDto>> =
        arPangRepository.checkNickLiveDataRepository

    fun checkNickName(requestCheckNickNameDto: RequestCheckNickNameDto) {
        viewModelScope.launch {
            arPangRepository.requestCheckNickName(requestCheckNickNameDto)
        }
    }

    fun checkNickNameResponseLiveData(): LiveData<NetworkResult<ResponseCheckNickNameDto>> {
        return checkNickNameResponseLiveData
    }

    /**
     * =============================================================================================
     * 프로필 수정
     * =============================================================================================
     */
    private val updateProfileResponseLiveData: LiveData<NetworkResult<ResponseUpdateProfileDto>> =
        arPangRepository.updateProfileLiveDataRepository

    fun updateProfile(requestUpdateProfileDto: RequestUpdateProfileDto) {
        viewModelScope.launch {
            arPangRepository.requestUpdateProfile(requestUpdateProfileDto)
        }
    }

    fun updateProfileResponseLiveData(): LiveData<NetworkResult<ResponseUpdateProfileDto>> {
        return updateProfileResponseLiveData
    }
}