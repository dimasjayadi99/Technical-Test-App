package com.example.technicaltestapp.api.DailyApi

import com.example.technicaltestapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfigDaily {
    const val baseurl = BuildConfig.BASE_URL

    fun getRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() : ApiServiceDaily{
        return getRetrofit().create(ApiServiceDaily::class.java)
    }

}