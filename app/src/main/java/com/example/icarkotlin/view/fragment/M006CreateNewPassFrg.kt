package com.example.icarkotlin.view.fragment

import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.PhoneAuthCallBack
import com.example.icarkotlin.R
import com.example.icarkotlin.databinding.FrgM006CreateNewPassBinding
import com.example.icarkotlin.view.ProgressLoading
import com.example.icarkotlin.view.dialog.OTPConfirmDialog
import com.example.icarkotlin.view.viewmodel.M006CreateNewPassViewModel
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider



class M006CreateNewPassFrg :
    BaseFragment<FrgM006CreateNewPassBinding, M006CreateNewPassViewModel>(),
    PhoneAuthCallBack.CodeSentCallBack{
    private var pass: String? = null
    private var confirmPass: String? = null
    private var mCodeSent: String? = null
    private lateinit var otpConfirmDialog: OTPConfirmDialog
    private var credential: PhoneAuthCredential? = null
    override fun initViews() {

        binding?.tvPhone?.text = mData.toString()
        binding?.tvCreateNewPass?.setOnClickListener {
            createNewPass()
        }
    }

    private fun createNewPass() {
        pass = binding?.edtPass?.text.toString()
        confirmPass = binding?.edtConfirmPass?.text.toString()

        if (pass!!.length < 6 || confirmPass!!.length < 6) {
            Toast.makeText(mContext, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
            return
        }
        if (pass != confirmPass) {
            Toast.makeText(mContext, "Mật khẩu nhập lại chưa khớp", Toast.LENGTH_SHORT).show()
            return
        }
        ProgressLoading.show(mContext, false)
        CommonUtils.getInstance().sendVerificationCode(
            binding?.tvPhone?.text.toString(),
            requireActivity(),
            getAuthCallBack()
        )
    }

    override fun callBack(key: String, data: Any?) {
        when (key) {
            M006CreateNewPassViewModel.API_CREATE_NEW_PASS_KEY -> {
                Toast.makeText(mContext, R.string.create_new_pass_success, Toast.LENGTH_SHORT).show()
                otpConfirmDialog?.dismiss()
                ProgressLoading.dismiss()
                callBack?.showFrg(TAG, M002LoginFrg.TAG, false)
            }

            OTPConfirmDialog.KEY_CONFIRM_OTP -> {
                credential = PhoneAuthProvider.getCredential(mCodeSent!!, data.toString())
                if(credential == null){
                    Toast.makeText(mContext, "Mã xác thực không đúng, thử lại sau", Toast.LENGTH_SHORT)
                    return
                }
                mViewModel.createNewPass(binding?.tvPhone?.text.toString(),
                    binding?.edtConfirmPass?.text.toString(),
                    mCodeSent!!,
                    data.toString())
            }
        }
    }

    private fun getAuthCallBack(): PhoneAuthCallBack.CodeSentCallBack {
        return this
    }

    override fun initBinding(mRootView: View): FrgM006CreateNewPassBinding? {
        return FrgM006CreateNewPassBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M006CreateNewPassViewModel> {
        return M006CreateNewPassViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m006_create_new_pass
    }

    override fun onCodeSent(verificationId: String) {
        Log.i(TAG, "onCodeSent : $verificationId")
        mCodeSent = verificationId
        Log.i(TAG, "onCodeSent : $mCodeSent")
        otpConfirmDialog = OTPConfirmDialog(mContext, binding?.tvPhone?.text.toString(), this)
        ProgressLoading.dismiss()
        otpConfirmDialog.show()
    }

    companion object {
        val TAG = M006CreateNewPassFrg::class.java.name
    }
}
