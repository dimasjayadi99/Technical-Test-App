package com.example.technicaltestapp.model.daily

import com.google.gson.annotations.SerializedName

data class RequestCategory(

	@field:SerializedName("UserID")
	val userID: String?,

	@field:SerializedName("Password")
	val password: String?,

	@field:SerializedName("Token")
	val token: String?,

	@field:SerializedName("GroupCD")
	val groupCD: String?,

	@field:SerializedName("RequestData")
	val requestData: RequestDataCategory?
)

data class RequestDataCategory(

	@field:SerializedName("Type")
	val type: String?,

	@field:SerializedName("KdDept")
	val kdDept: String?,

	@field:SerializedName("TxtSearch")
	val txtSearch: String?

)
