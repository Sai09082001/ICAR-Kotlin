package com.example.icarkotlin.view.fragment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.Gravity
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.R
import com.example.icarkotlin.Storage
import com.example.icarkotlin.databinding.FrgM003MenuMainBinding
import com.example.icarkotlin.view.MapManager
import com.example.icarkotlin.view.api.model.CarInfoModelRes
import com.example.icarkotlin.view.viewmodel.BaseViewModel
import com.example.icarkotlin.view.viewmodel.M003MenuViewModel
import com.google.android.gms.maps.SupportMapFragment


class M003MenuFrg : BaseFragment<FrgM003MenuMainBinding, M003MenuViewModel>() {
    companion object {
        val TAG: String = M003MenuFrg::class.java.name
    }

    override fun initViews() {
        checkPermission()
        val phone = CommonUtils.getInstance().getPref(BaseViewModel.PHONE)
        val name = CommonUtils.getInstance().getPref(BaseViewModel.USER_NAME)

        binding?.includeMenu?.tvUserName?.text = name ?: "Chưa xác định"
        binding?.includeMenu?.tvPhone?.text = phone
        binding?.drawer?.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                binding?.includeActionbar?.ivMenu?.setImageResource(R.drawable.ic_menu_open_24)
            }

            override fun onDrawerClosed(drawerView: View) {
                binding?.includeActionbar?.ivMenu?.setImageResource(R.drawable.ic_menu_24)
            }
        })

        binding?.includeActionbar?.ivMenu?.setOnClickListener(this)
        binding?.includeActionbar?.ivMyLocation?.setOnClickListener(this)
        binding?.includeActionbar?.ivCarList?.setOnClickListener(this)
        binding?.includeMenu?.tvLogout?.setOnClickListener(this)

        val mapFrg = childFragmentManager.findFragmentById(R.id.frg_map) as SupportMapFragment
        mapFrg.getMapAsync {
            MapManager.getInstance().mMap = it
            MapManager.getInstance().initMap()
        }
    }

    override fun callBack(key: String, data: Any?) {
        super.callBack(key, data)
        if (key == M003MenuViewModel.API_KEY_GET_LIST_CAR) {
            showListCarOnMap(data)
        }
    }

    private fun showListCarOnMap(data: Any?) {
        if (data == null) return
        MapManager.getInstance().showListCar(data as CarInfoModelRes)
    }

    override fun doClickView(v: View?) {
        when (v?.id) {
            R.id.iv_menu -> {
                binding?.drawer?.openDrawer(GravityCompat.START)
            }
            R.id.iv_my_location -> {
                MapManager.getInstance().showMyLocation()
            }
            R.id.iv_car_list -> {
                mViewModel.getListCar()
            }
            R.id.tv_logout -> {
                logout()
            }
        }
    }

    override fun logout() {
        CommonUtils.getInstance().clearPref(BaseViewModel.PHONE)
        CommonUtils.getInstance().clearPref(BaseViewModel.TOKEN)
        CommonUtils.getInstance().clearPref(BaseViewModel.USER_NAME)
        Storage.getInstance().clearAll()
        MapManager.getInstance().stopHandleLocation()
        callBack?.showFrg(TAG, M002LoginFrg.TAG, false)
    }
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        for (rs in grantResults) {
            if (rs != PackageManager.PERMISSION_GRANTED) {
                showNotify("Hãy cho phép quyền truy cập vị trí để sử dụng ứng dụng")
            }
        }
    }

    override fun initBinding(mRootView: View): FrgM003MenuMainBinding {
        return FrgM003MenuMainBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M003MenuViewModel> {
        return M003MenuViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m003_menu_main
    }
}
