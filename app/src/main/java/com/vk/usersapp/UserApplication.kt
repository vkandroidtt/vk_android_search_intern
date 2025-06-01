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

    override fun onCreate() {
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