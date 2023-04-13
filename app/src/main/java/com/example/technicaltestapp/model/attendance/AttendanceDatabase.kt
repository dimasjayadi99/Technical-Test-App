package com.example.technicaltestapp.model.attendance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Attendance::class], version = 1)
abstract class AttendanceDatabase : RoomDatabase() {

    abstract fun attendanceDao() : AttendanceDao

    companion object{
        @Volatile
        private var INSTANCE : AttendanceDatabase? = null

        fun getDatabase(context: Context) : AttendanceDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AttendanceDatabase::class.java,
                    "attendance_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}