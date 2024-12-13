package com.syncrown.arpang.ui.component.home.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.db.push_db.PushMessageDatabase
import com.syncrown.arpang.db.search_db.SearchWordDao
import com.syncrown.arpang.db.search_db.SearchWordDatabase
import com.syncrown.arpang.db.search_db.SearchWordEntity
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestResultMatchDto
import com.syncrown.arpang.network.model.RequestSearchingMatchDto
import com.syncrown.arpang.network.model.ResponsePopularTagDto
import com.syncrown.arpang.network.model.ResponseResultMatchDto
import com.syncrown.arpang.network.model.ResponseSearchingMatchDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val arPangRepository: ArPangRepository = ArPangRepository()
    private val searchWordDao: SearchWordDao

    init {
        val db = SearchWordDatabase.getDatabase(application)
        searchWordDao = db.searchWordDao()
    }

    /**
     * =============================================================================================
     * 저장된 최근검색어 가져오기
     * =============================================================================================
     */
    fun getWords(): LiveData<List<SearchWordEntity>> {
        val result = MutableLiveData<List<SearchWordEntity>>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(searchWordDao.getWordsEntities())
        }
        return result
    }

    /**
     * =============================================================================================
     * 저장된 모든 검색어 지우기
     * =============================================================================================
     */
    fun getDeleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            searchWordDao.deleteAllWords()
        }
    }

    /**
     * =============================================================================================
     * 저장된 최근검색어 전체갯수
     * =============================================================================================
     */
    fun getWordCount(): Int {
        return 1
    }

    /**
     * =============================================================================================
     * 인기 태그 - 검색전
     * =============================================================================================
     */
    private val popularTagResponseLiveData: LiveData<NetworkResult<ResponsePopularTagDto>> =
        arPangRepository.popularTagSearchLiveDataRepository

    fun setPopularTag() {
        viewModelScope.launch {
            arPangRepository.requestPopularTag()
        }
    }

    fun popularTagResponseLiveData(): LiveData<NetworkResult<ResponsePopularTagDto>> {
        return popularTagResponseLiveData
    }

    /**
     * =============================================================================================
     * 입력된 단어가 포함된 이름/공유/보관함 태그 리스트 - 검색중
     * =============================================================================================
     */
    private val inputMatchResponseLiveData: LiveData<NetworkResult<ResponseSearchingMatchDto>> =
        arPangRepository.searchingMatchListLiveDataRepository

    fun inputMatchList(requestSearchingMatchDto: RequestSearchingMatchDto) {
        viewModelScope.launch {
            arPangRepository.requestSearchingInputMatch(requestSearchingMatchDto)
        }
    }

    fun inputMatchResponseLiveData(): LiveData<NetworkResult<ResponseSearchingMatchDto>> {
        return inputMatchResponseLiveData
    }

    /**
     * =============================================================================================
     * 입력된 단어가 포함된 이름/공유/보관함 태그 리스트 - 검색결과
     * =============================================================================================
     */
    private val resultMatchResponseLiveData: LiveData<NetworkResult<ResponseResultMatchDto>> =
        arPangRepository.resultMatchingLiveDataRepository

    fun resultMatchList(requestResultMatchDto: RequestResultMatchDto) {
        viewModelScope.launch {
            arPangRepository.requestResultMatch(requestResultMatchDto)
        }
    }

    fun resultMatchResponseLiveData(): LiveData<NetworkResult<ResponseResultMatchDto>> {
        return resultMatchResponseLiveData
    }
}