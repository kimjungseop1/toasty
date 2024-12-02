package com.syncrown.arpang.ui.component.home.tab1_home.empty_cartridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCartridgeListByTagDto
import com.syncrown.arpang.network.model.RequestRecommendTagListDto
import com.syncrown.arpang.network.model.ResponseCartridgeListByTagDto
import com.syncrown.arpang.network.model.ResponseRecommendTagListDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class EmptyCartridgeViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     *
     **********************************************************************************************/


    /***********************************************************************************************
     * 추천태그별 카트리지 리스트
     **********************************************************************************************/
    private val cartridgeByTagResponseLiveData: LiveData<NetworkResult<ResponseCartridgeListByTagDto>> =
        arPangRepository.cartridgeListByTagsLiveDataRepository

    fun cartridgeByTag(requestCartridgeListByTagDto: RequestCartridgeListByTagDto) {
        viewModelScope.launch {
            arPangRepository.requestCartridgeListByTag(requestCartridgeListByTagDto)
        }
    }

    fun cartridgeByTagListResponseLiveData(): LiveData<NetworkResult<ResponseCartridgeListByTagDto>> {
        return cartridgeByTagResponseLiveData
    }


    /***********************************************************************************************
     * 추천태그 리스트
     **********************************************************************************************/
    private val recommendTagListResponseLiveData: LiveData<NetworkResult<ResponseRecommendTagListDto>> =
        arPangRepository.recommendTagListLiveDataRepository

    fun responseRecommendTagList(requestRecommendTagListDto: RequestRecommendTagListDto) {
        viewModelScope.launch {
            arPangRepository.requestRecommendTagList(requestRecommendTagListDto)
        }
    }

    fun recommendTagListResponseLiveData(): LiveData<NetworkResult<ResponseRecommendTagListDto>> {
        return recommendTagListResponseLiveData
    }
}