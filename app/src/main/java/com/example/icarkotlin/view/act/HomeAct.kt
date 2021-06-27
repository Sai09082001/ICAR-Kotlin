package com.example.icarkotlin.view.act

import android.view.View
import com.example.icarkotlin.R
import com.example.icarkotlin.databinding.ActHomeBinding
import com.example.icarkotlin.view.MapManager
import com.example.icarkotlin.view.fragment.M000SplashFrg


class HomeAct : BaseActivity<ActHomeBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.act_home
    }

    override fun initBinding(rootView: View): ActHomeBinding {
        return ActHomeBinding.bind(rootView)
    }

    override fun initViews() {
        MapManager.getInstance().mContext = this
        showFrg(TAG, M000SplashFrg.TAG, false)
    }

    companion object {
        private val TAG = HomeAct::class.java.name
    }
}