package com.example.technicaltestapp.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technicaltestapp.model.attendance.Attendance
import com.example.technicaltestapp.model.attendance.AttendanceRepository
import com.example.technicaltestapp.view.activity.AddAttendanceActivity
import kotlinx.coroutines.launch

class AttendanceViewModel(private val attendanceRepository: AttendanceRepository) : ViewModel() {
    val attendanceList : LiveData<List<Attendance>> = attendanceRepository.listAttendance

    fun insertAttendance(attendance: Attendance) = viewModelScope.launch {
        attendanceRepository.insertAttendance(attendance)
    }

    fun updateAttendance(attendance: Attendance) = viewModelScope.launch {
        attendanceRepository.updateAttendance(attendance)
    }

    fun deleteAttendance(id : Int) = viewModelScope.launch {
        attendanceRepository.deleteAttendance(id)
    }

    fun searchAttendance(tanggalKehadiran : String) : LiveData<List<Attendance>> = attendanceRepository.searchAttendance(tanggalKehadiran)

    fun onItemClickListener(activity: Activity,attendance: Attendance){
        val intent = Intent(activity,AddAttendanceActivity::class.java)
        intent.putExtra("indicator",2)
        intent.putExtra("attendanceItem", attendance)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
    }

}