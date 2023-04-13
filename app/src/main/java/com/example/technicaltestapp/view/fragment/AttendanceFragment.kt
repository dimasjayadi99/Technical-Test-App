package com.example.technicaltestapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.technicaltestapp.adapter.AdapterAttendance
import com.example.technicaltestapp.databinding.FragmentAttendanceBinding
import com.example.technicaltestapp.model.attendance.AttendanceDatabase
import com.example.technicaltestapp.model.attendance.AttendanceRepository
import com.example.technicaltestapp.viewmodel.AttendanceViewModel
import com.example.technicaltestapp.viewmodel.AttendanceViewModelFactory

class AttendanceFragment : Fragment() {

    private var _binding : FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var attendanceViewModel : AttendanceViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAttendanceBinding.inflate(layoutInflater)

        val attendanceDao = AttendanceDatabase.getDatabase(requireContext()).attendanceDao()
        val attendanceRepository = AttendanceRepository(attendanceDao)
        attendanceViewModel = ViewModelProvider(this, AttendanceViewModelFactory(attendanceRepository))[AttendanceViewModel::class.java]

        binding.apply {
            svAttendance.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (p0 != null){
                        searchAttendance(p0)
                    }
                    return true
                }

                override fun onQueryTextChange(newText : String?): Boolean {
                    if (newText != null){
                        searchAttendance(newText)
                    }
                    return true
                }

            })
        }

        updateView()

        return binding.root
    }

    private fun updateView() {
        attendanceViewModel.attendanceList.observe(viewLifecycleOwner, Observer { itemlist->
            itemlist.let {
                val adapterAttendance = AdapterAttendance(requireActivity(),it,attendanceViewModel)
                binding.rvAttendance.apply {
                    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    setHasFixedSize(true)
                    adapterAttendance.notifyDataSetChanged()
                    adapter = adapterAttendance
                }
            }
        })
    }

    private fun searchAttendance(p0: String) {
        attendanceViewModel.searchAttendance("%$p0%").observe(this) { listCatatan ->
            listCatatan?.let {
                    val adapterAttendance = AdapterAttendance(requireActivity(),it,attendanceViewModel)
                    binding.rvAttendance.apply {
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                        setHasFixedSize(true)
                        adapterAttendance.notifyDataSetChanged()
                        adapter = adapterAttendance
                }
            }
        }
    }

}