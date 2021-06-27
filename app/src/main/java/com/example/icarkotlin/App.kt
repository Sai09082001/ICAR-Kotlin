package com.example.icarkotlin

import android.app.Application


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        storage = Storage()
        instance = this
    }

    fun getStorage(): Storage {
        return storage!!
    }

    companion object {
        const val DEVICE_TOKEN: String = "DEVICE_TOKEN"
        var storage: Storage? = null
        private lateinit var instance: App
        fun getInstance(): App {
            return instance
        }
    }
}