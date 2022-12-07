package com.example.petcare.ui.user.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.R
import com.example.petcare.databinding.FragmentLoginBinding
import com.example.petcare.helper.Async
import com.example.petcare.helper.showToast
import com.example.petcare.utils.AuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: GoogleSignInClient

    private val viewModel by viewModels<LoginViewModel>()

    //? used for google one tap sign-in
    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d(TAG, "firebase auth: $ ")
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: Exception) {
                    Log.d(TAG, "Google signin failed: ")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: SOMETHINGS WRONG! $e")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.btnLoginWithGoogle.setOnClickListener(this)

        //? also for google oneTap sign-in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        oneTapClient = GoogleSignIn.getClient(requireActivity(), gso)

        auth = Firebase.auth
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnToRegister -> {
                val go = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(go)
            }
            binding.btnLoginWithGoogle -> {
                oneTapSignIn()
            }
            binding.btnLogin -> {
                val email = binding.etLoginEmail.text.toString()
                val password = binding.etLoginPassword.text.toString()
                try {
                    AuthUtil.isLoginValid(email, password)
                    loginWithEmailAndPassword(email, password)
                } catch (e: Exception) {
                    showToast(e.message)
                }
            }
        }
    }

    private fun loginWithEmailAndPassword(email: String, password: String) {
        viewModel.loginWithEmail(email, password).observe(viewLifecycleOwner) {
            when (it) {
                is Async.Error -> {
                    binding.btnLogin.isEnabled = true
                    binding.loginProgress.visibility = View.INVISIBLE
                    showToast(it.error)
                }
                is Async.Loading -> {
                    binding.btnLogin.isEnabled = false
                    binding.loginProgress.visibility = View.VISIBLE
                }
                is Async.Success -> {
                    binding.btnLogin.isEnabled = true
                    binding.loginProgress.visibility = View.INVISIBLE
                    showToast("Success")
                    val go = LoginFragmentDirections.actionLoginFragmentToActionHome()
                    findNavController().navigate(go)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModel.oneTapGoogleLogin(credential).observe(viewLifecycleOwner) {
            when (it) {
                is Async.Loading -> {
                    binding.loginProgress.visibility = View.VISIBLE
                }
                is Async.Success -> {
                    val name = it.data.name ?: "No Name"
                    showToast("Hello, $name", true)
                    val go = LoginFragmentDirections.actionLoginFragmentToActionHome()
                    findNavController().navigate(go)
                }
                is Async.Error -> {
                    showToast(it.error)
                }
            }
        }
    }

    private fun oneTapSignIn() {
        val signinIntent = oneTapClient.signInIntent
        resultLauncher.launch(signinIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "Login Fragment"
    }
}