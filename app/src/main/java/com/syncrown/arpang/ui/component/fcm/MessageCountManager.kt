package com.syncrown.arpang.ui.component.fcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object MessageCountManager {
    private val _unreadMessageCount = MutableLiveData(0)
    val unreadMessageCount: LiveData<Int> get() = _unreadMessageCount

    fun incrementCount() {
        _unreadMessageCount.postValue((_unreadMessageCount.value ?: 0) + 1)
    }

    fun resetCount() {
        _unreadMessageCount.postValue(0)
    }
}