package com.example.icarkotlin.view.viewmodel

import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.view.api.APIRequest

class M003MenuViewModel : BaseViewModel() {

    fun getListCar() {
        val token = CommonUtils.getInstance().getPref(TOKEN)
        val api: APIRequest = getWS().create(APIRequest::class.java)
        api.getListCar(token, 1, 1)?.enqueue(initResponse(API_KEY_GET_LIST_CAR))
    }

    companion object {
        const val API_KEY_GET_LIST_CAR = "API_KEY_GET_LIST_CAR"
    }
}
