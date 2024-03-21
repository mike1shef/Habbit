package com.mshauchenka.habbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshauchenka.habbit.databinding.FragmentAddNoteBinding
import com.mshauchenka.habbit.databinding.TasksFragmentBinding

class AddNoteFragment : Fragment() {
    private var _binding : FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        val view = binding.root
        var text = ""

        val application = requireNotNull(this.activity).application
        val dao = TaskDataBase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val vm = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)

        binding.close.setOnClickListener {
            findNavController().navigate(R.id.tasksFragment)
        }

        binding.saveButton.setOnClickListener {
            text = binding.editNote.editText!!.text.toString()
            vm.addTask(text)
            findNavController().navigate(R.id.tasksFragment)
        }


        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}