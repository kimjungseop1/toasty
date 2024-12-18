package com.syncrown.arpang.ui.component.home.tab1_home.main.use

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestMainBannerDto
import com.syncrown.arpang.network.model.ResponseMainBannerDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CaseViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * AR 이렇게활용
     **********************************************************************************************/
    private val arBannerResponseLiveData: LiveData<NetworkResult<ResponseMainBannerDto>> =
        arPangRepository.mainBannerLiveDataRepository

    fun arBanner(requestMainBannerDto: RequestMainBannerDto) {
        viewModelScope.launch {
            arPangRepository.requestMainBanner(requestMainBannerDto)
        }
    }

    fun arBannerResponseLiveData(): LiveData<NetworkResult<ResponseMainBannerDto>> {
        return arBannerResponseLiveData
    }

    /***********************************************************************************************
     * 인생두컷 이렇게활용
     **********************************************************************************************/
    private val twoCutBannerResponseLiveData: LiveData<NetworkResult<ResponseMainBannerDto>> =
        arPangRepository.mainBannerLiveDataRepository

    fun twoCutBanner(requestMainBannerDto: RequestMainBannerDto) {
        viewModelScope.launch {
            arPangRepository.requestMainBanner(requestMainBannerDto)
        }
    }

    fun twoCutBannerResponseLiveData(): LiveData<NetworkResult<ResponseMainBannerDto>> {
        return twoCutBannerResponseLiveData
    }

    /***********************************************************************************************
     * 라벨스티커 이렇게활용
     **********************************************************************************************/
    private val stickerBannerResponseLiveData: LiveData<NetworkResult<ResponseMainBannerDto>> =
        arPangRepository.mainBannerLiveDataRepository

    fun stickerBanner(requestMainBannerDto: RequestMainBannerDto) {
        viewModelScope.launch {
            arPangRepository.requestMainBanner(requestMainBannerDto)
        }
    }

    fun stickerBannerResponseLiveData(): LiveData<NetworkResult<ResponseMainBannerDto>> {
        return stickerBannerResponseLiveData
    }
}