package com.example.technicaltestapp.model.daily

import com.google.gson.annotations.SerializedName

data class ResponseCategory(

	@field:SerializedName("result")
	val result: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItemCategory?>? = null
)

data class DataItemCategory(

	@field:SerializedName("KdDept")
	val kdDept: String? = null,

	@field:SerializedName("Category")
	val category: String? = null,

	@field:SerializedName("Seq")
	val seq: String? = null

)
