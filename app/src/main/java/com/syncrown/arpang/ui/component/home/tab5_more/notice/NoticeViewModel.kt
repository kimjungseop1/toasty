package com.syncrown.arpang.ui.component.home.tab5_more.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestNoticeListDto
import com.syncrown.arpang.network.model.ResponseNoticeListDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NoticeViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 공지사항 리스트
     **********************************************************************************************/
    private val noticeListResponseLiveData: LiveData<NetworkResult<ResponseNoticeListDto>> =
        arPangRepository.noticeListLiveDataRepository

    fun noticeList(requestNoticeListDto: RequestNoticeListDto) {
        viewModelScope.launch {
            arPangRepository.requestNoticeList(requestNoticeListDto)
        }
    }

    fun noticeListResponseLiveData(): LiveData<NetworkResult<ResponseNoticeListDto>> {
        return noticeListResponseLiveData
    }
}