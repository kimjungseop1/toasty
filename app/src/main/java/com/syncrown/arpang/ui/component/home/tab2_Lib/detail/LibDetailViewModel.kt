package com.syncrown.arpang.ui.component.home.tab2_Lib.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestStorageDetailDto
import com.syncrown.arpang.network.model.ResponseStorageDetailDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LibDetailViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 보관함 컨텐츠 상세조회
     **********************************************************************************************/
    private val libContentDetailResponseLiveData: LiveData<NetworkResult<ResponseStorageDetailDto>> =
        arPangRepository.storageContentDetailDataRepository

    fun libContentDetail(requestStorageDetailDto: RequestStorageDetailDto) {
        viewModelScope.launch {
            arPangRepository.requestStorageContentDetail(requestStorageDetailDto)
        }
    }

    fun libContentDetailResponseLiveData(): LiveData<NetworkResult<ResponseStorageDetailDto>> {
        return libContentDetailResponseLiveData
    }
}