package com.syncrown.arpang.ui.component.home.tab3_share.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestShareDetailDto
import com.syncrown.arpang.network.model.ResponseShareDetailDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ShareDetailViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 공유 컨텐츠 상세조회
     **********************************************************************************************/
    private val shareDetailResponseLiveData: LiveData<NetworkResult<ResponseShareDetailDto>> =
        arPangRepository.shareDetailLiveDataRepository

    fun shareContentDetail(requestShareDetailDto: RequestShareDetailDto) {
        viewModelScope.launch {
            arPangRepository.requestShareContentDetail(requestShareDetailDto)
        }
    }

    fun shareContentDetailResponseLiveData(): LiveData<NetworkResult<ResponseShareDetailDto>> {
        return shareDetailResponseLiveData
    }
}