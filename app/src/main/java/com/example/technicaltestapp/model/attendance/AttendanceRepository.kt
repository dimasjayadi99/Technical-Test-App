package com.example.technicaltestapp.model.attendance

import androidx.lifecycle.LiveData

class AttendanceRepository (private val attendanceDao: AttendanceDao) {

    val listAttendance : LiveData<List<Attendance>> = attendanceDao.getAttendance()

    suspend fun insertAttendance(attendance: Attendance) {
        attendanceDao.insertAttendance(attendance)
    }

    suspend fun deleteAttendance(id : Int){
        attendanceDao.deleteAttendance(id)
    }

    suspend fun updateAttendance(attendance: Attendance){
        attendanceDao.updateAttendance(attendance)
    }

    fun searchAttendance(tanggalKehadiran : String) : LiveData<List<Attendance>>{
        return attendanceDao.searchAttendance(tanggalKehadiran)
    }


}