package com.syncrown.arpang.ui.component.home.tab5_more.scrap

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestScrapContentDto
import com.syncrown.arpang.network.model.ResponseScrapContentDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ScrapViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 스크랩 컨텐츠 리스트
     **********************************************************************************************/
    private val scrapListResponseLiveData: LiveData<NetworkResult<ResponseScrapContentDto>> =
        arPangRepository.scrapContentListLiveDataRepository

    fun scrapContentList(requestScrapContentDto: RequestScrapContentDto) {
        viewModelScope.launch {
            arPangRepository.requestScrapContentList(requestScrapContentDto)
        }
    }

    fun scrapContentListResponseLiveData(): LiveData<NetworkResult<ResponseScrapContentDto>> {
        return scrapListResponseLiveData
    }
}