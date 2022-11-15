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
import com.example.petcare.utils.AuthUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
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
                val name = binding.etName.text.toString()
                val email = binding.etRegisterEmail.text.toString()
                val password = binding.etRegisterPassword.text.toString()
                val confirmPassword = binding.etRegisterConfirmPassword.text.toString()
                try {
                    AuthUtil.isRegisterValid(email, password,confirmPassword, name)
                    registerUser(email, password, name)
                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun registerUser(email: String, password: String, name: String) {
        binding.btnRegister.isEnabled = false
        binding.registerProgress.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isComplete) {
                    binding.btnRegister.isEnabled = true
                    binding.registerProgress.visibility = View.INVISIBLE
                }
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdate = userProfileChangeRequest {
                        displayName = name
                    }
                    user?.updateProfile(profileUpdate)
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
                        "${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "onClick: Fail login. Exception : ${task.exception}")
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