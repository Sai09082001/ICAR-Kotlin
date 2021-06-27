package com.example.icarkotlin.view.viewmodel

import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.view.api.APIRequest
import com.example.icarkotlin.view.api.model.entities.UserRegisterEntity


class M001RegisterViewModel : BaseViewModel() {
    fun register(phone: String, pass: String, verificationId: String, code: String) {
        if (CommonUtils.getInstance().isInternetAvailable()) {
            mCallback?.showWarnNoInternet()
        }
        val api: APIRequest = getWS().create(APIRequest::class.java)
        val userEntity = UserRegisterEntity(phone, pass, verificationId, code)
        api.register(userEntity)?.enqueue(initResponse(API_REGISTER_KEY))
    }

    fun checkPhone(phone: String) {
        if (CommonUtils.getInstance().isInternetAvailable()) {
            mCallback?.showWarnNoInternet()
        }
        val api: APIRequest = getWS().create(APIRequest::class.java)
        api.checkPhone(phone)?.enqueue(initResponse(API_CHECK_PHONE_KEY))
    }

    companion object {
        const val API_REGISTER_KEY = "API_REGISTER_KEY"
        const val API_CHECK_PHONE_KEY = "API_CHECK_PHONE_KEY"
        const val PHONE_EXIST = "Tài khoản đã tồn tại"
        const val PHONE_NOT_EXIST = "Tài khoản không tồn tại"
    }
}
