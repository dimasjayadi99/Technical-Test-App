package com.example.technicaltestapp.model.daily

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DataItemDaily(
	@SerializedName("Seq")
	val seq: String?,
	@SerializedName("NIK")
	val nik: String?,
	@SerializedName("NamaKaryawan")
	val namaKaryawan: String?,
	@SerializedName("NamaHead")
	val namaHead: String?,
	@SerializedName("KdDept")
	val kdDept: String?,
	@SerializedName("Department")
	val department: String?,
	@SerializedName("ActDate")
	val actDate: String?,
	@SerializedName("Deadline")
	val deadline: String?,
	@SerializedName("TimeIn")
	val timeIn: String?,
	@SerializedName("TimeOut")
	val timeOut: String?,
	@SerializedName("Status")
	val status: String?,
	@SerializedName("Category")
	val category: String?,
	@SerializedName("CreatedDate")
	val createdDate: String?,
	@SerializedName("Activity")
	val activity: String?,
	@SerializedName("Remark")
	val remark: String?
) : Parcelable