package com.syncrown.arpang.ui.component.home.input_tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestAllFavoriteDto
import com.syncrown.arpang.network.model.RequestMyFavoriteDto
import com.syncrown.arpang.network.model.ResponseAllFavoriteDto
import com.syncrown.arpang.network.model.ResponseMyFavoriteDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class InputTagViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 인기 있는 태그
     **********************************************************************************************/
    private val allFavoriteHashTagResponseLiveData: LiveData<NetworkResult<ResponseAllFavoriteDto>> =
        arPangRepository.allFavoriteHashTagLiveDataRepository

    fun allFavoriteHashTag(requestAllFavoriteDto: RequestAllFavoriteDto) {
        viewModelScope.launch {
            arPangRepository.requestAllFavoriteHashTag(requestAllFavoriteDto)
        }
    }

    fun allFavoriteHashTagResponseLiveData(): LiveData<NetworkResult<ResponseAllFavoriteDto>> {
        return allFavoriteHashTagResponseLiveData
    }

    /***********************************************************************************************
     * 내가 사용했던 태그
     **********************************************************************************************/
    private val myFavoriteHashTagResponseLiveData: LiveData<NetworkResult<ResponseMyFavoriteDto>> =
        arPangRepository.myFavoriteHashTagLiveDataRepository

    fun myFavoriteHashTag(requestMyFavoriteDto: RequestMyFavoriteDto) {
        viewModelScope.launch {
            arPangRepository.requestMyFavoriteHashTag(requestMyFavoriteDto)
        }
    }

    fun myFavoriteHashTagResponseLiveData(): LiveData<NetworkResult<ResponseMyFavoriteDto>> {
        return myFavoriteHashTagResponseLiveData
    }
}