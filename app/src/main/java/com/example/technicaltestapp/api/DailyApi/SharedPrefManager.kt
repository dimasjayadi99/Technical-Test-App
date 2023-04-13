package com.example.technicaltestapp.api.DailyApi

import android.annotation.SuppressLint
import android.content.Context
import com.example.technicaltestapp.model.UserAuth
import com.example.technicaltestapp.model.daily.ResponseDaily

class SharedPrefManager private constructor(private val mCtx : Context){
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("id", null)?.toIntOrNull() != null
        }

    val user: UserAuth
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return UserAuth(
                sharedPreferences.getString("nik", "")!!
            )
        }

    fun saveUser(user: ResponseDaily?) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("nik", user?.data?.get(0)?.nik)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_preff"
        @SuppressLint("StaticFieldLeak")
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }
}