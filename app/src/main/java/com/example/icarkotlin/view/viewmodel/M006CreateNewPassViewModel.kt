package com.example.icarkotlin.view.viewmodel

import com.example.icarkotlin.view.api.APIRequest
import com.example.icarkotlin.view.api.model.entities.UserCreateNewPassEntity


class M006CreateNewPassViewModel : BaseViewModel() {
    companion object {
        val API_CREATE_NEW_PASS_KEY: String = "API_CREATE_NEW_PASS_KEY"
    }

    fun createNewPass(phone: String, newPass: String, verificationId: String, code: String) {
        val api: APIRequest = getWS().create(APIRequest::class.java)
        val userEntity = UserCreateNewPassEntity(phone, newPass, verificationId, code)
        api.createNewPass(userEntity)?.enqueue(initResponse(API_CREATE_NEW_PASS_KEY))
    }
}
