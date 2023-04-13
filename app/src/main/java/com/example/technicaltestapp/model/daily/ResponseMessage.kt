package com.example.technicaltestapp.model.daily

import com.google.gson.annotations.SerializedName

data class ResponseMessage(

	@field:SerializedName("result")
	val result: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
