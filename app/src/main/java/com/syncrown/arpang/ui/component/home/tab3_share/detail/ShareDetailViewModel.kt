package com.syncrown.arpang.ui.component.home.tab3_share.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAddCommentDto
import com.syncrown.arpang.network.model.RequestCommentListDto
import com.syncrown.arpang.network.model.RequestCommentReportDto
import com.syncrown.arpang.network.model.RequestContentReportDto
import com.syncrown.arpang.network.model.RequestDelCommentDto
import com.syncrown.arpang.network.model.RequestDetailContentHashTagDto
import com.syncrown.arpang.network.model.RequestFavoriteDto
import com.syncrown.arpang.network.model.RequestScrapUpdateDto
import com.syncrown.arpang.network.model.RequestShareDetailDto
import com.syncrown.arpang.network.model.ResponseAddCommentDto
import com.syncrown.arpang.network.model.ResponseCommentListDto
import com.syncrown.arpang.network.model.ResponseCommentReportDto
import com.syncrown.arpang.network.model.ResponseContentReportDto
import com.syncrown.arpang.network.model.ResponseDelCommentDto
import com.syncrown.arpang.network.model.ResponseDetailContentHashTagDto
import com.syncrown.arpang.network.model.ResponseFavoriteDto
import com.syncrown.arpang.network.model.ResponseScrapUpdateDto
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

    /***********************************************************************************************
     * 좋아요 등록 / 삭제
     **********************************************************************************************/
    private val favoriteUpdateLiveData: LiveData<NetworkResult<ResponseFavoriteDto>> =
        arPangRepository.favoriteUpdateLiveDataRepository

    fun favoriteUpdate(requestFavoriteDto: RequestFavoriteDto) {
        viewModelScope.launch {
            arPangRepository.requestFavoriteUpdate(requestFavoriteDto)
        }
    }

    fun favoriteUpdateResponseLiveData(): LiveData<NetworkResult<ResponseFavoriteDto>> {
        return favoriteUpdateLiveData
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
     * 게시글 신고하기
     **********************************************************************************************/
    private val contentReportResponseLiveData: LiveData<NetworkResult<ResponseContentReportDto>> =
        arPangRepository.contentReportLiveDataRepository

    fun reportContents(requestContentReportDto: RequestContentReportDto) {
        viewModelScope.launch {
            arPangRepository.requestContentReport(requestContentReportDto)
        }
    }

    fun reportContentsResponseLiveData(): LiveData<NetworkResult<ResponseContentReportDto>> {
        return contentReportResponseLiveData
    }

    /***********************************************************************************************
     * 스크랩
     **********************************************************************************************/
    private val contentScrapResponseLiveData: LiveData<NetworkResult<ResponseScrapUpdateDto>> =
        arPangRepository.scrapUpdateLiveDataRepository

    fun scrapContents(requestScrapUpdateDto: RequestScrapUpdateDto) {
        viewModelScope.launch {
            arPangRepository.requestScrapUpdate(requestScrapUpdateDto)
        }
    }

    fun scrapContentsResponseLiveData(): LiveData<NetworkResult<ResponseScrapUpdateDto>> {
        return contentScrapResponseLiveData
    }
}