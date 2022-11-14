package com.example.petcare.ui.user.auth.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petcare.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        auth = Firebase.auth

    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnToLogin -> {
                val go = RegisterFragmentDirections.actionGlobalLoginFragment()
                findNavController().navigate(go)
            }
            binding.btnRegister -> {
                binding.btnRegister.isEnabled = false
                binding.registerProgress.visibility = View.VISIBLE
                val email = binding.etRegisterEmail.text.toString()
                val password = binding.etRegisterPassword.text.toString()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isComplete) {
                            binding.btnRegister.isEnabled = true
                            binding.registerProgress.visibility = View.INVISIBLE
                        }
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireActivity(),
                                "Account successfully created.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "onClick: Login success.")
                            val go =
                                RegisterFragmentDirections.actionGlobalLoginFragment()
                            findNavController().navigate(go)
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Failed. ${task.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "onClick: Fail login. Exception : ${task.exception}")
                        }
                    }
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