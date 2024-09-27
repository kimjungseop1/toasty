package com.syncrown.toasty.ui.commons

import androidx.lifecycle.MutableLiveData

object ActivityFinishManager {
    val finishActivityEvent = MutableLiveData<Class<*>>()
}