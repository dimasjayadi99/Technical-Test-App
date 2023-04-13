package com.example.technicaltestapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.technicaltestapp.databinding.ItemAttendanceBinding
import com.example.technicaltestapp.model.attendance.Attendance
import com.example.technicaltestapp.viewmodel.AttendanceViewModel

class AdapterAttendance(private val activity: Activity,var list : List<Attendance>,private val viewModel: AttendanceViewModel) : RecyclerView.Adapter<AdapterAttendance.MyViewHolder>() {
    inner class MyViewHolder(private val binding : ItemAttendanceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(attendance: Attendance){
            binding.tanggal.text = attendance.tanggalKehadiran
            if (attendance.telat.toInt() < 0) {
                binding.status.text = "Terlambat"
            } else if (attendance.pulangAwal.toInt() < 0) {
                binding.status.text = "Pulang Awal"
            } else if (attendance.telat.toInt() >= 0 && attendance.pulangAwal.toInt() >= 0) {
                binding.status.text = "Tepat Waktu"
            }
            val masuk = attendance.jamMasuk
            val pulang = attendance.jamPulang
            binding.waktu.text = "$masuk - $pulang WIB"

            itemView.setOnClickListener {
                viewModel.onItemClickListener(activity,attendance)
            }

            itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Hapus data")
                builder.setMessage("Anda yakin ingin hapus data?")
                builder.setPositiveButton("Hapus"){dialog,_->
                    viewModel.deleteAttendance(attendance.id)
                    dialog.dismiss()
                }
                builder.setNegativeButton("Batal"){dialog,_->
                    dialog.dismiss()
                }
                builder.setCancelable(true)
                builder.create()
                builder.show()
                true
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val attendanceList = list[position]
        holder.bind(attendanceList)
    }
}