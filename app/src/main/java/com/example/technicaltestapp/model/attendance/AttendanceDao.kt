package com.example.technicaltestapp.model.attendance

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM attendance ORDER BY id DESC")
    fun getAttendance() : LiveData<List<Attendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Update
    suspend fun updateAttendance(attendance: Attendance)

    @Query("DELETE FROM attendance WHERE id = :id")
    suspend fun deleteAttendance(id : Int)

    @Query("SELECT * FROM attendance WHERE tanggalKehadiran LIKE '%' || :tanggalKehadiran || '%' ORDER BY id DESC")
    fun searchAttendance(tanggalKehadiran : String) : LiveData<List<Attendance>>

}