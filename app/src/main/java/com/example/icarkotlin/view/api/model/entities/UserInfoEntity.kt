package com.example.icarkotlin.view.api.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserInfoEntity : Serializable {
    @SerializedName("username")
    val username: String? = null

    @SerializedName("phone")
    val phone: String? = null

    @SerializedName("token")
    val token: String? = null
}