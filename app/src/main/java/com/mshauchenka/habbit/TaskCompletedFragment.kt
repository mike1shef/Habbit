package com.mshauchenka.habbit

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mshauchenka.habbit.databinding.FragmentMainScreenElementBinding
import com.mshauchenka.habbit.databinding.FragmentTaskCompletedBinding
import kotlin.contracts.contract

class TaskCompletedFragment : Fragment() {

    private var _binding : FragmentTaskCompletedBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskCompletedBinding.inflate(inflater, container, false)
        val view = binding.root

        val source = ImageDecoder.createSource(resources, R.drawable.task_done)

        Thread(Runnable {
            val drawable = ImageDecoder.decodeDrawable(source)
            binding.image.post{
                binding.image.setImageDrawable(drawable)
                (drawable as AnimatedImageDrawable).start()
            }
        }).start()




        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

