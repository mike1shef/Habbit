package com.mshauchenka.habbit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshauchenka.habbit.databinding.FragmentMainScreenElementBinding

class HomeFragment : Fragment() {
    private var _binding : FragmentMainScreenElementBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainScreenElementBinding.inflate(inflater, container, false)
        val view = binding.root
        val vm = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        vm.currentTask.observe(viewLifecycleOwner, Observer {task ->
            task?.let {
                binding.body.text = it.title
            }
        })

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
                vm.completeCurrentTask()
                findNavController().navigate(R.id.action_mainElementFragment_to_taskCompletedFragment)
            } else {
                Toast.makeText(context, "Current track: ${vm.currentTask.value}", Toast.LENGTH_SHORT).show()}
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainElementFragment_to_addNoteFragment)
        }


        binding.mainCardMixButton.setOnClickListener {
            vm.mixCurrentTask()
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}