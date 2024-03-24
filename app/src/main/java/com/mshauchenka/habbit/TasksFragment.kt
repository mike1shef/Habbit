package com.mshauchenka.habbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshauchenka.habbit.databinding.TasksFragmentBinding



class TasksFragment : Fragment() {
    private var _binding :TasksFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

       val application = requireNotNull(this.activity).application
        val dao = TaskDataBase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val vm = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val adapter = RecyclerAdapter(vm)

        binding.tasksRecyclerView.adapter = adapter

        val recyclerView = binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }

        vm.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}