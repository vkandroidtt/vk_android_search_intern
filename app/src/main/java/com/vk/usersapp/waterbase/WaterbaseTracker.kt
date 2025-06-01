package com.vk.usersapp.waterbase

import android.util.Log

object WaterbaseTracker {
    private const val TAG = "WaterbaseTracker"
    fun trackAppStart(time: Long) {
        Log.d(TAG, "Замер холодного старта приложения: $time ms")
    }
}