package com.example.icarkotlin.view.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseModel<T> : Serializable {
    @SerializedName("success")
    var status: Boolean = false

    @SerializedName("message")
    var message: String = ""

    @SerializedName("data")
    var data: T? = null
}