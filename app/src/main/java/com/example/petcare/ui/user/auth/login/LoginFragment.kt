package com.example.petcare.ui.user.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petcare.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "signinWithCredential: Success")
                                    val user = auth.currentUser
                                    updateUI(user)
                                } else {
                                    Log.d(TAG, "signinWithCredential: Fail ")
                                    updateUI(null)
                                }
                            }
                    }
                    else -> {
                        Toast.makeText(requireActivity(), "token is null", Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, "Err: No ID token!")
                    }
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
//
//        oneTapClient = Identity.getSignInClient(requireActivity())
//        signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.default_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build()
//            ).build()

        auth = Firebase.auth
//        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
//            override fun onStart(owner: LifecycleOwner) {
//                super.onStart(owner)
//                val currentUser = auth.currentUser
//                updateUI(currentUser)
//            }
//        })

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Log.d(TAG, "updateUI: user has login.")
            val go = LoginFragmentDirections.actionLoginFragmentToActionHome()
            findNavController().navigate(go)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnToRegister -> {
                val go = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(go)
            }
            binding.btnLoginWithGoogle -> {

            }
            binding.btnLogin -> {
                val email = binding.etLoginEmail.text.toString()
                val password = binding.etLoginPassword.text.toString()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "onClick: login success")
                        Toast.makeText(requireActivity(), "success", Toast.LENGTH_SHORT).show()
                        val go = LoginFragmentDirections.actionLoginFragmentToActionHome()
                        findNavController().navigate(go)
                    } else {
                        Log.d(TAG, "onClick: login failed. exception : ${task.exception}")
                        Toast.makeText(
                            requireActivity(),
                            "failed. ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
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