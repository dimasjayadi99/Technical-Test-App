package com.example.technicaltestapp.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.technicaltestapp.R
import com.example.technicaltestapp.api.DailyApi.SharedPrefManager
import com.example.technicaltestapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setup bottom navigation

        val navController = findNavController(R.id.nav_fragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.floating.setOnClickListener {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val currentDestination = navController.currentDestination

            val intent = when (currentDestination?.id) {
                R.id.dailyFragment -> Intent(this, AddDailyActivity::class.java)
                R.id.attendanceFragment -> Intent(this, AddAttendanceActivity::class.java)
                else -> null
            }
            intent?.putExtra("indicator",1)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.tvLogout.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("Keluar Akun")
                .setMessage("Anda yakin ingin keluar akun?")
                .setPositiveButton("Keluar"){dialog,_->
                    SharedPrefManager.getInstance(this).clear()
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(Intent(this,InputAuthActivity::class.java))
                    finish()
                    dialog.dismiss()
                }
                .setNegativeButton("Batal"){dialog,_->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .create()
                .show()
        }

    }
}