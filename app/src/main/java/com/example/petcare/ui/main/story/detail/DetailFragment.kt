package com.example.petcare.ui.main.story.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.FragmentDetailBinding
import com.example.petcare.utils.DateFormatter


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<Story>(DATA)
        setUpView(data)
    }

    private fun setUpView(data: Story?) {
        _binding?.username?.text = data?.name
        _binding?.description?.text = data?.description
        _binding?.date?.text = DateFormatter.formatterDate(data?.createdAt!!)
        if (data.avatarUrl != null) {
            Glide.with(requireContext())
                .load(data.avatarUrl)
                .circleCrop()
                .into(_binding?.photoProfile!!)
        }else{
            _binding?.photoProfile?.setImageResource(R.drawable.ic_avatar_24)
        }
        Glide.with(requireContext())
            .load(data.urlImg)
            .centerCrop()
            .into(_binding?.photoStory!!)
        _binding?.comment?.setOnClickListener {
            it.findNavController().navigate(R.id.action_detailFragment_to_commentFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val DATA = "data"
    }


}