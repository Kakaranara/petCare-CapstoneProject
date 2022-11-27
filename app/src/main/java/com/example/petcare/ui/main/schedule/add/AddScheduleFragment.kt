package com.example.petcare.ui.main.schedule.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.petcare.databinding.FragmentAddScheduleBinding


class AddScheduleFragment : Fragment() {
    private var _binding: FragmentAddScheduleBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addScheduleToolbar.setupWithNavController(findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val TAG = "AddScheduleFragment"
    }
}