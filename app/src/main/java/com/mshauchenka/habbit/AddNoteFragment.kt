package com.mshauchenka.habbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mshauchenka.habbit.databinding.FragmentAddNoteBinding

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
            findNavController().navigate(R.id.action_addNoteFragment_to_mainElementFragment)
        }

        binding.saveButton.setOnClickListener {
            val textFieldText = binding.editNote.editText?.text
            if (textFieldText.isNullOrBlank()){
                Toast.makeText(context, "Enter a note", Toast.LENGTH_SHORT).show()
            } else {
                text = binding.editNote.editText!!.text.toString()
                vm.addTask(text)
                findNavController().navigate(R.id.mainElementFragment)
            }
        }


        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}