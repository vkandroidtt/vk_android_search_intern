package com.vk.usersapp

import android.app.Application
import com.example.mylib.MyLibSDK

class UserApplication : Application() {

    private var sdk: MyLibSDK = MyLibSDK()

    override fun onCreate() {
        super.onCreate()
        sdk.onAppCreated()
    }
}