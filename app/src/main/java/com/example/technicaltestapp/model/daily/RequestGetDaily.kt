package com.example.technicaltestapp.model.daily

import com.google.gson.annotations.SerializedName

data class RequestGetDaily(
        @field:SerializedName("UserID")
        val userID: String?,

        @field:SerializedName("Password")
        val password: String?,

        @field:SerializedName("Token")
        val token: String?,

        @field:SerializedName("GroupCD")
        val groupCD: String?,

        @field:SerializedName("RequestData")
        val requestData: RequestDataDaily
)

data class RequestDataDaily(

        @field:SerializedName("NIK")
        val nIK: String? = null,

        @field:SerializedName("FromDate")
        val fromDate: String? = null,

        @field:SerializedName("ToDate")
        val toDate: String? = null,

        @field:SerializedName("Status")
        val status: String? = null
)