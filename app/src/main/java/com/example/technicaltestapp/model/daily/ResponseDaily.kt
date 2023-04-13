package com.example.technicaltestapp.model.daily

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseDaily(
    @SerializedName("result")
    val result: Int?,
    @SerializedName("data")
    val data: List<DataItemDaily>?,
    @SerializedName("message")
    val message: String?
) : Parcelable