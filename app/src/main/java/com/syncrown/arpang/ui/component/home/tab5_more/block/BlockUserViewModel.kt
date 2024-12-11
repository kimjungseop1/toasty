package com.syncrown.arpang.ui.component.home.tab5_more.block

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestBlockUserListDto
import com.syncrown.arpang.network.model.RequestTargetUserBlockDto
import com.syncrown.arpang.network.model.ResponseBlockUserListDto
import com.syncrown.arpang.network.model.ResponseTargetUserBlockDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class BlockUserViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /**
     * =============================================================================================
     * 내가 차단한 사용자 목록
     * =============================================================================================
     */
    private val blockUserListResponseLiveData: LiveData<NetworkResult<ResponseBlockUserListDto>> =
        arPangRepository.blockUserListLiveDataRepository

    fun blockUserList(requestBlockUserListDto: RequestBlockUserListDto) {
        viewModelScope.launch {
            arPangRepository.requestBlockUserList(requestBlockUserListDto)
        }
    }

    fun blockUserListResponseLiveData(): LiveData<NetworkResult<ResponseBlockUserListDto>> {
        return blockUserListResponseLiveData
    }

    /**
     * =============================================================================================
     * 차단 등록/해제
     * =============================================================================================
     */
    private val targetUserBlockResponseLiveData: LiveData<NetworkResult<ResponseTargetUserBlockDto>> =
        arPangRepository.targetUserBlockUpdateLiveDataRepository

    fun userBlock(requestTargetUserBlockDto: RequestTargetUserBlockDto) {
        viewModelScope.launch {
            arPangRepository.requestTargetUserBlock(requestTargetUserBlockDto)
        }
    }

    fun targetUserBlockResponseLiveData(): LiveData<NetworkResult<ResponseTargetUserBlockDto>> {
        return targetUserBlockResponseLiveData
    }
}