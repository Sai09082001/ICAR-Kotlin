package com.example.icarkotlin

import android.util.Log
import android.widget.Toast
import com.example.icarkotlin.view.ProgressLoading
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


class PhoneAuthCallBack(var mCallBack: CodeSentCallBack) :
    PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
        mCallBack.onCodeSent(s)
    }

    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
        Log.i(TAG, "onVerificationCompleted")
    }

    override fun onVerificationFailed(p0: FirebaseException) {
        ProgressLoading.dismiss()
        Toast.makeText(
            App.getInstance(),
            "Không xác thực được tài khoản, thử lại sau",
            Toast.LENGTH_SHORT
        ).show()
    }

    interface CodeSentCallBack {
        fun onCodeSent(s: String)
    }

    companion object {
        val TAG = PhoneAuthCallBack::class.java.name
    }
}