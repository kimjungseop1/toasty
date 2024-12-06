package com.syncrown.arpang.ui.component.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAppMainDto
import com.syncrown.arpang.network.model.RequestShareContentAllOpenListDto
import com.syncrown.arpang.network.model.RequestStorageContentListDto
import com.syncrown.arpang.network.model.RequestSubscribeTotalDto
import com.syncrown.arpang.network.model.RequestUserProfileDto
import com.syncrown.arpang.network.model.ResponseAppMainDto
import com.syncrown.arpang.network.model.ResponseShareContentAllOpenListDto
import com.syncrown.arpang.network.model.ResponseStorageContentListDto
import com.syncrown.arpang.network.model.ResponseSubscribeTotalDto
import com.syncrown.arpang.network.model.ResponseUserProfileDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 메인
     **********************************************************************************************/
    //TODO 용지연결페이지에서 배너를 눌러 메인의 탭4번으로 바로 이동할떄 체킹
    private val _onPaperGuideActivityClosed: MutableLiveData<String> = MutableLiveData("")
    val onPaperGuideActivityClosed: LiveData<String> get() = _onPaperGuideActivityClosed

    fun paperGuideActivityClosed(newMessage: String) {
        _onPaperGuideActivityClosed.postValue(newMessage)
    }


    /***********************************************************************************************
     * 홈
     **********************************************************************************************/
    //TODO 프린트가 연결되어있는지 여부 (메인액티비티, 홈프레그먼트 둘다 사용)
    private val _connectState = MutableLiveData<Boolean>()
    val connectStateShare: LiveData<Boolean> get() = _connectState

    fun updateConnectedState(newState: Boolean) {
        _connectState.value = newState
    }

    //TODO 메인 앱메뉴
    private val appMainResponseLiveData: LiveData<NetworkResult<ResponseAppMainDto>> =
        arPangRepository.appMainMenuLiveDataRepository

    fun appMain(requestAppMainDto: RequestAppMainDto) {
        viewModelScope.launch {
            arPangRepository.requestAppMain(requestAppMainDto)
        }
    }

    fun appMainResponseLiveData(): LiveData<NetworkResult<ResponseAppMainDto>> {
        return appMainResponseLiveData
    }

    /***********************************************************************************************
     * 보관함
     **********************************************************************************************/
    //TODO 보관함 리스트
    private val storageContentLiveData: MutableLiveData<NetworkResult<ResponseStorageContentListDto>> =
        arPangRepository.storageContentListLiveDataRepository

    fun getStorageList(requestStorageContentListDto: RequestStorageContentListDto) {
        viewModelScope.launch {
            arPangRepository.requestStorageContentList(requestStorageContentListDto)
        }
    }

    fun storageContentResponseLiveData(): MutableLiveData<NetworkResult<ResponseStorageContentListDto>> {
        return storageContentLiveData
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun clearLibListData() {
        storageContentLiveData.value = null
    }

    /***********************************************************************************************
     * 공유공간
     **********************************************************************************************/
    //TODO 전체 공유 컨텐츠 리스트
    private val shareAllContentLiveData: MutableLiveData<NetworkResult<ResponseShareContentAllOpenListDto>> =
        arPangRepository.shareContentAllOpenListLiveDataRepository

    fun getShareAllList(requestShareContentAllOpenListDto: RequestShareContentAllOpenListDto) {
        viewModelScope.launch {
            arPangRepository.requestShareContentAllOpenList(requestShareContentAllOpenListDto)
        }
    }

    fun shareAllContentListResponseLiveData(): MutableLiveData<NetworkResult<ResponseShareContentAllOpenListDto>> {
        return shareAllContentLiveData
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun clearShareListData() {
        shareAllContentLiveData.value = null
    }

    /***********************************************************************************************
     * 더보기
     **********************************************************************************************/
    fun appVersion(context: Context): String? {
        try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(
                context.packageName, 0
            )

            return pInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    private val getUserProfileResponseLiveData: LiveData<NetworkResult<ResponseUserProfileDto>> =
        arPangRepository.userprofileLiveDataRepository

    fun getUserProfile(requestUserProfileDto: RequestUserProfileDto) {
        viewModelScope.launch {
            arPangRepository.requestUserProfile(requestUserProfileDto)
        }
    }

    fun getUserProfileResponseLiveData(): LiveData<NetworkResult<ResponseUserProfileDto>> {
        return getUserProfileResponseLiveData
    }

    //구독자 전체 갯수
    private val subscribeTotalCountResponseLiveData: LiveData<NetworkResult<ResponseSubscribeTotalDto>> =
        arPangRepository.subscribeTotalCntLiveDataRepository

    fun subscribeTotalCount(requestSubscribeTotalDto: RequestSubscribeTotalDto) {
        viewModelScope.launch {
            arPangRepository.requestSubscribeTotalCount(requestSubscribeTotalDto)
        }
    }

    fun subscribeTotalCountResponseLiveData(): LiveData<NetworkResult<ResponseSubscribeTotalDto>> {
        return subscribeTotalCountResponseLiveData
    }
}