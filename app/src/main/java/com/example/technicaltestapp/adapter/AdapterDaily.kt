package com.example.technicaltestapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.technicaltestapp.databinding.ItemDailyBinding
import com.example.technicaltestapp.model.daily.DataItemDaily
import com.example.technicaltestapp.view.activity.AddDailyActivity

class AdapterDaily(val activity: Activity ,private val items: List<DataItemDaily>?) :
    RecyclerView.Adapter<AdapterDaily.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemDailyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: DataItemDaily) {

            binding.tvNamaKaryawan.text = daily.namaKaryawan
            binding.tvNamaHead.text = daily.namaHead
            binding.tvDepartment.text = daily.department
            binding.tvCateogry.text = daily.category
            binding.tvActDate.text = daily.actDate

            itemView.setOnClickListener {
                val intent = Intent(activity,AddDailyActivity::class.java)
                intent.putExtra("indicator",2)
                intent.putExtra("dataItemDaily",daily)
                activity.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (items!=null){
            return items.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dailyList = items?.get(position)
        dailyList?.let { holder.bind(it) }
    }
}