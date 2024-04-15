package com.mshauchenka.habbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mshauchenka.habbit.databinding.FragmentMainScreenElementBinding

class HomeFragment : Fragment() {
    private lateinit var vm : MainViewModel
    private var _binding : FragmentMainScreenElementBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenElementBinding.inflate(inflater, container, false)
        val view = binding.root
        vm = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val adapter = RecyclerAdapter(vm)
        binding.tasksRecyclerView.adapter = adapter

        vm.currentTask.observe(viewLifecycleOwner, Observer {task ->
            if (task == null){
                binding.card.visibility = View.GONE
            } else { binding.body.text = task.title }
        })

        val recyclerView = binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }

        binding.mainCardPostponeButton.setOnClickListener {
            binding.card.visibility = View.GONE
        }

        binding.card.setOnClickListener {
            if (vm.currentTask.value != null) {
                startActivity(vm.openCardInBrowser())
            } else {
                Toast.makeText(context, "Current task: ${vm.currentTask.value}", Toast.LENGTH_SHORT).show()}
        }


        binding.mainCardFinishButton.setOnClickListener {
            if (vm.currentTask.value != null) {
                val position = vm.tasks.value?.indexOf(vm.currentTask.value)

                vm.completeCurrentTask()
                recyclerView.adapter?.notifyItemChanged(position!!)

            } else {
                binding.card.visibility = View.GONE
                Toast.makeText(context, "Current task: ${vm.currentTask.value}", Toast.LENGTH_SHORT).show()}
        }

        binding.floatingActionButton.setOnClickListener {
            AddNoteFragment().show(this.parentFragmentManager, "Tag")
        }


        binding.mainCardMixButton.setOnClickListener {
            vm.mixCurrentTask()
        }

        vm.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                if (it.filter { !it.currentTask && !it.completed }.isEmpty()){
                    binding.mainCardMixButton.visibility = View.INVISIBLE
                }
            }
        })

        val callback = object :SwipeToRemove() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    vm.tasks.value?.get(position)?.let { vm.removeTask(it) }
                    recyclerView.adapter?.notifyItemRemoved(position)
                }
            }
        }
        ItemTouchHelper(callback).apply {
            attachToRecyclerView(recyclerView)
        }

        return view
    }

    override fun onResume() {
        vm = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        vm.getCurrentTask()

        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}