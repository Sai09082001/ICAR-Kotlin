package com.example.icarkotlin.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.icarkotlin.R
import com.example.icarkotlin.Storage
import com.example.icarkotlin.view.api.model.CarInfoModelRes
import com.example.icarkotlin.view.api.model.entities.CarInfoEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapManager : LocationCallback() {
    var mMap: GoogleMap? = null
    lateinit var mContext: Context
    private var myLocation: Marker? = null
    private lateinit var fusedLPC: FusedLocationProviderClient
    private val listCarMarker = ArrayList<Marker>()

    @SuppressLint("VisibleForTests")
    fun initMap() {
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.setAllGesturesEnabled(true)
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
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        mMap?.setInfoWindowAdapter(initAdapter())
        //update my location
        fusedLPC = FusedLocationProviderClient(mContext)
        val locationReq = LocationRequest.create()
        locationReq.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationReq.interval = 2000
        fusedLPC.requestLocationUpdates(locationReq, this, Looper.getMainLooper())
    }

    private fun initAdapter(): GoogleMap.InfoWindowAdapter {
        return object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(p0: Marker): View? {
                return initCarInfoView(p0)
            }

            override fun getInfoContents(p0: Marker): View? {
                return initCarInfoView(p0)
            }
        }
    }

    private fun initCarInfoView(marker: Marker): View? {
        val carInfo = marker.tag as CarInfoEntity
        val v = View.inflate(mContext, R.layout.item_info, null)
        val tvBrand = v.findViewById<TextView>(R.id.tv_car_name_info)
        val tvNumber = v.findViewById<TextView>(R.id.tv_car_number)
        val tvStatus = v.findViewById<TextView>(R.id.tv_car_status_info)
        val tvSpeed = v.findViewById<TextView>(R.id.tv_car_speed_info)
        val tvAddress = v.findViewById<TextView>(R.id.tv_car_location_info)

        tvBrand.text = carInfo.carBrand
        tvNumber.text = carInfo.carNumber
        tvStatus.text = if (carInfo.activeStatus.equals("offline")) "Dừng đỗ" else "Đang di chuyển"
        tvSpeed.text = carInfo.lastSpeed
        tvAddress.text = carInfo.lastAddress

        return v
    }

    private fun updateMyLocation(rs: LocationResult) {
        if (Storage.getInstance().myPos == null) {
            Storage.getInstance().myPos = rs.locations[0]

            if (mMap == null) return
            showMyLocation()
        } else {
            Storage.getInstance().myPos = rs.locations[0]
        }
        Log.d(TAG, "updateMyLocation: ${Storage.getInstance().myPos}}")
    }

    fun showMyLocation() {
        val pos = LatLng(
            Storage.getInstance().myPos!!.latitude,
            Storage.getInstance().myPos!!.longitude
        )

        if (myLocation == null) {
            val myLocationOp = MarkerOptions()
            myLocationOp.title("Vị trí của tôi")
            myLocationOp.position(pos)
            myLocationOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            myLocation = mMap?.addMarker(myLocationOp)
        } else {
            myLocation?.position = pos
        }
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))
    }

    fun showListCar(carInfoModelRes: CarInfoModelRes) {
        Log.i(TAG, "carInfoModelRes= $carInfoModelRes")
        if (carInfoModelRes.data == null) return
        for (car in listCarMarker) {
            car.remove()
        }

        listCarMarker.clear()
        for ((index, car) in (carInfoModelRes.data!!).withIndex()) {
            showCarOnMap(car, index)
        }
    }

    private fun showCarOnMap(car: CarInfoEntity, index: Int) {
        val op = MarkerOptions()
        op.title(car.carNumber)
        val pos = LatLng((car.lastLat ?: "0").toDouble(), (car.lastLng ?: "0").toDouble())
        op.position(pos)
        op.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64))
        val marker = mMap?.addMarker(op)
        marker?.tag = car
        listCarMarker.add(marker!!)
        if (index == 0) {
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f))
        }
    }

    override fun onLocationResult(rs: LocationResult) {
        updateMyLocation(rs)
    }

    fun stopHandleLocation() {
        fusedLPC.removeLocationUpdates(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: MapManager? = null
        private var TAG = MapManager.javaClass.name

        fun getInstance(): MapManager {
            if (INSTANCE == null) {
                INSTANCE = MapManager()
            }
            return INSTANCE!!
        }
    }
}