package com.example.petcare.ui.main.story.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.R
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentStoryBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.Async
import com.example.petcare.helper.showToast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll


class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyList: ArrayList<Story>
    private lateinit var mAuth: FirebaseAuth
    private var isLiked: Boolean = false


    private lateinit var adapter: StoryAdapter
    private val viewModel by activityViewModels<StoryViewModel> {
        ViewModelFactory(Injection.provideStoryRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storyList = ArrayList()

        mAuth = FirebaseAuth.getInstance()
        setRecyclerView()
        getStories()
        goToAdd()
    }

    private fun setRecyclerView() {
        _binding?.rvItem?.layoutManager = LinearLayoutManager(requireContext())
        _binding?.rvItem?.setHasFixedSize(true)
    }

    private fun getStories() {
        lifecycleScope.launch {
            viewModel.getStories().observe(viewLifecycleOwner){result->
                when(result){
                    is Async.Loading -> {
                        handleLoading(true)
                    }
                    is Async.Error -> {
                        handleLoading(false)
                        context?.showToast(result.error)
                    }
                    is Async.Success -> {
                        handleLoading(false)
                        setStory(result.data.story)
                    }
                }
            }
        }

    }

    private fun setStory(data: List<Story>?) {
        if (data?.size == 0){
            _binding?.noData?.visibility = View.VISIBLE
        }else {
            val currentUser = mAuth.currentUser!!.uid
            adapter = StoryAdapter(onItemLiked = {stories->
                isLiked = stories.like.contains(currentUser)
                if (isLiked){
                    lifecycleScope.launch{
                        viewModel.deleteStoryLike(stories.postId, currentUser).observe(viewLifecycleOwner){result->
                            when(result){
                                is Async.Loading -> {
                                    handleLoading(true)
                                }
                                is Async.Error -> {
                                    handleLoading(false)
                                    context?.showToast(result.error)
                                }
                                is Async.Success -> {
                                    handleLoading(false)
                                    isLiked = false
                                    adapter.submitList(result.data.story)
                                }
                            }
                        }
                    }

                }else{
                    lifecycleScope.launch {
                        viewModel.addStoryLike(stories.postId, currentUser).observe(viewLifecycleOwner){ result->
                            when(result){
                                is Async.Loading -> {
                                    handleLoading(true)
                                }
                                is Async.Error -> {
                                    handleLoading(false)
                                    context?.showToast(result.error)
                                }
                                is Async.Success -> {
                                    handleLoading(false)
                                    adapter.submitList(result.data.story)
                                    isLiked = false
                                }
                            }
                        }
                    }
                }

            },
                onItemShared = {storiesShared->
                    //? update share
                    val shareCount = storiesShared.share + 1
                    lifecycleScope.launch {
                        viewModel.addSharePost(storiesShared.postId, shareCount).observe(viewLifecycleOwner){result->
                            when(result){
                                is Async.Loading -> {
                                    handleLoading(true)
                                }
                                is Async.Error -> {
                                    handleLoading(false)
                                    context?.showToast(result.error)
                                }
                                is Async.Success -> {
                                    handleLoading(false)
                                    getStories()
                                }
                            }
                        }
                    }

            })
            adapter.submitList(data)
            _binding?.rvItem?.adapter = adapter


        }
    }

    private fun handleLoading(isLoading: Boolean) {
        _binding?.pbStory?.apply {
            isIndeterminate = isLoading
            if (!isLoading){
                progress = 0
                visibility = View.GONE
            }else{
                visibility = View.VISIBLE

            }
        }
    }


    private fun goToAdd() {
        _binding?.btnAdd?.setOnClickListener {
            findNavController().navigate(R.id.action_action_story_to_addStoryFragmnet)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "Story Fragment"
    }
}