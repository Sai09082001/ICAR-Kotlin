package com.example.icarkotlin.view.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.icarkotlin.App
import com.example.icarkotlin.CommonUtils
import com.example.icarkotlin.Storage
import com.example.icarkotlin.view.ProgressLoading
import com.example.icarkotlin.view.api.model.BaseModel
import com.example.icarkotlin.view.callback.OnActionCallBack
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier


abstract class BaseViewModel : ViewModel() {
    val ex = MutableLiveData(false)
    val token: String?
        get() = CommonUtils.getInstance().getPref(TOKEN)
    val storage: Storage?
        get() = App.storage
    protected var mCallback: OnActionCallBack? = null
    protected fun <T> initResponse(key: String) : Callback<T>{
        return object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                ProgressLoading.dismiss()
                if(response.code() == CODE_200 || response.code() == CODE_201){
                    handleSuccess(key, response.body())
                } else {
                    errorReport(key, response.code(), response.errorBody())
                    ex.value = true
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                notifyToView("Lỗi hệ thống, thử lại sau!!!")
                Log.i(TAG, "onFailure..." + t.message)
                ProgressLoading.dismiss()
                ex.value = true
            }
        }
    }

    protected fun errorReport(key: String, code: Int, errorBody: ResponseBody?) {
        Log.i("TAG", "errorReport:$code")
        try {
            var err : BaseModel<*>? = null
            try {
                err = Gson(). fromJson<BaseModel<*>>(errorBody!!.string(), BaseModel::class.java)
            } catch (e: Exception){
                e.printStackTrace()
            }
            when (code){
                CODE_400 -> if (errorBody != null) {
                    notifyToView(if (err != null) err.message else errorBody.string())
                }
                CODE_401 -> {
                    notifyToView("Thông tin đăng nhập sai hoặc tài khoản đã hết hạn, hãy thử đăng nhập lại!")
                    mCallback!!.callBack(KEY_LOGOUT, null)
                    mCallback!!.logout()
                }
                CODE_403, CODE_404, CODE_500 -> {
                    Log.d("TAG", "errorReport: " + errorBody.toString())
                    notifyToView("Lỗi hệ thống, thử lại sau!!!")
                }
                else -> {

                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
        mCallback!!.callBack(KEY_ERROR, code)
    }

    protected fun notifyToView(msg: String?) {
        mCallback!!.callBack(KEY_NOTIFY, msg)
    }

    fun setCallBack(mCallBack: OnActionCallBack?) {
        this.mCallback = mCallBack
    }

    protected open fun handleSuccess(key: String, value: Any?) {
        mCallback!!.callBack(key, value)
    }

    protected fun getWS() : Retrofit{
        if(!CommonUtils.getInstance().isInternetAvailable()){
            ex.value = true
        }

        val client : OkHttpClient = OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .hostnameVerifier(HostnameVerifier { hostname, session -> true })
            .readTimeout(30, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object{
        const val USER_NAME: String = "USER_NAME"
        const val KEY_NOTIFY = "KEY_NOTIFY"
        const val KEY_LOGOUT = "KEY_LOGOUT"
        const val KEY_ERROR = "KEY_ERROR"
        const val TOKEN = "TOKEN"
        const val PHONE: String = "PHONE"
        protected const val CODE_400 = 400
        protected const val CODE_401 = 401
        protected const val CODE_403 = 403
        protected const val CODE_404 = 404
        protected const val CODE_500 = 500
        protected const val CODE_200 = 200
        protected const val CODE_201 = 201
        private val TAG: String = BaseViewModel::class.java.name
        var BASE_URL = "https://icar-api.techja.edu.vn/api/v1/"
    }
}