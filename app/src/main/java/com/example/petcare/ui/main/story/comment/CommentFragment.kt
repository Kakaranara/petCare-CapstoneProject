package com.example.petcare.ui.main.story.comment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.stori.Comment
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentCommentBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.Async
import com.example.petcare.helper.showToast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAdapter: CommentAdapter
    private val viewModel by viewModels<CommentViewModel> {
        ViewModelFactory(Injection.provideStoryRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        val currentName = mAuth.currentUser!!.displayName!!
        val data = arguments?.getParcelable<Story>(DATA_POST)

        _binding?.postComment!!.text = buildString {
            append("Komentar untuk postingan ")
            if (currentName == data!!.name) append("kamu") else append(data.name)
        }

        val id = mAuth.currentUser?.uid
        val avatarUrl = mAuth.currentUser?.photoUrl.toString()
        val timeStamp = System.currentTimeMillis().toString()
        _binding?.ivSend?.setOnClickListener {
            val commentText = _binding?.etComment?.text.toString()
            if (commentText.isNotEmpty()){
                val comment = Comment(
                    data!!.postId, id, avatarUrl, currentName, commentText, timeStamp
                )
                addCommentAction(comment)
            }
        }
        setUpRv()
        getComment(data)




    }

    private fun setUpRv() {
        _binding?.rvItem?.layoutManager = LinearLayoutManager(requireContext())
        _binding?.rvItem?.setHasFixedSize(true)
    }

    private fun getComment(data: Story?) {
        viewModel.getAllComment(data?.postId.toString()).observe(viewLifecycleOwner){result->
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
                    handleSuccess(result.data.comments)
                }
            }
        }
    }

    private fun handleSuccess(data: List<Comment>?) {
        if (data?.size == 0){
            _binding?.noData?.visibility = View.VISIBLE
            _binding?.etComment?.hint = "Jadilah yang pertama comment"
        }else {
            _binding?.noData?.visibility = View.GONE
            mAdapter = CommentAdapter()
            mAdapter.submitList(data)
            _binding?.rvItem?.adapter = mAdapter
            data?.forEach{
                updatePost(it.idPost.toString(), data.size)
            }
        }
    }

    private fun addCommentAction(data: Comment) {
        lifecycleScope.launch {
            viewModel.addComment(data).observe(viewLifecycleOwner){result->
                when(result){
                    is Async.Error -> {
                        handleLoading(false)
                        context?.showToast(result.error)
                    }
                    is Async.Loading -> handleLoading(true)
                    is Async.Success -> {
                        handleLoading(false)
                        _binding?.etComment?.clearFocus()
                        _binding?.etComment?.text = null
                        handleSuccess(result.data.comments)
                    }
                }
            }
        }
    }

    private fun updatePost(postId: String, comment: Int) {
        lifecycleScope.launch {
            viewModel.updatePostComment(postId, comment).observe(viewLifecycleOwner){result->
                when(result){
                    is Async.Error -> {
                        handleLoading(false)
                        context?.showToast(result.error)
                    }
                    is Async.Loading -> handleLoading(true)
                    is Async.Success -> {
                        handleLoading(false)
                        Log.d(TAG, "update post comment")
                    }
                }
            }

        }
    }

    private fun handleLoading(isLoading: Boolean) {
        _binding?.pbComment?.apply {
            isIndeterminate = isLoading
            if (!isLoading){
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
    ): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val TAG = "CommentFragment"
        const val DATA_POST = "data_post"
    }
}