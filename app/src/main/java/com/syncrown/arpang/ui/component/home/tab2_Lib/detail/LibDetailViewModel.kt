package com.syncrown.arpang.ui.component.home.tab2_Lib.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAddCommentDto
import com.syncrown.arpang.network.model.RequestCommentListDto
import com.syncrown.arpang.network.model.RequestCommentReportDto
import com.syncrown.arpang.network.model.RequestDelCommentDto
import com.syncrown.arpang.network.model.RequestDeleteStorageDto
import com.syncrown.arpang.network.model.RequestDetailContentHashTagDto
import com.syncrown.arpang.network.model.RequestEditContentHashTagDto
import com.syncrown.arpang.network.model.RequestPublicContentSettingDto
import com.syncrown.arpang.network.model.RequestStorageDetailDto
import com.syncrown.arpang.network.model.ResponseAddCommentDto
import com.syncrown.arpang.network.model.ResponseCommentListDto
import com.syncrown.arpang.network.model.ResponseCommentReportDto
import com.syncrown.arpang.network.model.ResponseDelCommentDto
import com.syncrown.arpang.network.model.ResponseDeleteStorageDto
import com.syncrown.arpang.network.model.ResponseDetailContentHashTagDto
import com.syncrown.arpang.network.model.ResponseEditContentHashTagDto
import com.syncrown.arpang.network.model.ResponsePublicContentSettingDto
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
     * 상세페이지 해시태그 리스트
     **********************************************************************************************/
    private val hashTagListResponseLiveData: LiveData<NetworkResult<ResponseDetailContentHashTagDto>> =
        arPangRepository.detailContentHashListRepository

    fun getHashTagList(requestDetailContentHashTagDto: RequestDetailContentHashTagDto) {
        viewModelScope.launch {
            arPangRepository.requestDetailHashTagList(requestDetailContentHashTagDto)
        }
    }

    fun hashTagListResponseLiveData(): LiveData<NetworkResult<ResponseDetailContentHashTagDto>> {
        return hashTagListResponseLiveData
    }

    /***********************************************************************************************
     * 상세페이지 해시태그 리스트 편집
     **********************************************************************************************/
    private val editHashTagResponseLiveData: LiveData<NetworkResult<ResponseEditContentHashTagDto>> =
        arPangRepository.editContentHashTagLiveDataRepository

    fun updateHashTag(requestEditContentHashTagDto: RequestEditContentHashTagDto) {
        viewModelScope.launch {
            arPangRepository.requestEditContentHashTag(requestEditContentHashTagDto)
        }
    }

    fun editHashTagResponseLiveData(): LiveData<NetworkResult<ResponseEditContentHashTagDto>> {
        return editHashTagResponseLiveData
    }

    /***********************************************************************************************
     * 컨텐츠 공개 여부 설정하기
     **********************************************************************************************/
    private val publicContentSettingLiveData: LiveData<NetworkResult<ResponsePublicContentSettingDto>> =
        arPangRepository.publicContentSettingLiveDataRepository

    fun setPublicContentSetting(requestPublicContentSettingDto: RequestPublicContentSettingDto) {
        viewModelScope.launch {
            arPangRepository.requestPublicContentSetting(requestPublicContentSettingDto)
        }
    }

    fun publicContentSettingLiveData(): LiveData<NetworkResult<ResponsePublicContentSettingDto>> {
        return publicContentSettingLiveData
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

    /***********************************************************************************************
     * 댓글 신고
     **********************************************************************************************/
    private val reportCommentResponseLiveData: LiveData<NetworkResult<ResponseCommentReportDto>> =
        arPangRepository.commentReportLiveDataRepository

    fun reportComment(requestCommentReportDto: RequestCommentReportDto) {
        viewModelScope.launch {
            arPangRepository.requestCommentReport(requestCommentReportDto)
        }
    }

    fun reportCommentResponseLiveData(): LiveData<NetworkResult<ResponseCommentReportDto>> {
        return reportCommentResponseLiveData
    }

    /***********************************************************************************************
     * 컨텐츠 삭제
     **********************************************************************************************/
    private val deleteContentLiveData: LiveData<NetworkResult<ResponseDeleteStorageDto>> =
        arPangRepository.storageContentDeleteLiveDataRepository

    fun deleteLibContent(requestDeleteStorageDto: RequestDeleteStorageDto) {
        viewModelScope.launch {
            arPangRepository.requestDeleteStorageContent(requestDeleteStorageDto)
        }
    }

    fun deleteLibContentResponseLiveData(): LiveData<NetworkResult<ResponseDeleteStorageDto>> {
        return deleteContentLiveData
    }
}