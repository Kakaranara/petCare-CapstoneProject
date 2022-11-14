package com.example.petcare.ui.user.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petcare.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener(this)
        auth = Firebase.auth
        auth.currentUser?.let {
            Glide.with(requireActivity())
                .load(it.photoUrl)
                .into(binding.circleImageView)
            binding.tvProfileName.text = it.displayName
            binding.tvProfileEmail.text = it.email
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnLogout -> {
                Firebase.auth.signOut()
                val go = ProfileFragmentDirections.actionGlobalLoginFragment()
                findNavController().navigate(go)
                Log.d(TAG, "onClick: LOGOUT SUCCESS")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "Profile Fragment"
    }
}