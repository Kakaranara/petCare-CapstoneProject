package com.example.petcare.ui.main.story.add

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentAddStoryBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.Async
import com.example.petcare.helper.showAlertDialog
import com.example.petcare.helper.showToast
import com.example.petcare.utils.GeneratePostId
import com.example.petcare.utils.StoryUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.File


class AddStoryFragment : Fragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var imgUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory(Injection.provideStoryRepository())
    }
    private lateinit var mAuth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(requireActivity(),
            REQUIRED_PERMISSION,
            REQUEST_CODE_PERMISSIONS)
        }
        mAuth = FirebaseAuth.getInstance()

        controlDescription()
        setupToolbar()
        binding.tvCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { upload() }
        binding.tvPickPhoto.setOnClickListener { startPickPhoto() }
        binding.previewPhoto.setOnClickListener {
            startPickPhoto()
        }


    }

    private fun setupToolbar() {
        binding.addstorytoolbar.apply {
            setupWithNavController(findNavController() , null )
            title = getString(R.string.add_postingan)
        }
    }

    private val intentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imgUri = result.data?.data
                Glide.with(requireActivity())
                    .load(imgUri)
                    .into(binding.previewPhoto)
                if (imgUri != null){
                    pickerVisible(true)
                }else{
                    pickerVisible(false)
                }
            }
        }

    private fun startPickPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }.also { intentGallery.launch(Intent.createChooser(it, "Select the photo")) }
        }
    }

    private fun upload() {
        lifecycleScope.launch {
            val name = mAuth.currentUser?.displayName.toString()
            viewModel.postImage(name, imgUri!!).observe(viewLifecycleOwner){result->
                when(result){
                    is Async.Error -> {
                        handleLoading(false)
                        Log.e(TAG, "onFailure: ${result.error}")
                    }
                    is Async.Loading -> {
                        handleLoading(true)
                        binding.btnUpload.isEnabled = false
                    }
                    is Async.Success -> {
                        handleLoading(false)
                        handleSuccess(result.data)
                    }
                }

            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        _binding?.pbUpload?.apply {
            isIndeterminate = isLoading
            if (!isLoading){
                progress = 0
                visibility = View.GONE
            }else{
                visibility = View.VISIBLE
            }
        }
    }

    private fun handleSuccess(data: Uri) {
        val postId = GeneratePostId.postIdRandom()
        val urlAvatar = if (mAuth.currentUser?.photoUrl != null) mAuth.currentUser?.photoUrl.toString() else null
        val desc = binding.etDescription.text.toString()
        val uid = mAuth.currentUser?.uid.toString()
        val name = mAuth.currentUser?.displayName
        val timeStamp = System.currentTimeMillis()
        val story = Story(
            postId, uid, name, urlAvatar, data.toString(), desc, timeStamp,
        )
        lifecycleScope.launch {
            viewModel.postStory(story).observe(viewLifecycleOwner){result->
                when(result){
                    is Async.Success->{
                        handleLoading(false)
                        findNavController().popBackStack()
                        context?.showToast(getString(R.string.upload_success_text))
                    }
                    is Async.Loading -> {
                        handleLoading(true)
                        _binding?.btnUpload?.isEnabled = false
                    }
                    is Async.Error -> {
                        Log.e(TAG, "onFailure: ${result.error}")
                        handleLoading(false)
                    }
                }
            }
        }
    }


    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(activity?.packageManager!!)

        StoryUtil.createCustomTempFile(requireContext()).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.petcare",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherCameraIntent.launch(intent)
        }
    }

    private fun controlDescription() {
        _binding?.etDescription?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                _binding!!.counterWord.text = buildString {
                    append(getString(R.string.description_counter_text))
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! == 100){
                    showToast(getString(R.string.description_max_char))
                }
            }
            override fun afterTextChanged(s: Editable?) {
                val currentText = s.toString()
                val currentLength = currentText.length
                _binding!!.counterWord.text = buildString {
                    append(currentLength)
                    append(getString(R.string.max_char))
                }
            }
        })
    }


    private val launcherCameraIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if (result.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            imgUri = Uri.fromFile(myFile)
            val imgResult = BitmapFactory.decodeFile(myFile.path)
            _binding?.previewPhoto?.setImageBitmap(imgResult)
            if (imgResult != null){
                pickerVisible(true)
            }else{
                pickerVisible(false)
            }
        }
    }

    /**
     * *PickVisualMedia -> to get Photo from phone
     */
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri!=null){
            imgUri = uri
            _binding?.previewPhoto?.setImageURI(uri)
            pickerVisible(true)
        }else{
            pickerVisible(false)
        }
    }

    private fun pickerVisible(isVisible: Boolean) {
        if (isVisible){
            _binding?.tvCamera?.visibility = View.GONE
            _binding?.tvPickPhoto?.visibility = View.GONE
        }else{
            _binding?.tvCamera?.visibility = View.VISIBLE
            _binding?.tvPickPhoto?.visibility = View.VISIBLE

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionGranted()){
                // ? using extention function
                context?.showAlertDialog(getString(R.string.not_get_permission))
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val TAG = "AddStoryFragment"
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}