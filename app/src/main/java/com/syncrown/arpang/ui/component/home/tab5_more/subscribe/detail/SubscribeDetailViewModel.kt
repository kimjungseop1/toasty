package com.syncrown.arpang.ui.component.home.tab5_more.subscribe.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestSubscribeUpdateDto
import com.syncrown.arpang.network.model.RequestSubscribeUserContentListDto
import com.syncrown.arpang.network.model.ResponseSubscribeUpdateDto
import com.syncrown.arpang.network.model.ResponseSubscribeUserContentListDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SubscribeDetailViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /**
     * =============================================================================================
     * 구독한 사용자의 공유(공개)한 컨텐츠 리스트
     * =============================================================================================
     */
    private val subscribeUserContentListResponseLiveData: LiveData<NetworkResult<ResponseSubscribeUserContentListDto>> =
        arPangRepository.subscribeUserContentListLiveDataRepository

    fun getSubscribeUserContentList(requestSubscribeUserContentListDto: RequestSubscribeUserContentListDto) {
        viewModelScope.launch {
            arPangRepository.requestSubscribeUserContentList(requestSubscribeUserContentListDto)
        }
    }

    fun subscribeUserContentListResponseLiveData(): LiveData<NetworkResult<ResponseSubscribeUserContentListDto>> {
        return subscribeUserContentListResponseLiveData
    }

    /**
     * =============================================================================================
     * 구독 등록/해제
     * =============================================================================================
     */
    private val subscribeUpdateResponseLiveData: LiveData<NetworkResult<ResponseSubscribeUpdateDto>> =
        arPangRepository.subscribeUpdateLiveDataRepository

    fun subscribeUpdate(requestSubscribeUpdateDto: RequestSubscribeUpdateDto) {
        viewModelScope.launch {
            arPangRepository.requestSubscribeUpdate(requestSubscribeUpdateDto)
        }
    }

    fun subscribeUpdateResponseLiveData(): LiveData<NetworkResult<ResponseSubscribeUpdateDto>> {
        return subscribeUpdateResponseLiveData
    }
}