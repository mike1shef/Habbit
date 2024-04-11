package com.mshauchenka.habbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshauchenka.habbit.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")

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