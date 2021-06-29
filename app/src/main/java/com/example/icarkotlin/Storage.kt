package com.example.icarkotlin

import android.annotation.SuppressLint
import android.location.Location

class Storage {
    fun clearAll() {
        myPos = null
    }

    var myPos: Location? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: Storage? = null

        fun getInstance(): Storage {
            if (INSTANCE == null) {
                INSTANCE = Storage()
            }
            return INSTANCE!!
        }
    }
}