package com.example.petcare.ui.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.databinding.FragmentScheduleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ScheduleFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<ScheduleViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fbAddSchedule.setOnClickListener(this)

        auth = Firebase.auth

        viewModel.listenForDataChanges().observe(viewLifecycleOwner){

        }

    }

    override fun onClick(view: View) {
        when (view) {
            binding.fbAddSchedule -> {
                val go = ScheduleFragmentDirections.actionActionScheduleToAddScheduleFragment()
                findNavController().navigate(go)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.unRegister()
    }

    companion object {
        const val TAG = "Schedule Fragment"
    }
}