package com.example.icarkotlin.view.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.R
import com.example.icarkotlin.databinding.FrgM000SplashBinding
import com.example.icarkotlin.view.viewmodel.BaseViewModel
import com.example.icarkotlin.view.viewmodel.M000SplashViewModel



class M000SplashFrg : BaseFragment<FrgM000SplashBinding, M000SplashViewModel>() {
    companion object {
        val TAG = M000SplashFrg::class.java.name
    }

    override fun initViews() {
        Handler(Looper.getMainLooper()).postDelayed({
            gotoLoginScreen()
        }, 2000)
    }

    private fun gotoLoginScreen() {
        val token = CommonUtils.getInstance().getPref(BaseViewModel.TOKEN)
        val phone = CommonUtils.getInstance().getPref(BaseViewModel.PHONE)
        if (token == null || token.isEmpty() || phone == null) {
            callBack?.showFrg(TAG, M002LoginFrg.TAG, false)
        } else {
            callBack?.showFrg(TAG, M003MenuFrg.TAG, false)
        }
    }

    override fun initBinding(mRootView: View): FrgM000SplashBinding? {
        return null
    }

    override fun getViewModelClass(): Class<M000SplashViewModel> {
        return M000SplashViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m000_splash
    }
}
