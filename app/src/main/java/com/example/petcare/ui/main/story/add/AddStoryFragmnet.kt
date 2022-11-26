package com.example.petcare.ui.main.story.add

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petcare.R
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.BaseResult
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentAddStoryBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.showAlertDialog
import com.example.petcare.helper.showToast
import com.example.petcare.ui.main.story.main.StoryViewModel
import com.example.petcare.utils.StoryUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.InputStream
import java.util.UUID
import kotlin.random.Random
import kotlin.random.nextInt


class AddStoryFragmnet : Fragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var imgUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory(Injection.provideStoryRepository())
    }

    private var lat: Float? = null
    private var lon: Float? = null

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mStorageReference: StorageReference
    private lateinit var mAuth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(requireActivity(),
            REQUIRED_PERMISSION,
            REQUEST_CODE_PERMISSIONS)
        }

        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseReference = FirebaseDatabase.getInstance("https://petcare-2e93d-default-rtdb.firebaseio.com/").reference
        mAuth = FirebaseAuth.getInstance()

        controlDescription()

        _binding?.btnGallery?.setOnClickListener { startGallery() }
        _binding?.btnCamera?.setOnClickListener { startCamera() }
        _binding?.btnUpload?.setOnClickListener { upload() }

    }

    private fun upload() {
        viewModel.postImage(imgUri!!).observe(viewLifecycleOwner){result->
            when(result){
                is BaseResult.Error -> {
                    handleLoading(false)
                    context?.showToast(result.message)
                }
                is BaseResult.Loading -> {
                    handleLoading(true)
                }
                is BaseResult.Success -> {
                    handleLoading(false)
                    handleSuccess(result.data)
                }
            }

        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading){
            _binding?.pbUpload?.visibility = View.VISIBLE
        }else{
            _binding?.pbUpload?.visibility = View.GONE
        }
    }

    private fun handleSuccess(data: Uri) {
        val postId = Random.nextInt().toString()
        val urlAvatar = if (mAuth.currentUser?.photoUrl != null) mAuth.currentUser?.photoUrl.toString() else null
        val desc = _binding?.etDescription?.text.toString()
        val uid = mAuth.currentUser?.uid.toString()
        val name = mAuth.currentUser?.displayName
        val timeStamp = System.currentTimeMillis()
        val story = Story(
            postId, uid, name, urlAvatar, data.toString(), desc, timeStamp,
        )
        viewModel.postStory(story).observe(viewLifecycleOwner){result->
            when(result){
                is BaseResult.Success->{
                    handleLoading(false)
                    findNavController().navigate(R.id.action_addStoryFragmnet_to_action_story)
                    context?.showToast("upload success")
                }
                is BaseResult.Loading -> { handleLoading(true)}
                is BaseResult.Error -> {
                    context?.showToast(result.message)
                    handleLoading(false)
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

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun controlDescription() {
        _binding?.etDescription?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                _binding!!.counterWord.text = "0/100"
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 100){
                    _binding!!.etDescription.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {
                val currentText = s.toString()
                val currentLength = currentText.length
                _binding!!.counterWord.text = "$currentLength/100"
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
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == RESULT_OK){
            val selectedImg: Uri = result?.data?.data as Uri
            imgUri = selectedImg
            _binding?.previewPhoto?.setImageURI(selectedImg)
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
                context?.showAlertDialog("Not getting camera permission")
            }
        }
    }

    private fun allPermissionGranted() = Companion.REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val TAG = "AddStoryFragment"
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}