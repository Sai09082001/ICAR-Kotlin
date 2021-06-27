package com.example.icarkotlin.view.api.model.entities

import com.example.icarkotlin.App
import com.example.icarkotlin.CommonUtils
import com.google.gson.annotations.SerializedName

import lombok.ToString
import java.io.Serializable

@ToString
class AccountEntity : Serializable {
    @SerializedName("phone")
    var mPhone: String? = null

    @SerializedName("password")
    var mPass: String? = null

    @SerializedName("device_token")
    var mDeviceToken: String? = null

    constructor(mPhone : String, mPass : String) : super(){
        this.mPhone = mPhone
        this.mPass = mPass
        mDeviceToken = CommonUtils.getInstance().getPref(App.DEVICE_TOKEN)
    }
}

