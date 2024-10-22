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
    private val noticeListResponseLiveData: LiveData<NetworkResult<ResponseNoticeListDto>> =
        arPangRepository.noticeListLiveDataRepository

    fun getNoticeList(langCode: String) {
        viewModelScope.launch {
            val requestNoticeList = RequestNoticeListDto()
            requestNoticeList.apply {
                lang_code = langCode
            }

            arPangRepository.requestNoticeList(requestNoticeList)
        }
    }

    fun noticeListResponseLiveData() : LiveData<NetworkResult<ResponseNoticeListDto>> {
        return noticeListResponseLiveData
    }
}