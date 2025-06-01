package com.vk.usersapp

import android.app.Application
import android.os.SystemClock
import com.example.mylib.MyLibSDK
import com.vk.usersapp.startup.StartTimeTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserApplication : Application() {
    private val sdk: MyLibSDK by lazy { MyLibSDK() }
    companion object {
        private lateinit var instance: UserApplication

        fun getInstance(): UserApplication = instance
    }
    override fun onCreate() {
        instance = this
        val beforeSdkInit = SystemClock.elapsedRealtime()

        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            sdk.onAppCreated()
            val afterSdkInit = SystemClock.elapsedRealtime()
            val sdkInitTime = afterSdkInit - beforeSdkInit
            StartTimeTracker.trackStartupTime(sdkInitTime)
        }
    }
}