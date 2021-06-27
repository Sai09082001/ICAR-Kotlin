package com.example.icarkotlin

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommonUtils private constructor() {
    fun isInternetAvailable(): Boolean {
        val cm : ConnectivityManager = App.getInstance().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfor = cm.activeNetworkInfo
        return networkInfor != null && networkInfor.isConnectedOrConnecting
    }

    fun clearPref(key: String) {
        val pref: SharedPreferences = App.getInstance()
            .getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        pref.edit().remove(key).apply()
    }

    fun clearPref(vararg keys: String) {
        val pref: SharedPreferences = App.getInstance()
            .getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        val edt: SharedPreferences.Editor = pref.edit()
        for (key in keys) {
            edt.remove(key)
        }
        edt.apply()
    }

    fun getPref(key: String, isDelete: Boolean): String? {
        val pref: SharedPreferences = App.getInstance()
            .getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        val vl = pref.getString(key, null)
        if (vl != null && isDelete) {
            pref.edit().remove(key).apply()
        }
        return vl
    }

    fun getPref(key: String): String? {
        return getPref(key, false)
    }

    fun savePref(key: String, value: String?) {
        val pref: SharedPreferences = App.getInstance()
            .getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        pref.edit().putString(key, value).apply()
    }

    fun isMail(email: String): Boolean {
        val pattern: Pattern = Pattern
            .compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE
            )
        val matcher: Matcher = pattern.matcher(email)
        return matcher.find()
    }

    fun isPhone(phone: String): Boolean {
        return phone.matches("^(09|03|07|08|05)\\d{8}$".toRegex())
    }

    fun sendVerificationCode(phone: String, activity : FragmentActivity, authCallBack: PhoneAuthCallBack.CodeSentCallBack?) {
        if(FirebaseAuth.getInstance().currentUser != null){
            FirebaseAuth.getInstance().signOut()
        }

        if(!getInstance().isInternetAvailable()){
            return
        }

        var phoneNo = ""
        if(phone.startsWith("0")){
            phoneNo = "+84${phone.substring(1)}"
        }
        if(phone.isEmpty()){
            Toast.makeText(App.getInstance(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }
        if(authCallBack == null){
            return
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNo,
            60,
            TimeUnit.SECONDS,
            activity,
            PhoneAuthCallBack(authCallBack)
        )
    }

    companion object {
        private const val PREF_FILE: String = "file_pref"
        private var INSTANCE: CommonUtils? = null
        fun getInstance(): CommonUtils {
            if (INSTANCE == null) {
                INSTANCE = CommonUtils()
            }
            return INSTANCE!!
        }
    }
}