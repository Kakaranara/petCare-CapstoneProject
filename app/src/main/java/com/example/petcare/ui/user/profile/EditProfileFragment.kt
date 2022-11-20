package com.example.petcare.ui.user.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.petcare.databinding.FragmentEditProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class EditProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editProfileToolbar.setupWithNavController(findNavController(), null)
        binding.btnEditImage.setOnClickListener(this)
        binding.btnConfirmEdit.setOnClickListener(this)

        Firebase.auth.currentUser?.let { user ->
            Glide.with(requireActivity())
                .load(user.photoUrl)
                .into(binding.circleImageEdit)
            binding.etEditName.setText(user.displayName)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnEditImage -> {
                Snackbar.make(requireView(), "TEST", Snackbar.LENGTH_SHORT).show()
            }
            binding.btnConfirmEdit -> {
                val update = userProfileChangeRequest {
                    displayName = binding.etEditName.text.toString()
                    //TODO : update image from gallery
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}