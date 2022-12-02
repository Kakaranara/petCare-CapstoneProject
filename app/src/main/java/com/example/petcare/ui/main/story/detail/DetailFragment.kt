package com.example.petcare.ui.main.story.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.petcare.BuildConfig
import com.example.petcare.R
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentDetailBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.Async
import com.example.petcare.helper.capitalizeWords
import com.example.petcare.helper.showToast
import com.example.petcare.ui.main.story.comment.CommentFragment
import com.example.petcare.utils.DateFormatter
import com.example.petcare.utils.ShareLink
import com.google.firebase.auth.FirebaseAuth


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding
    private lateinit var mAuth: FirebaseAuth
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory(Injection.provideStoryRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<Story>(DATA)
        mAuth = FirebaseAuth.getInstance()
        getDetail(data!!.postId)
    }

    private fun getDetail(postId: String) {
        viewModel.getDetailPost(postId).observe(viewLifecycleOwner){result->
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
                    setUpView(result.data)
                }
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading){
            _binding?.pbDetail?.visibility = View.VISIBLE
        }else{
            _binding?.pbDetail?.visibility = View.GONE
        }
    }

    private fun setUpView(data: Story?) {
        _binding?.username?.text = data?.name
        _binding?.description?.text = data?.description
        _binding?.date?.text = DateFormatter.formatterDate(data?.createdAt!!)
        if (data.share == 0 || data.share ==1){
            _binding?.countShare?.text = buildString {
                append(data.share.toString())
                append(" share")
            }
        }else{
            _binding?.countShare?.text = buildString {
                append(data.share.toString())
                append(" shares")
            }
        }
        if (data.comment == 0 || data.comment == 1){
            _binding?.countComment?.text = buildString {
                append(data.comment.toString())
                append(" comment")
            }
        }else{
            _binding?.countComment?.text = buildString {
                append(data.comment.toString())
                append(" comments")
            }
        }
        if (data.like.size == 0 || data.like.size == 1){
            _binding?.countLike?.text = buildString {
                append(data.like.size.toString())
                append(" like")
            }
        }else{
            _binding?.countLike?.text = buildString {
                append(data.like.size.toString())
                append(" likes")
            }

        }
        if (data.avatarUrl != null) {
            Glide.with(requireContext())
                .load(data.avatarUrl!!.toUri())
                .circleCrop()
                .into(_binding?.photoProfile!!)
        }else{
            _binding?.photoProfile?.setImageResource(R.drawable.ic_launcher_foreground)
        }
        Glide.with(requireContext())
            .load(data.urlImg)
            .centerCrop()
            .into(_binding?.photoStory!!)
        _binding?.comment?.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(CommentFragment.DATA_POST, data)
            it.findNavController().navigate(R.id.action_detailFragment_to_commentFragment, bundle)
        }
        val ivLike = _binding?.favorite!!
        val currentUser = mAuth.currentUser!!.uid
        var isLiked = data.like.contains(currentUser)
        if (isLiked){
            ivLike.setImageDrawable(ContextCompat.getDrawable(ivLike.context, R.drawable.ic_baseline_favorite_24))
        }else{
            ivLike.setImageDrawable(ContextCompat.getDrawable(ivLike.context, R.drawable.ic_baseline_favorite_border_24))
        }

        ivLike.setOnClickListener {
            if (isLiked){
//                viewModel.deletePostLike(data.postId, currentUser).observe(viewLifecycleOwner){result->
//                    when(result){
//                        is Async.Loading -> {
//                            handleLoading(true)
//                        }
//                        is Async.Error -> {
//                            handleLoading(false)
//                            context?.showToast(result.error)
//                        }
//                        is Async.Success -> {
//                            handleLoading(false)
//                            isLiked = false
//                            getDetail(data.postId)
//                        }
//                    }
//                }
            }else{
//                viewModel.addPostLike(data.postId, currentUser).observe(viewLifecycleOwner){result->
//                    when(result){
//                        is Async.Loading -> {
//                            handleLoading(true)
//                        }
//                        is Async.Error -> {
//                            handleLoading(false)
//                            context?.showToast(result.error)
//                        }
//                        is Async.Success -> {
//                            handleLoading(false)
//                            isLiked = false
//                            getDetail(data.postId)
//                        }
//                    }
//                }
            }
        }

        _binding?.share?.setOnClickListener {
            ShareLink.generateShareLink(
                deepLink = "${BuildConfig.PREFIX}/post/${data.postId}".toUri(),
                previewImgLink = data.urlImg!!.toUri()
            ){generateLink->
                try {
                    val intentShare = Intent(Intent.ACTION_SEND)
                    intentShare.type = "text/plan"
                    intentShare.putExtra(Intent.EXTRA_SUBJECT, "Ada yang baru nihh ->")
                    val body: String =
                        "\n" + data.name!!.capitalizeWords() +"\n" + "Share post on PetCare App" + "\n" +  generateLink + "\n"
                    intentShare.putExtra(Intent.EXTRA_TEXT, body)
                    startActivity(Intent.createChooser(intentShare, "Share with: "))

                    //? update data share
                    val shareCount = data.share + 1
//                    viewModel.addSharePost(data.postId, shareCount).observe(viewLifecycleOwner){result->
//                        when(result){
//                            is Async.Loading -> {
//                                handleLoading(true)
//                            }
//                            is Async.Error -> {
//                                handleLoading(false)
//                                context?.showToast(result.error)
//                            }
//                            is Async.Success -> {
//                                handleLoading(false)
//                                getDetail(data.postId)
//                            }
//                        }
//                    }
                }catch (e: Exception){
                    showToast("cannot, share")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val DATA = "data"
    }


}