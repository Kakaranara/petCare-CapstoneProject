package com.example.petcare.ui.main.story.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.BaseResult
import com.example.petcare.data.stori.Comment
import com.example.petcare.data.stori.CommentResponse
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentCommentBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.Async
import com.example.petcare.helper.showToast
import com.google.firebase.auth.FirebaseAuth


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
        val data = arguments?.getParcelable<Story>(DATA_POST)
        setUpRv()
        getComment(data)
        addCommentAction(data)


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
                    handleSuccess(result.data)
                    var postId = String()
                    result.data.comments?.forEach {data->
                        postId = data.idPost.toString()
                    }
                    updatePost(postId, result.data.comments!!.size)
                }
            }
        }
    }

    private fun handleSuccess(data: CommentResponse) {
        if (data.comments?.size == 0){
            _binding?.noData?.visibility = View.VISIBLE
            _binding?.etComment?.hint = "Jadilah yang pertama comment"
        }else {
            mAdapter = CommentAdapter(data.comments!!)
            _binding?.rvItem?.adapter = mAdapter
        }
    }

    private fun addCommentAction(data: Story?) {
        mAuth = FirebaseAuth.getInstance()
        val id = mAuth.currentUser?.uid
        val avatarUrl = mAuth.currentUser?.photoUrl.toString()
        val name = mAuth.currentUser?.displayName
        val timeStamp = System.currentTimeMillis().toString()
        _binding?.ivSend?.setOnClickListener {
            val commentText = _binding?.etComment?.text.toString()
            if (commentText.isNotEmpty()){
                val comment = Comment(
                    data?.postId, id, avatarUrl, name, commentText, timeStamp
                )
                viewModel.addComment(comment).observe(viewLifecycleOwner){result->
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
                            getComment(data)
                        }
                    }
                }

            }else{
                showToast("write any word")
            }
        }
    }

    private fun updatePost(postId: String, comment: Int) {
        viewModel.updateComment(postId, comment).observe(viewLifecycleOwner){result->
            when(result){
                is Async.Error -> {
                    handleLoading(false)
                    context?.showToast(result.error)
                }
                is Async.Loading -> handleLoading(true)
                is Async.Success -> {
                    handleLoading(false)
                }
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading){
            _binding?.pbComment?.visibility = View.VISIBLE
        }else{
            _binding?.pbComment?.visibility = View.GONE
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