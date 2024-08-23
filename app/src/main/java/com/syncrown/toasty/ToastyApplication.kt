package com.syncrown.toasty

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class ToastyApplication : Application() {
    private var instance: ToastyApplication? = null

    fun getGlobalApplication(): ToastyApplication? {
        checkNotNull(instance) { "instance is null" }
        return instance
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}