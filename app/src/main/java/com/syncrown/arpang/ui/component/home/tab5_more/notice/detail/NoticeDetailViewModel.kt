package com.syncrown.arpang.ui.component.home.tab5_more.notice.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestNoticeDetailDto
import com.syncrown.arpang.network.model.ResponseNoticeDetailDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NoticeDetailViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 공지사항 상세보기
     **********************************************************************************************/
    private val noticeDetailResponseLiveData: LiveData<NetworkResult<ResponseNoticeDetailDto>> =
        arPangRepository.noticeDetailLiveDataRepository

    fun noticeListDetail(requestNoticeDetailDto: RequestNoticeDetailDto) {
        viewModelScope.launch {
            arPangRepository.requestNoticeDetail(requestNoticeDetailDto)
        }
    }

    fun noticeDetailResponseLiveData(): LiveData<NetworkResult<ResponseNoticeDetailDto>> {
        return noticeDetailResponseLiveData
    }
}