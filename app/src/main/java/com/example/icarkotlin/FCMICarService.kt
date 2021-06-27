package com.example.icarkotlin

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMICarService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val rs = remoteMessage.notification?.body
        Log.i(TAG, "onMessageReceived...$rs")
        super.onMessageReceived(remoteMessage)
    }

    companion object{
        val TAG = FCMICarService::class.java.name
    }
}