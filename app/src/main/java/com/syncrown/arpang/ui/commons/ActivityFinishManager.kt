package com.syncrown.arpang.ui.commons

import androidx.lifecycle.MutableLiveData

object ActivityFinishManager {
    val finishActivityEvent = MutableLiveData<Class<*>>()
}