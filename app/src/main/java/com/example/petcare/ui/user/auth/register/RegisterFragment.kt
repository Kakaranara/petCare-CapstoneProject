package com.example.petcare.ui.user.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.databinding.FragmentRegisterBinding
import com.example.petcare.helper.Async
import com.example.petcare.helper.showToast
import com.example.petcare.utils.AuthUtil

class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)

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
                    AuthUtil.isRegisterValid(email, password, confirmPassword, name)
                    viewModel.registerWithEmail(email, password, name).observe(viewLifecycleOwner) {
                        when (it) {
                            is Async.Error -> {
                                binding.btnRegister.isEnabled = true
                                binding.registerProgress.visibility = View.INVISIBLE
                                showToast(it.error)
                            }
                            is Async.Loading -> {
                                binding.btnRegister.isEnabled = false
                                binding.registerProgress.visibility = View.VISIBLE
                            }
                            is Async.Success -> {
                                binding.btnRegister.isEnabled = true
                                binding.registerProgress.visibility = View.INVISIBLE
                                showToast("Account successfully created.")
                                val go =
                                    RegisterFragmentDirections.actionGlobalLoginFragment()
                                findNavController().navigate(go)
                            }
                        }
                    }
                } catch (e: Exception) {
                    showToast(e.message)
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