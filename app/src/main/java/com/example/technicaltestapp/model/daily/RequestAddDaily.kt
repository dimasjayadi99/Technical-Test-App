package com.example.technicaltestapp.model.daily

import com.google.gson.annotations.SerializedName

data class RequestAddDaily(

	@field:SerializedName("UserID")
	val userID: String?,

	@field:SerializedName("Password")
	val password: String?,

	@field:SerializedName("Token")
	val token: String?,

	@field:SerializedName("GroupCD")
	val groupCD: String?,

	@field:SerializedName("RequestData")
	val requestData: RequestData?
)

data class RequestData(

	@field:SerializedName("NIK")
	val nIK: String?,

	@field:SerializedName("KdDept")
	val kdDept: String?,

	@field:SerializedName("CategorySeq")
	val categorySeq: String?,

	@field:SerializedName("ActDate")
	val actDate: String?,

	@field:SerializedName("TimeIn")
	val timeIn: String?,

	@field:SerializedName("TimeOut")
	val timeOut: String?,

	@field:SerializedName("Activity")
	val activity: String?,

	@field:SerializedName("Deadline")
	val deadline: String?,

	@field:SerializedName("Remark")
	val remark: String?,

	@field:SerializedName("SavedBy")
	val savedBy: String?
)
