package com.vk.usersapp.startup

import android.os.SystemClock
import android.util.Log
import com.vk.usersapp.waterbase.WaterbaseTracker

internal object StartTimeTracker {
    private var startTime: Long = 0
    private var isFirstStart = true

    fun markStartTime() {
        if (isFirstStart) {
            startTime = SystemClock.elapsedRealtime()
            isFirstStart = false
        }
    }

    fun trackStartupTime(sdkInitTime: Long) {
        if (startTime > 0) {
            val totalStartupTime = SystemClock.elapsedRealtime() - startTime
            val startupTimeWithoutSdk = totalStartupTime - sdkInitTime

            WaterbaseTracker.trackAppStart(totalStartupTime)

            val metrics = mapOf(
                "Полное время загрузки" to totalStartupTime,
                "Загрузка SDK библиотеки" to sdkInitTime,
                "Загрузка бз SDK" to startupTimeWithoutSdk
            )
            Log.d("StartTimeTracker", "Аналитика загрузки: $metrics")
        }
    }
}