package com.example.icarkotlin.view.fragment

import android.view.View
import android.widget.Toast
import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.R
import com.example.icarkotlin.databinding.FrgM005ForgotPassBinding
import com.example.icarkotlin.view.api.model.UserInfoModelRes
import com.example.icarkotlin.view.viewmodel.M001RegisterViewModel
import com.example.icarkotlin.view.viewmodel.M005ForgotPassViewModel


class M005ForgotPassFrg : BaseFragment<FrgM005ForgotPassBinding, M005ForgotPassViewModel>() {

    override fun initViews() {
        binding?.tvTakePass?.setOnClickListener {
            checkInput(binding?.edtPhone?.text.toString())
        }
    }

    private fun checkInput(phone: String) {
        if (!CommonUtils.getInstance().isPhone(phone)) {
            Toast.makeText(mContext, "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT)
                .show()
        } else {
            mViewModel.checkPhone(phone)
        }
    }

    override fun callBack(key: String, data: Any?) {
        when (key) {
            M005ForgotPassViewModel.API_CHECK_PHONE_KEY -> {
                val userInfoModelRes: UserInfoModelRes = data as UserInfoModelRes
                val status = userInfoModelRes.message
                if (status == M001RegisterViewModel.PHONE_EXIST) {
                    callBack?.showFrg(
                        TAG,
                        binding?.edtPhone?.text.toString(),
                        M006CreateNewPassFrg.TAG,
                        true
                    )
                } else {
                    Toast.makeText(
                        mContext,
                        "Số điện thoại không tồn tại trong hệ thống",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun initBinding(mRootView: View): FrgM005ForgotPassBinding {
        return FrgM005ForgotPassBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M005ForgotPassViewModel> {
        return M005ForgotPassViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m005_forgot_pass
    }

    companion object {
        val TAG = M005ForgotPassFrg::class.java.name
    }
}
