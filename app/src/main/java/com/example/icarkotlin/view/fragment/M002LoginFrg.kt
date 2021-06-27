package com.example.icarkotlin.view.fragment

import android.view.View
import android.widget.Toast
import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.R
import com.example.icarkotlin.databinding.FrgM002LoginBinding
import com.example.icarkotlin.view.ProgressLoading
import com.example.icarkotlin.view.api.model.UserInfoModelRes
import com.example.icarkotlin.view.viewmodel.BaseViewModel
import com.example.icarkotlin.view.viewmodel.M002LoginViewModel



class M002LoginFrg : BaseFragment<FrgM002LoginBinding, M002LoginViewModel>() {
    override fun initViews() {
        binding?.tvCreateAcc?.setOnClickListener {
            callBack?.showFrg(TAG, M001RegisterFrg.TAG, true)
        }

        binding?.tvLoginBtn?.setOnClickListener {
            login()
        }

        binding?.tvForgotPass?.setOnClickListener{
            callBack?.showFrg(TAG, M005ForgotPassFrg.TAG, true)
        }
    }


    override fun initBinding(mRootView: View): FrgM002LoginBinding {
        return FrgM002LoginBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M002LoginViewModel> {
        return M002LoginViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m002_login
    }

    override fun callBack(key: String, data: Any?) {
        if (key == M002LoginViewModel.API_KEY_LOGIN) {
            loginSuccess(data)
        } else if (key == BaseViewModel.KEY_NOTIFY) {
            Toast.makeText(mContext, data.toString(), Toast.LENGTH_SHORT).show()
            ProgressLoading.dismiss()
        }
    }

    private fun loginSuccess(data: Any?) {
        val userInfoModelRes = data as UserInfoModelRes

        val token = userInfoModelRes.data?.token
        val phone = userInfoModelRes.data?.phone
        val userName = userInfoModelRes.data?.username

        CommonUtils.getInstance().savePref(BaseViewModel.TOKEN, token)
        CommonUtils.getInstance().savePref(BaseViewModel.USER_NAME, userName)
        CommonUtils.getInstance().savePref(BaseViewModel.PHONE, phone)

        ProgressLoading.dismiss()
        Toast.makeText(mContext, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
        gotoM003MenuFrg()
    }

    private fun gotoM003MenuFrg() {
        callBack?.showFrg(TAG, M003MenuFrg.TAG, false)
    }

    private fun login() {
        val phone = binding?.edtPhone?.text.toString()
        val pass = binding?.edtPass?.text.toString()
        if (phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(mContext, "Bạn chưa nhập đủ thông tin đăng nhập", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (pass.length < 6) {
            Toast.makeText(mContext, "Mật khẩu cần ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
            return
        }

        if (!CommonUtils.getInstance().isPhone(phone)) {
            Toast.makeText(mContext, "Bạn chưa nhập đủ thông tin đăng nhập", Toast.LENGTH_SHORT)
                .show()
            return
        }

        ProgressLoading.show(mContext, false)
        mViewModel.login(phone, pass)
    }

    companion object {
        val TAG: String = M002LoginFrg::class.java.name
    }
}
