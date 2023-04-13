package com.example.technicaltestapp.model.attendance

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "attendance")
@Parcelize
data class Attendance(@PrimaryKey(autoGenerate = true) val id : Int, val nik : String, val tanggalKehadiran : String, val jamMasuk : String, val jamPulang : String, val telat : String, val pulangAwal : String) : Parcelable