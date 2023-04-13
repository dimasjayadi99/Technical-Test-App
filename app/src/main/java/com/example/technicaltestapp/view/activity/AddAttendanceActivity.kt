package com.example.technicaltestapp.view.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.example.technicaltestapp.api.DailyApi.SharedPrefManager
import com.example.technicaltestapp.databinding.ActivityAddAttendanceBinding
import com.example.technicaltestapp.model.attendance.Attendance
import com.example.technicaltestapp.model.attendance.AttendanceDatabase
import com.example.technicaltestapp.model.attendance.AttendanceRepository
import com.example.technicaltestapp.viewmodel.AttendanceViewModel
import com.example.technicaltestapp.viewmodel.AttendanceViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*

class AddAttendanceActivity : AppCompatActivity() {

    private var _binding : ActivityAddAttendanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timedialog : TimePickerDialog

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val attendanceDao = AttendanceDatabase.getDatabase(this).attendanceDao()
        val attendanceRepository = AttendanceRepository(attendanceDao)
        attendanceViewModel = ViewModelProvider(this,AttendanceViewModelFactory(attendanceRepository))[AttendanceViewModel::class.java]

        binding.apply {

            val indicator = intent.getIntExtra("indicator",1)
            val dataAttendance = intent.getParcelableExtra<Attendance>("attendanceItem")
            val nik = SharedPrefManager.getInstance(this@AddAttendanceActivity).user.nik
            etNik.setText(nik)

            // set date
            val date = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dates = dateFormat.format(date)
            etTanggalKehadiran.setText(dates)
            // date picker
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            datePickerDialog(dateFormat,year,month,dayOfMonth)
            datePicker.setOnClickListener{
                datePickerDialog.show()
            }

            // set time in dan time out
            val time = Calendar.getInstance()
            time.set(Calendar.HOUR_OF_DAY, 0)
            time.set(Calendar.MINUTE, 0)
            // default time
            etJamHadir.setText("00:00")
            etJamPulang.setText("00:00")

            timeinPicker.setOnClickListener{
                timedialogIn(time)
                timedialog.show()
            }

            timeoutPicker.setOnClickListener{
                timedialogOut(time)
                timedialog.show()
            }

            if (indicator == 2){
                btnSimpan.text = "Update Data"
                etNik.setText(dataAttendance?.nik)
                etTanggalKehadiran.setText(dataAttendance?.tanggalKehadiran)
                etJamHadir.setText(dataAttendance?.jamMasuk)
                etJamPulang.setText(dataAttendance?.jamPulang)
            }

            btnSimpan.setOnClickListener{
                val id = dataAttendance?.id
                val nik = etNik.text.toString()
                val tanggal = etTanggalKehadiran.text.toString()
                val timeIn = etJamHadir.text.toString()
                val timeOut = etJamPulang.text.toString()

                val jamMasuk = LocalTime.parse(timeIn)
                val jamMasukStandar = LocalTime.of(8, 30)
                var telat = 0
                if (jamMasuk.isAfter(jamMasukStandar)) {
                    telat = jamMasuk.until(jamMasukStandar, ChronoUnit.MINUTES).toInt()
                }

                val jamPulang = LocalTime.parse(timeOut)
                val jamPulangStandar = LocalTime.of(17, 30)
                var pulangAwal = 0
                if (jamPulang.isBefore(jamPulangStandar)) {
                    pulangAwal = jamPulangStandar.until(jamPulang, ChronoUnit.MINUTES).toInt()
                }

                if (nik.isEmpty()){
                    etNik.error = "Empty"
                    etNik.requestFocus()
                }else if (tanggal.isEmpty()){
                    etTanggalKehadiran.error = "Empty"
                    etTanggalKehadiran.requestFocus()
                }else if (timeIn.isEmpty()){
                    etJamHadir.error = "Empty"
                    etJamHadir.requestFocus()
                }else if (timeOut.isEmpty()){
                    etJamPulang.error = "Empty"
                    etJamPulang.requestFocus()
                }else{
                    if (indicator == 1){
                        saveDataPresensi(nik,tanggal,timeIn,timeOut,telat.toString(),pulangAwal.toString())
                    }else{
                        // update data
                        updaeDataPresensi(id!!,nik,tanggal,timeIn,timeOut,telat.toString(),pulangAwal.toString())
                    }
                }

            }

            // on back press
            imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@AddAttendanceActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
        })

    }

    private fun datePickerDialog(dateFormat: SimpleDateFormat, year: Int, month: Int, dayOfMonth: Int) {
        datePickerDialog = DatePickerDialog(
            this@AddAttendanceActivity,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Aksi yang dilakukan ketika tanggal dipilih
                // Anda dapat menampilkan tanggal yang dipilih ke dalam TextView atau melakukan aksi lainnya
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.etTanggalKehadiran.setText(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )
    }

    private fun timedialogIn(time: Calendar) {
        timedialog = TimePickerDialog(
            this@AddAttendanceActivity,
            { _, hourOfDay, minute ->
                // Handle time set
                binding.etJamHadir.setText(String.format("%02d:%02d", hourOfDay, minute))
            },
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            true // 24 hour format
        )
    }

    private fun timedialogOut(time: Calendar) {
        timedialog = TimePickerDialog(
            this@AddAttendanceActivity,
            { _, hourOfDay, minute ->
                // Handle time set
                binding.etJamPulang.setText(String.format("%02d:%02d", hourOfDay, minute))
            },
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            true // 24 hour format
        )
    }

    private fun saveDataPresensi(nik: String, tanggal: String, timeIn: String, timeOut: String, telat: String, pulangAwal: String) {
        val id = 0
        val requestData = Attendance(id,nik,tanggal,timeIn,timeOut,telat,pulangAwal)
        attendanceViewModel.insertAttendance(requestData)
        Toast.makeText(this, "Berhasil Disimpan!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun updaeDataPresensi(id: Int, nik: String, tanggal: String, timeIn: String, timeOut: String, telat: String, pulangAwal: String) {
        val requestData = Attendance(id,nik,tanggal,timeIn,timeOut,telat,pulangAwal)
        attendanceViewModel.updateAttendance(requestData)
        Toast.makeText(this, "Berhasil Diupdate!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}