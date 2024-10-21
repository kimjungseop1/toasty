package com.syncrown.arpang.ui.component.join.consent

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class JoinConsentViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    private val joinMemberResponseLiveData: LiveData<NetworkResult<ResponseJoinDto>> =
        arPangRepository.joinLiveDataRepository


    /**
     * =============================================================================================
     * 가입신청
     * =============================================================================================
     */
    fun joinMember(requestJoinDto: RequestJoinDto) {
        viewModelScope.launch {
            arPangRepository.requestJoin(requestJoinDto)
        }
    }

    fun joinMemberResponseLiveData(): LiveData<NetworkResult<ResponseJoinDto>> {
        return joinMemberResponseLiveData
    }
}