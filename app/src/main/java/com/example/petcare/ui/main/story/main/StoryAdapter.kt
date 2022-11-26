package com.example.petcare.ui.main.story.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.data.stori.Like
import com.example.petcare.data.stori.Story
import com.example.petcare.data.stori.StoryResponse
import com.example.petcare.databinding.StoryItemLayoutBinding
import com.example.petcare.ui.main.home.HomeFragmentDirections
import com.example.petcare.ui.main.story.comment.CommentFragment
import com.example.petcare.ui.main.story.detail.DetailFragment
import com.example.petcare.utils.DateFormatter

class StoryAdapter(private val onLikeClicked: (Like) -> Unit): ListAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
        val ivLike = holder.binding.favorite
        val like = Like(
            data.postId, data.uid
        )
        if (like.isLiked){
            ivLike.setImageDrawable(ContextCompat.getDrawable(ivLike.context, R.drawable.ic_baseline_favorite_24))
        }else{
            ivLike.setImageDrawable(ContextCompat.getDrawable(ivLike.context, R.drawable.ic_baseline_favorite_border_24))
        }
        ivLike.setOnClickListener {
            onLikeClicked(like)
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(DetailFragment.DATA, data)
            it.findNavController().navigate(R.id.action_action_story_to_detailFragment, bundle)
        }

        holder.binding.comment.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(CommentFragment.DATA_POST, data)
            it.findNavController().navigate(R.id.action_action_story_to_commentFragment, bundle)
        }
    }


    class StoryViewHolder(val binding: StoryItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            binding.username.text = data.name
            binding.description.text = data.description
            binding.date.text = DateFormatter.formatterDate(data.createdAt!!)
            binding.commentCount.text = data.comment.toString()
            Glide.with(itemView.context)
                .load(data.urlImg)
                .centerCrop()
                .into(binding.previewPhoto)
            if (data.avatarUrl != null) {
                Glide.with(itemView.context)
                    .load(data.avatarUrl)
                    .circleCrop()
                    .into(binding.photoProfile)
            }else{
                binding.photoProfile.setImageResource(R.drawable.ic_avatar_24)
            }

        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.uid == newItem.uid
            }

        }
    }
}