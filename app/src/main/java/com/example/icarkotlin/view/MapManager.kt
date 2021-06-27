package com.example.icarkotlin.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap

class MapManager {
    lateinit var mMap: GoogleMap
    lateinit var mContext: Context
    fun initMap() {
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: MapManager? = null
        fun getInstance(): MapManager {
            if (INSTANCE == null) {
                INSTANCE = MapManager()
            }
            return INSTANCE!!
        }
    }
}