package com.example.technicaltestapp.api.DailyApi

import com.example.technicaltestapp.model.daily.*
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceDaily {
    @POST("activity?act=act_s")
    fun getDaily(@Body requestGetDaily: RequestGetDaily): Call<ResponseDaily>

    @POST("activity?act=act_s_add")
    fun addDaily(@Body requestAddDaily: RequestAddDaily) : Call<ResponseMessage>

    @POST("global?act=act_e")
    fun getCategory(@Body requestCategory: RequestCategory) : Call<ResponseCategory>

    @POST("activity?act=act_s_edt")
    fun updateDaily(@Body requestUpdateDaily: RequestUpdateDaily) : Call<ResponseMessage>

}