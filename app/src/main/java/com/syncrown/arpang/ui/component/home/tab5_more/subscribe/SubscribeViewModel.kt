package com.syncrown.arpang.ui.component.home.tab5_more.subscribe

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestSubscribeByMeDto
import com.syncrown.arpang.network.model.RequestSubscribeByMyDto
import com.syncrown.arpang.network.model.RequestSubscribeReleaseDto
import com.syncrown.arpang.network.model.RequestSubscribeTotalDto
import com.syncrown.arpang.network.model.ResponseSubscribeListDto
import com.syncrown.arpang.network.model.ResponseSubscribeReleaseDto
import com.syncrown.arpang.network.model.ResponseSubscribeTotalDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SubscribeViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 구독자 전체 갯수
     **********************************************************************************************/
    private val subscribeTotalCountResponseLiveData: LiveData<NetworkResult<ResponseSubscribeTotalDto>> =
        arPangRepository.subscribeTotalCntLiveDataRepository

    fun subscribeTotalCount(requestSubscribeTotalDto: RequestSubscribeTotalDto) {
        viewModelScope.launch {
            arPangRepository.requestSubscribeTotalCount(requestSubscribeTotalDto)
        }
    }

    fun subscribeTotalCountResponseLiveData(): LiveData<NetworkResult<ResponseSubscribeTotalDto>> {
        return subscribeTotalCountResponseLiveData
    }

    /***********************************************************************************************
     * 내가 구독한 사용자 목록
     **********************************************************************************************/
    private val subscribeByMyResponseLiveData: LiveData<NetworkResult<ResponseSubscribeListDto>> =
        arPangRepository.subscribeListByMyLiveDataRepository

    fun subscribeByMyList(requestSubscribeByMyDto: RequestSubscribeByMyDto) {
        viewModelScope.launch {
            arPangRepository.requestSubscribeListByMy(requestSubscribeByMyDto)
        }
    }

    fun subscribeByMyResponseLiveData(): LiveData<NetworkResult<ResponseSubscribeListDto>> {
        return subscribeByMyResponseLiveData
    }

    /***********************************************************************************************
     * 나를 구독한 사용자 목록
     **********************************************************************************************/
    private val subscribeByMeResponseLiveData: LiveData<NetworkResult<ResponseSubscribeListDto>> =
        arPangRepository.subscribeListByMeLiveDataRepository

    fun subscribeByMeList(requestSubscribeByMeDto: RequestSubscribeByMeDto) {
        viewModelScope.launch {
            arPangRepository.requestSubscribeListByMe(requestSubscribeByMeDto)
        }
    }

    fun subscribeByMeResponseLiveData(): LiveData<NetworkResult<ResponseSubscribeListDto>> {
        return subscribeByMeResponseLiveData
    }

    /***********************************************************************************************
     * 구독해제
     **********************************************************************************************/
    private val subscribeReleaseResponseLiveData: LiveData<NetworkResult<ResponseSubscribeReleaseDto>> =
        arPangRepository.subscribeReleaseLiveDataRepository

    fun subscribeRelease(requestSubscribeReleaseDto: RequestSubscribeReleaseDto) {
        viewModelScope.launch {
            arPangRepository.requestSubscribeRelease(requestSubscribeReleaseDto)
        }
    }

    fun subscribeReleaseResponseLiveData(): LiveData<NetworkResult<ResponseSubscribeReleaseDto>> {
        return subscribeReleaseResponseLiveData
    }
}