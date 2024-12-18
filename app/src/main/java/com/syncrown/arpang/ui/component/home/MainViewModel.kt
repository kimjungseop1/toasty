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
import com.syncrown.arpang.network.model.RequestLibPaperListDto
import com.syncrown.arpang.network.model.RequestMainBannerDto
import com.syncrown.arpang.network.model.RequestShareContentAllOpenListDto
import com.syncrown.arpang.network.model.RequestStorageContentListDto
import com.syncrown.arpang.network.model.RequestSubscribeTotalDto
import com.syncrown.arpang.network.model.RequestUserProfileDto
import com.syncrown.arpang.network.model.ResponseAppMainDto
import com.syncrown.arpang.network.model.ResponseLibPaperListDto
import com.syncrown.arpang.network.model.ResponseMainBannerDto
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

    //TODO 팝업배너
    private val popupBannerResponseLiveData: LiveData<NetworkResult<ResponseMainBannerDto>> =
        arPangRepository.popupBannerLiveDataRepository

    fun popupBanner(requestMainBannerDto: RequestMainBannerDto) {
        viewModelScope.launch {
            arPangRepository.requestPopupBanner(requestMainBannerDto)
        }
    }

    fun popupBannerResponseLiveData(): LiveData<NetworkResult<ResponseMainBannerDto>> {
        return popupBannerResponseLiveData
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

    //TODO 메인베너
    private val homeBannerResponseLiveData: LiveData<NetworkResult<ResponseMainBannerDto>> =
        arPangRepository.mainBannerLiveDataRepository

    fun homeBanner(requestMainBannerDto: RequestMainBannerDto) {
        viewModelScope.launch {
            arPangRepository.requestMainBanner(requestMainBannerDto)
        }
    }

    fun homeBannerResponseLiveData(): LiveData<NetworkResult<ResponseMainBannerDto>> {
        return homeBannerResponseLiveData
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

    //TODO 내 보관함에 사용된 용지 리스트
    private val libPaperListLiveData: MutableLiveData<NetworkResult<ResponseLibPaperListDto>> =
        arPangRepository.libPaperListLiveDataRepository

    fun filterPaperList(requestLibPaperListDto: RequestLibPaperListDto) {
        viewModelScope.launch {
            arPangRepository.requestLibPaperList(requestLibPaperListDto)
        }
    }

    fun filterPaperListResponseLiveData(): MutableLiveData<NetworkResult<ResponseLibPaperListDto>> {
        return libPaperListLiveData
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