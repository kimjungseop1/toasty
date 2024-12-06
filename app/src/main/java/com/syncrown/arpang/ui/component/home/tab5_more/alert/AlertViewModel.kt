package com.syncrown.arpang.ui.component.home.tab5_more.alert

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.db.push_db.PushMessageDao
import com.syncrown.arpang.db.push_db.PushMessageDatabase
import com.syncrown.arpang.db.push_db.PushMessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel(application: Application) : AndroidViewModel(application) {
    private val pushMessageDao: PushMessageDao

    init {
        val db = PushMessageDatabase.getDatabase(application)
        pushMessageDao = db.pushMessageDao()
    }

    /**
     * =============================================================================================
     * 오늘의 메시지만 가져오는 리스트
     * =============================================================================================
     */
    fun getTodayMessages(): LiveData<List<PushMessageEntity>> {
        val result = MutableLiveData<List<PushMessageEntity>>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(pushMessageDao.getMessageTodayEntities())
        }
        return result
    }

    /**
     * =============================================================================================
     * 일주일 이내의 메시지를 가져오는 리스트
     * =============================================================================================
     */
    fun getWeekMessages(): LiveData<List<PushMessageEntity>> {
        val result = MutableLiveData<List<PushMessageEntity>>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(pushMessageDao.getMessageUntilWeekEntities())
        }
        return result
    }

    /**
     * =============================================================================================
     * 30일이 넘는 데이터를 삭제
     * =============================================================================================
     */
    fun deleteOldMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            pushMessageDao.deleteOldMessages()
        }
    }
}