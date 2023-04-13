package com.example.technicaltestapp.view.activity

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.technicaltestapp.BuildConfig
import com.example.technicaltestapp.api.DailyApi.ApiConfigDaily
import com.example.technicaltestapp.api.DailyApi.SharedPrefManager
import com.example.technicaltestapp.databinding.ActivityAddDailyBinding
import com.example.technicaltestapp.model.daily.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddDailyActivity : AppCompatActivity() {

    private var _binding : ActivityAddDailyBinding? = null
    private val binding get() = _binding!!
    private var category : String = ""
    private lateinit var timedialog : TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddDailyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            val indicator = intent.getIntExtra("indicator",1)
            val dataDaily = intent.getParcelableExtra<DataItemDaily>("dataItemDaily")
            val seq = dataDaily?.seq
            var status = dataDaily?.status
            val sharedPrefManager = SharedPrefManager
            val nik = sharedPrefManager.getInstance(this@AddDailyActivity).user.nik
            etNik.setText(nik)

            // set date
            val date = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dates = dateFormat.format(date)
            etActDate.setText(dates)

            // date picker
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            datePickerDialog(dateFormat,year,month,dayOfMonth)

            // set time in dan time out
            val time = Calendar.getInstance()
            time.set(Calendar.HOUR_OF_DAY, 0)
            time.set(Calendar.MINUTE, 0)
            // default time
            etTimeIn.setText("00:00")
            etTimeOut.setText("00:00")

            if(indicator == 2){
                // get parsing data
                btnSimpan.text = "Update Data"
                etNik.isEnabled = false
                textView4.visibility = View.VISIBLE
                spinnerStatus.visibility = View.VISIBLE
                etNik.setText(dataDaily?.nik)
                etActDate.setText(dataDaily?.actDate)
                etTimeIn.setText(dataDaily?.timeIn)
                etTimeOut.setText(dataDaily?.timeOut)
                etActivity.setText(dataDaily?.activity)
                etDeadline.setText(dataDaily?.deadline)
                etRemark.setText(dataDaily?.remark)
            }

            datePicker.setOnClickListener{
                datePickerDialog.show()
            }

            timein.setOnClickListener{
                timedialogIn(time)
                timedialog.show()
            }

            timeout.setOnClickListener{
                timedialogOut(time)
                timedialog.show()
            }

            // spinner category
            DataCategoryFromSpinner()

            // spinner status
            val data = arrayListOf("On Progress", "Selesai")
            val itemValueMap = hashMapOf(
                "On Progress" to "P",
                "Selesai" to "D"
            )

            spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    val selectedValue = itemValueMap[selectedItem]
                    // memperbarui nilai status sesuai pilihan pengguna
                    status = selectedValue
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    status = "" // menghapus nilai status jika pengguna tidak memilih apapun
                }
            }

            // set adapter spinner
            val adapter = ArrayAdapter(this@AddDailyActivity, R.layout.simple_spinner_item, data)
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerStatus.adapter = adapter

            btnSimpan.setOnClickListener{
                val nik = etNik.text.toString()
                val kd_dept = etDept.text.toString()
                val actDate = etActDate.text.toString()
                val time_in = etTimeIn.text.toString()
                val time_out = etTimeOut.text.toString()
                val myActivity = etActivity.text.toString()
                val deadline = etDeadline.text.toString()
                val remark = etRemark.text.toString()

                if (nik.isEmpty()){
                    etNik.error = "Empty"
                    etNik.requestFocus()
                }else if (kd_dept.isEmpty()){
                    etDept.error = "Empty"
                    etDept.requestFocus()
                }else if (actDate.isEmpty()){
                    etActDate.error = "Empty"
                    etActDate.requestFocus()
                }else if (time_in.isEmpty()){
                    etTimeIn.error = "Empty"
                    etTimeIn.requestFocus()
                }else if (time_out.isEmpty()){
                    etTimeOut.error = "Empty"
                    etTimeOut.requestFocus()
                }else if (myActivity.isEmpty()){
                    etActivity.error = "Empty"
                    etActivity.requestFocus()
                }else if (remark.isEmpty()){
                    etRemark.error = "Empty"
                    etRemark.requestFocus()
                }else{
                    // save data
                    if(indicator == 1){
                        saveData(nik,kd_dept,category,actDate,time_in,time_out,myActivity,deadline,remark)
                    }else{
                        //	seq,nik,kddept,actdate,timein,timeout,categoryseq,activity,dedaline,remark,status,saveby
                        updateData(seq!!,nik,kd_dept,actDate,time_in,time_out,category,myActivity,deadline,remark,status!!)
                        Log.d("Sequence Number : ", actDate)
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
                val intent = Intent(this@AddDailyActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
        })

    }

    private fun updateData(seq: String, nik: String, kd_dept: String, actDate: String, time_in: String, time_out: String,category: String, myActivity: String, deadline: String,remark:String,status: String) {
        val requestData = RequestUpdateData(seq,nik,kd_dept,actDate,time_in,time_out,category,myActivity,deadline,remark,status,nik)
        val requestUpdateDaily = RequestUpdateDaily(BuildConfig.UserID,BuildConfig.Password,BuildConfig.API_KEY,"adm",requestData)
        ApiConfigDaily.getService().updateDaily(requestUpdateDaily).enqueue(object : Callback<ResponseMessage>{
            override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    val intent = Intent(this@AddDailyActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@AddDailyActivity, responseBody.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
            }

        })
    }

    private fun datePickerDialog(dateFormat: SimpleDateFormat, year: Int, month: Int, dayOfMonth: Int) {
        datePickerDialog = DatePickerDialog(
            this@AddDailyActivity,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Aksi yang dilakukan ketika tanggal dipilih
                // Anda dapat menampilkan tanggal yang dipilih ke dalam TextView atau melakukan aksi lainnya
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.etActDate.setText(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )
    }

    private fun timedialogIn(time: Calendar) {
        timedialog = TimePickerDialog(
            this@AddDailyActivity,
            { _, hourOfDay, minute ->
                // Handle time set
                binding.etTimeIn.setText(String.format("%02d:%02d", hourOfDay, minute))
            },
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            true // 24 hour format
        )
    }

    private fun timedialogOut(time: Calendar) {
        timedialog = TimePickerDialog(
            this@AddDailyActivity,
            { _, hourOfDay, minute ->
                // Handle time set
                binding.etTimeOut.setText(String.format("%02d:%02d", hourOfDay, minute))
            },
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            true // 24 hour format
        )
    }

    private fun saveData(nik: String, kdDept: String, category: String, date: String, timeIn: String, timeOut: String, myActivity: String, deadline: String, remark: String) {
        val requestData = RequestData(nik,kdDept,category,date,timeIn,timeOut,myActivity,deadline,remark,nik)
        val requestAddDaily = RequestAddDaily(BuildConfig.UserID,BuildConfig.Password,BuildConfig.API_KEY,"adm",requestData)
        ApiConfigDaily.getService().addDaily(requestAddDaily).enqueue(object : Callback<ResponseMessage>{
            override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    val intent = Intent(this@AddDailyActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@AddDailyActivity, responseBody.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                Toast.makeText(this@AddDailyActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun DataCategoryFromSpinner() {
        val requestDataCategory = RequestDataCategory("Dropdown","04","Category")
        val requestCategory = RequestCategory(BuildConfig.UserID,BuildConfig.Password,BuildConfig.API_KEY,"ADM",requestDataCategory)
        ApiConfigDaily.getService().getCategory(requestCategory).enqueue(object : Callback<ResponseCategory>{
            override fun onResponse(call: Call<ResponseCategory>, response: Response<ResponseCategory>) {
                if (response.isSuccessful){
                    if (response.body()!=null){
                        val responseBody = response.body()
                        val categoryList = responseBody?.data?.map { it?.category } ?: emptyList()
                        val seqMap = responseBody?.data?.associateBy({ it?.category }, { it?.seq })

                        val spinnerArrayAdapter = ArrayAdapter(this@AddDailyActivity, R.layout.simple_spinner_item, categoryList)
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                        binding.spinnerCategory.adapter = spinnerArrayAdapter

                        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                val selectedCategory = parent.getItemAtPosition(position).toString()
                                val selectedSeq = seqMap?.get(selectedCategory)
                                //kirim selectedSeq ke server
                                category = selectedSeq.toString()
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ResponseCategory>, t: Throwable) {
                Toast.makeText(this@AddDailyActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }



}