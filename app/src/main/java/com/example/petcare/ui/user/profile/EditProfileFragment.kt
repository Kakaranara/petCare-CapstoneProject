package com.example.petcare.ui.user.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.petcare.databinding.FragmentEditProfileBinding
import com.example.petcare.helper.Async
import com.example.petcare.helper.showToast

class EditProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EditProfileViewModel>()
    private var uri: Uri? = null

    private val intentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uri = result.data?.data
                Glide.with(requireActivity())
                    .load(uri)
                    .into(binding.circleImageEdit)
            }
        }

    companion object {
        const val TAG = "Edit Profile Fragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editProfileToolbar.setupWithNavController(findNavController(), null)
        binding.btnEditImage.setOnClickListener(this)
        binding.btnConfirmEdit.setOnClickListener(this)

        viewModel.getUser()?.let { user ->
            uri = user.photoUrl
            Glide.with(requireActivity())
                .load(uri)
                .into(binding.circleImageEdit)
            binding.etEditName.setText(user.displayName)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnEditImage -> {
                Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }.also { intentGallery.launch(Intent.createChooser(it, "im here bro")) }
            }
            binding.btnConfirmEdit -> {
                val name = binding.etEditName.text.toString()
                viewModel.updateProfileData(name, uri).observe(viewLifecycleOwner){
                    when(it){
                        is Async.Error -> {
                            binding.btnConfirmEdit.isEnabled = true
                            showToast("Failed. ${it.error}")
                        }
                        Async.Loading -> {
                            binding.btnConfirmEdit.isEnabled = false
                        }
                        is Async.Success -> {
                            showToast("Success")
                            findNavController().popBackStack()
                        }
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
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}