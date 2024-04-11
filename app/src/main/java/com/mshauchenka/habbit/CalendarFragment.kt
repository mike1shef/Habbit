package com.mshauchenka.habbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshauchenka.habbit.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.util.Calendar

class CalendarFragment : Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater,container, false)
        val view = binding.root
        vm = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val adapter = RecyclerAdapter(vm)
        binding.calendarRecyclerView.adapter = adapter
        val recyclerView = binding.calendarRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }

        val calendar = Calendar.getInstance().apply {
            this.set(LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
        }

        binding.calendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            val date = LocalDate.of(year, month +1, dayOfMonth)
            vm.getTasksByDate(date).observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        })

        return view

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}