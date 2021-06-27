package com.example.icarkotlin.view.api.model.entities

import com.example.icarkotlin.App
import com.example.icarkotlin.CommonUtils
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class UserCreateNewPassEntity : Serializable {
    @SerializedName("phone")
    var mPhone: String? = null

    @SerializedName("new_password")
    var mNewPass: String? = null

    @SerializedName("verification_id")
    var mVerificationId: String? = null

    @SerializedName("code")
    var mCode: String? = null

    @SerializedName("device_token")
    var mDeviceToken: String? = null

    constructor(mPhone: String, mNewPass: String, mVerificationId : String, mCode : String) : super() {
        this.mPhone = mPhone
        this.mNewPass = mNewPass
        this.mVerificationId = mVerificationId
        this.mCode = mCode
        mDeviceToken = CommonUtils.getInstance().getPref(App.DEVICE_TOKEN)
    }
}