package com.example.technicaltestapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.technicaltestapp.BuildConfig
import com.example.technicaltestapp.api.DailyApi.ApiConfigDaily
import com.example.technicaltestapp.api.DailyApi.SharedPrefManager
import com.example.technicaltestapp.databinding.ActivityInputAuthBinding
import com.example.technicaltestapp.model.daily.RequestDataDaily
import com.example.technicaltestapp.model.daily.RequestGetDaily
import com.example.technicaltestapp.model.daily.ResponseDaily
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputAuthActivity : AppCompatActivity() {
    private var _binding : ActivityInputAuthBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInputAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSimpan.setOnClickListener{
                // buat objek RequestDataDaily yang akan digunakan sebagai body request API
                val nik = etNik.text.toString()
                if (nik.isEmpty()){
                    etNik.error = "Empty"
                    etNik.requestFocus()
                }else{
                    val requestDaily = RequestDataDaily(nik, "", "", "")
                    // buat objek RequestHeader yang berisi informasi authentikasi dan request body
                    val requestGetDaily = RequestGetDaily(BuildConfig.UserID, BuildConfig.Password, BuildConfig.API_KEY, "adm", requestDaily)
                    ApiConfigDaily.getService().getDaily(requestGetDaily).enqueue(object : Callback<ResponseDaily>{
                        override fun onResponse(call: Call<ResponseDaily>, response: Response<ResponseDaily>) {
                            if (response.isSuccessful){
                                val responseData = response.body()?.data
                                if (responseData!=null){
                                    SharedPrefManager.getInstance(applicationContext).saveUser(response.body())
                                    val intent = Intent(applicationContext,MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                    Toast.makeText(this@InputAuthActivity, "NIK ditemukan", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this@InputAuthActivity, "NIK tidak ditemukan", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this@InputAuthActivity, "Terjadi kesalahan saat menghubungkan data", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseDaily>, t: Throwable) {
                            Toast.makeText(this@InputAuthActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}