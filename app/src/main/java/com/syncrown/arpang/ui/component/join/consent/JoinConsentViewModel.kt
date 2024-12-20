package com.syncrown.arpang.ui.component.join.consent

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.L
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCheckNickNameDto
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.ResponseCheckNickNameDto
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class JoinConsentViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /**
     * =============================================================================================
     * 가입신청
     * =============================================================================================
     */
    private val joinMemberResponseLiveData: LiveData<NetworkResult<ResponseJoinDto>> =
        arPangRepository.joinLiveDataRepository

    fun joinMember(requestJoinDto: RequestJoinDto) {
        viewModelScope.launch {
            arPangRepository.requestJoin(requestJoinDto)
        }
    }

    fun joinMemberResponseLiveData(): LiveData<NetworkResult<ResponseJoinDto>> {
        return joinMemberResponseLiveData
    }

    /**
     * =============================================================================================
     * 닉네임 중복체크
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
     * 로그인
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