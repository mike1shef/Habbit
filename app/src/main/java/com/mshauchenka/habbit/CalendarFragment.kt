package com.mshauchenka.habbit

import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshauchenka.habbit.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm : MainViewModel

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater,container, false)
        val view = binding.root
        vm = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val adapter = RecyclerAdapter(vm)

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")
        val firstCompletedTaskDate = vm.minimalDate
        val calendar : Calendar = Calendar.getInstance()



        binding.calendarView.maxDate = calendar.timeInMillis
        if (firstCompletedTaskDate !== null){
        calendar.set(
                firstCompletedTaskDate.year,
                firstCompletedTaskDate.monthValue-1,
                firstCompletedTaskDate.dayOfMonth
        )
        } else {
            val localDate = LocalDate.now()
            calendar.set(localDate.year, localDate.monthValue-1, localDate.dayOfMonth)}

        binding.calendarView.minDate = calendar.timeInMillis

        binding.calendarRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        vm.date.observe(viewLifecycleOwner) { date ->
            val text = String.format(
                resources.getString(R.string.formatted_day),
                date.format(dateFormatter)
            )
            binding.calendarText.text = text

            vm.getTasksByDate(date).observe(viewLifecycleOwner) {
                it?.let {
                    adapter.submitList(it)
                }
            }
        }

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            vm.date.value = LocalDate.of(year, month + 1, dayOfMonth)
        }

        return view
    }

    override fun onDestroyView() {
        _binding = null
        vm.date.value = LocalDate.now()
        super.onDestroyView()
    }
}