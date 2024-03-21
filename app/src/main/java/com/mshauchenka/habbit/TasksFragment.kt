package com.mshauchenka.habbit

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mshauchenka.habbit.databinding.TasksFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn


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
        val vm = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)

        val adapter = RecyclerAdapter()
        binding.tasksRecyclerView.adapter = adapter

        val recyclerView = binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }

        vm.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                recyclerView.adapter.apply {
                    adapter.data = it
                }
            }
        })

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_addNoteFragment)
        }

        return view
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}