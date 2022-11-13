package com.example.petcare.ui.user.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.petcare.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Code Here
         */
        binding.btnToLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnToLogin -> {
                val go = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(go)
            }
            binding.btnRegister -> {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "Register Fragment"
    }
}