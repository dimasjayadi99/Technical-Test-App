package com.example.technicaltestapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.technicaltestapp.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private var _binding : ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    private val timeload : Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        Handler(mainLooper).postDelayed({
           startActivity(Intent(this,InputAuthActivity::class.java))
           finish()
        },timeload)

        setContentView(binding.root)
    }
}