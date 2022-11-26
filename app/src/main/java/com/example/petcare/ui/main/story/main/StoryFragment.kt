package com.example.petcare.ui.main.story.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.R
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.stori.Like
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentStoryBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.Async
import com.example.petcare.helper.showAlertDialog
import com.example.petcare.helper.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StoryAdapter
    private val viewModel by activityViewModels<StoryViewModel> {
        ViewModelFactory(Injection.provideStoryRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.rvItem?.layoutManager = LinearLayoutManager(requireContext())
        _binding?.rvItem?.setHasFixedSize(true)

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
        goToAdd()
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading){
            _binding?.pbStory?.visibility = View.VISIBLE
        }else{
            _binding?.pbStory?.visibility = View.GONE
        }
    }

    private fun setStory(data: List<Story>?) {
        if (data?.size == 0){
            _binding?.noData?.visibility = View.VISIBLE
        }else {
            adapter = StoryAdapter{stories->
                if (stories.isLiked){
                    val like = Like( isLiked = false)
                    viewModel.likeStory(like)
                }else{
                    val like = Like(isLiked = true)
                    viewModel.likeStory(like)
                }
            }
            adapter.submitList(data)
            _binding?.rvItem?.adapter = adapter
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