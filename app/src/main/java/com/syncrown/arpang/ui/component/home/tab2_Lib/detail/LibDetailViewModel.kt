package com.syncrown.arpang.ui.component.home.tab2_Lib.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAddCommentDto
import com.syncrown.arpang.network.model.RequestCommentListDto
import com.syncrown.arpang.network.model.RequestDelCommentDto
import com.syncrown.arpang.network.model.RequestStorageDetailDto
import com.syncrown.arpang.network.model.ResponseAddCommentDto
import com.syncrown.arpang.network.model.ResponseCommentListDto
import com.syncrown.arpang.network.model.ResponseDelCommentDto
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

    /***********************************************************************************************
     * 댓글 리스트
     **********************************************************************************************/
    private val commentListResponseLiveData: LiveData<NetworkResult<ResponseCommentListDto>> =
        arPangRepository.commentListLiveDataRepository

    fun commentList(requestCommentListDto: RequestCommentListDto) {
        viewModelScope.launch {
            arPangRepository.requestCommentList(requestCommentListDto)
        }
    }

    fun commentListResponseLiveData(): LiveData<NetworkResult<ResponseCommentListDto>> {
        return commentListResponseLiveData
    }

    /***********************************************************************************************
     * 댓글 작성
     **********************************************************************************************/
    private val addCommentResponseLiveData: LiveData<NetworkResult<ResponseAddCommentDto>> =
        arPangRepository.addCommentLiveDataRepository

    fun addComment(requestAddCommentDto: RequestAddCommentDto) {
        viewModelScope.launch {
            arPangRepository.requestAddComment(requestAddCommentDto)
        }
    }

    fun addCommentResponseLiveData(): LiveData<NetworkResult<ResponseAddCommentDto>> {
        return addCommentResponseLiveData
    }

    /***********************************************************************************************
     * 댓글 삭제
     **********************************************************************************************/
    private val delCommentResponseLiveData: LiveData<NetworkResult<ResponseDelCommentDto>> =
        arPangRepository.delCommentLiveDataRepository

    fun deleteComment(requestDelCommentDto: RequestDelCommentDto) {
        viewModelScope.launch {
            arPangRepository.requestDelComment(requestDelCommentDto)
        }
    }

    fun deleteCommentResponseLiveData(): LiveData<NetworkResult<ResponseDelCommentDto>> {
        return delCommentResponseLiveData
    }
}