package com.example.icarkotlin.view.api

import com.example.icarkotlin.view.api.model.UserInfoModelRes
import com.example.icarkotlin.view.api.model.entities.AccountEntity
import com.example.icarkotlin.view.api.model.entities.UserCreateNewPassEntity
import com.example.icarkotlin.view.api.model.entities.UserRegisterEntity
import retrofit2.Call
import retrofit2.http.*

interface APIRequest {
    @POST("auth/login")
    @Headers("Content-Type:application/json")
    fun login(@Body body: AccountEntity?): Call<UserInfoModelRes?>?

    @GET("auth/check")
    @Headers("Content-Type:application/json")
    fun checkPhone(@Query("phone") phone: String): Call<UserInfoModelRes?>?

    @POST("auth/register")
    @Headers("Content-Type:application/json")
    fun register(@Body body: UserRegisterEntity?): Call<UserInfoModelRes?>?

    @POST("auth/forgot_password")
    @Headers("Content-Type:application/json")
    fun createNewPass(@Body userCreateNewPassEntity: UserCreateNewPassEntity) : Call<UserInfoModelRes?>?
}