package com.vk.usersapp

import android.app.Application
import android.util.Log
import com.example.mylib.MyLibSDK
import com.vk.usersapp.waterbase.WaterbaseTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserApplication : Application() {
    private var startTime: Long = System.currentTimeMillis()

    private val sdk: MyLibSDK by lazy { MyLibSDK() }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Default).launch {
            sdk.onAppCreated()

            logAppStartTime()
        }
    }

    private fun logAppStartTime() {
        val duration = System.currentTimeMillis() - startTime
        WaterbaseTracker.trackAppStart(duration)
        Log.d("UserApplication", "App start duration: $duration ms")
    }
}