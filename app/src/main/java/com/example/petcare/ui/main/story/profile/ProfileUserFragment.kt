package com.example.petcare.ui.main.story.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.petcare.R
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentProfileUserBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.Async
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class ProfileUserFragment : Fragment() {
    private var _binding: FragmentProfileUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserPostAdapter
    private lateinit var mAuth: FirebaseAuth
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory(Injection.provideStoryRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = arguments?.getString(ID)
        mAuth = FirebaseAuth.getInstance()

        setupRecylerView()
        getStoryByUid(uid)



    }

    private fun setupRecylerView() {
        _binding?.rvProfile?.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            setHasFixedSize(true)
        }
    }

    private fun getStoryByUid(uid: String?) {
        viewModel.getStoryById(uid.toString()).observe(viewLifecycleOwner){result->
            when(result){
                is Async.Loading -> handleLoading(true)
                is Async.Error -> {
                    handleLoading(false)
                    Log.e(TAG, "onfailure: ${result.error}")
                }
                is Async.Success->{
                    handleLoading(false)
                    val size = result.data.story!!.size
                    Log.d(TAG, "${result.data.story}")
                    setUpProfile(result.data.story, size)

                }
            }
        }
    }

    private fun setUpProfile(data: List<Story>?, size: Int) {
        if (size == 0 ){
            _binding?.noData?.visibility = View.VISIBLE
            _binding?.username?.text = mAuth.currentUser!!.displayName
            _binding?.tvPost?.text = "Your post";
            Glide.with(requireContext())
                .load(mAuth.currentUser!!.photoUrl)
                .circleCrop()
                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground).error(R.drawable.ic_avatar_24))
                .into(binding.photo)
            _binding?.profileToolbar?.apply {
                val currentname = mAuth.currentUser!!.displayName
                setupWithNavController(findNavController(), null)
                title = "Your Profile"
            }
        }else{
            _binding?.noData?.visibility = View.GONE
            adapter = UserPostAdapter()
            adapter.submitList(data)
            _binding?.rvProfile?.adapter = adapter

            data?.forEach {
                val currentname = mAuth.currentUser!!.displayName
                _binding?.username?.text = it.name
                _binding?.tvPost?.text = if (it.name == currentname) "Your post" else "${it.name}' post"
                Glide.with(requireContext())
                    .load(it.avatarUrl)
                    .circleCrop()
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground).error(R.drawable.ic_avatar_24))
                    .into(binding.photo)
                _binding?.profileToolbar?.apply {
                    setupWithNavController(findNavController(), null)
                    title = if (it.name == currentname) "Your Profile" else "${it.name }'s Profile"
                }
            }
        }

    }

    private fun handleLoading(loading: Boolean) {
        _binding?.pbProfile?.apply {
            isIndeterminate = loading
            if (!loading){
                progress = 0
                visibility = View.GONE
            }else{
                visibility = View.VISIBLE
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        _binding = FragmentProfileUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "ProfileUserFragment"
        const val ID = "id"
    }
}