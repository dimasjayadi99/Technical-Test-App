package com.example.technicaltestapp.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.technicaltestapp.BuildConfig
import com.example.technicaltestapp.R
import com.example.technicaltestapp.adapter.AdapterDaily
import com.example.technicaltestapp.api.DailyApi.ApiConfigDaily
import com.example.technicaltestapp.api.DailyApi.SharedPrefManager
import com.example.technicaltestapp.databinding.FragmentDailyBinding
import com.example.technicaltestapp.model.daily.RequestDataDaily
import com.example.technicaltestapp.model.daily.RequestGetDaily
import com.example.technicaltestapp.model.daily.ResponseDaily
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyFragment : Fragment() {
    // deklarasi lateinit var binding
    private lateinit var binding: FragmentDailyBinding
    // deklarasi var status yang dapat bernilai null
    private var status = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        val sharedPrefManager = SharedPrefManager
        val nik = sharedPrefManager.getInstance(requireContext()).user.nik
        updateView(nik.toString(),"")
        binding.imgSort.setOnClickListener {
            // ambil view dari layout view_sort untuk alertdialog
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.view_sort, null)
            // set value untuk spinner
            val spinner = dialogView.findViewById<Spinner>(R.id.sp_status)
            val data = arrayListOf("Semua", "Baru", "On Progress", "Selesai")
            val itemValueMap = hashMapOf(
                "Semua" to "",
                "Baru" to "N",
                "On Progress" to "P",
                "Selesai" to "D"
            )
            // set adapter spinner
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    val selectedValue = itemValueMap[selectedItem]
                    // memperbarui nilai status sesuai pilihan pengguna
                    status = selectedValue.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    status = "" // menghapus nilai status jika pengguna tidak memilih apapun
                }
            }
            // alert dialog ketika icon sort diklik
            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("Filter Data")
                .setMessage("Filter data berdasarkan status")
                .setPositiveButton("Terapkan") { dialog, _ ->
                    dialog.dismiss()
                    updateView(nik!!, status) // memanggil fungsi updateView() dengan parameter query dan status
                }
                .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                .setView(dialogView)
                .setCancelable(true)
                .create()

            alertDialog.show()
        }

        return binding.root
    }

    // fungsi untuk memperbarui tampilan UI dengan data dari API
    private fun updateView(p0: String, status: String?) {
        // buat objek RequestDataDaily yang akan digunakan sebagai body request API
        val requestDaily = RequestDataDaily(p0, "", "", status)
        // buat objek RequestHeader yang berisi informasi authentikasi dan request body
        val requestGetDaily = RequestGetDaily(BuildConfig.UserID, BuildConfig.Password, BuildConfig.API_KEY, "adm", requestDaily)
        // panggil endpoint API untuk mendapatkan data berdasarkan request header dan request daily
        ApiConfigDaily.getService().getDaily(requestGetDaily).enqueue(object : Callback<ResponseDaily> {
            // callback saat response berhasil diterima
            override fun onResponse(call: Call<ResponseDaily>, response: Response<ResponseDaily>) {
                binding.apply {
                    // cek apakah response berhasil
                    if (response.isSuccessful) {
                        // cek apakah response memiliki body
                        if (response.body() != null) {
                            // ambil objek ResponseDaily dari body response
                            val responseBody = response.body()
                            // ambil list data harian dari objek ResponseDaily
                            val responseList = responseBody?.data

                            // filter list data harian berdasarkan status jika parameter status tidak kosong
                            val filteredList = if (status.isNullOrEmpty()) responseList else {
                                responseList?.filter { it.status == status }
                            }

                            // buat adapter untuk menampilkan list data
                            val adapterDaily = AdapterDaily(requireActivity(),filteredList)
                            recyclerView.apply {
                                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                                setHasFixedSize(true)
                                adapterDaily.notifyDataSetChanged()
                                adapter = adapterDaily
                            }
                            textView3.text = "Berikut data untuk NIK: $p0"

                            // tampilkan pesan jika tidak ada data yang ditemukan
                            if (filteredList != null && filteredList.isNotEmpty()) {
                                tvNotFound.visibility = View.GONE
                                binding.textView3.visibility = View.VISIBLE
                            } else {
                                tvNotFound.visibility = View.VISIBLE
                                binding.textView3.visibility = View.GONE
                            }
                        }
                    }
                }
            }

            // callback saat terjadi kesalahan pada saat melakukan request
            override fun onFailure(call: Call<ResponseDaily>, t: Throwable) {
                // tampilkan pesan kesalahan
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}