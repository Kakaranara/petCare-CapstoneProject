package com.example.petcare.ui.user.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.petcare.databinding.FragmentEditProfileBinding
import com.google.android.material.snackbar.Snackbar

class EditProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editProfileToolbar.setupWithNavController(findNavController(), null)

        binding.btnEditImage.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnEditImage -> {
                Snackbar.make(requireView(), "TEST", Snackbar.LENGTH_SHORT).show()
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