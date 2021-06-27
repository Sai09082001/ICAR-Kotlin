package com.example.icarkotlin.view.fragment

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.PhoneAuthCallBack
import com.example.icarkotlin.R
import com.example.icarkotlin.databinding.FrgM001RegisterBinding
import com.example.icarkotlin.view.ProgressLoading
import com.example.icarkotlin.view.api.model.UserInfoModelRes
import com.example.icarkotlin.view.dialog.OTPConfirmDialog
import com.example.icarkotlin.view.viewmodel.BaseViewModel
import com.example.icarkotlin.view.viewmodel.M001RegisterViewModel
import com.google.firebase.auth.FirebaseAuth

class M001RegisterFrg : BaseFragment<FrgM001RegisterBinding, M001RegisterViewModel>(),
    PhoneAuthCallBack.CodeSentCallBack {
    private var otpDialog: OTPConfirmDialog? = null
    private var mCodeSent: String? = null
    private var edtPhone: EditText? = null
    private var edtPass: EditText? = null
    private var edtConfirmPass: EditText? = null
    private var phone: String? = null
    private var pass: String? = null

    override fun initViews() {
        findViewById<TextView>(R.id.tv_register_btn)?.setOnClickListener(this)
        edtPhone = findViewById(R.id.edt_phone)
        edtPass = findViewById(R.id.edt_pass)
        edtConfirmPass = findViewById(R.id.edt_confirm_pass)
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.setLanguageCode("vi")
    }

    override fun initBinding(mRootView: View): FrgM001RegisterBinding? {
        return FrgM001RegisterBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M001RegisterViewModel> {
        return M001RegisterViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m001_register
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.tv_register_btn) {
            phone = edtPhone?.text.toString()
            pass = edtPass?.text.toString()
            val passConf = edtConfirmPass?.text.toString()
            if (phone!!.isEmpty() || pass!!.isEmpty() || passConf.isEmpty()) {
                Toast.makeText(mContext, R.string.txt_warn_not_enough_info, Toast.LENGTH_LONG)
                    .show()
                return
            }
            if (pass!!.length < 6 || passConf.length < 6) {
                Toast.makeText(mContext, R.string.txt_warn_pass_at_last_6_length, Toast.LENGTH_LONG)
                    .show()
                return
            }
            if (passConf != pass) {
                Toast.makeText(mContext, R.string.txt_warn_pass_not_match, Toast.LENGTH_LONG).show()
                return
            }
            if (!CommonUtils.getInstance().isPhone(phone!!)) {
                Toast.makeText(mContext, R.string.txt_phone_is_invalid, Toast.LENGTH_LONG).show()
                return
            }
            ProgressLoading.show(mContext, false)
            mViewModel.checkPhone(phone!!)
        }
    }

    override fun callBack(key: String, data: Any?) {
        when (key) {
            M001RegisterViewModel.API_REGISTER_KEY -> {
                Toast.makeText(mContext, R.string.txt_register_success, Toast.LENGTH_SHORT).show()
                otpDialog?.dismiss()
                ProgressLoading.dismiss()
                callBack?.showFrg(TAG, M002LoginFrg.TAG, false)
            }

            M001RegisterViewModel.API_CHECK_PHONE_KEY -> {
                val userInfoModelRes: UserInfoModelRes = data as UserInfoModelRes
                val status: String = userInfoModelRes.message
                if (status == M001RegisterViewModel.PHONE_EXIST) {
                    Toast.makeText(mContext, R.string.txt_warn_phone_exist, Toast.LENGTH_SHORT)
                        .show()
                } else if (status == M001RegisterViewModel.PHONE_NOT_EXIST) {
                    CommonUtils.getInstance()
                        .sendVerificationCode(phone!!, requireActivity(), getAuthCallBack())
                }
                ProgressLoading.dismiss()
            }

            OTPConfirmDialog.KEY_CONFIRM_OTP -> {
                Log.i(TAG, "callBack cf OTP")
                ProgressLoading.show(mContext, false)
                Log.i(TAG, "verification ID $mCodeSent otp ${data.toString()}")
                mViewModel.register(phone!!, pass!!, mCodeSent!!, data.toString())
            }

            BaseViewModel.KEY_NOTIFY -> {
                Toast.makeText(mContext, data.toString(), Toast.LENGTH_SHORT).show()
                ProgressLoading.dismiss()
            }
        }
    }


    private fun getAuthCallBack(): PhoneAuthCallBack.CodeSentCallBack {
        return this
    }
    override fun onCodeSent(verificationId: String) {
        Log.i(TAG, "onCodeSent: $verificationId")
        mCodeSent = verificationId
        Log.i(TAG, "onCodeSent: $mCodeSent")
        otpDialog = OTPConfirmDialog(mContext, phone, this)
        ProgressLoading.dismiss()
        otpDialog?.show()
    }

    companion object {
        val TAG = M001RegisterFrg::class.java.name
    }

}
