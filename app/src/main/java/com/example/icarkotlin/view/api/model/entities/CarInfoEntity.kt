package com.example.icarkotlin.view.api.model.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CarInfoEntity : Serializable {
    @SerializedName("id")
    val id: Int? = null
    @SerializedName("name")
    val name: String? = null
    @SerializedName("phone_manager")
    val phoneManager: String? = null
    @SerializedName("password_manager")
    val passwordManager: String? = null
    @SerializedName("car_number")
    val carNumber: String? = null
    @SerializedName("car_brand")
    val carBrand: String? = null
    @SerializedName("active_status")
    val activeStatus: String? = null
    @SerializedName("last_address")
    val lastAddress: String? = null
    @SerializedName("last_speed")
    val lastSpeed: String? = null
    @SerializedName("last_lat")
    val lastLat: String? = null
    @SerializedName("last_lng")
    val lastLng: String? = null
    @SerializedName("last_status")
    val lastStatus: String? = null
}