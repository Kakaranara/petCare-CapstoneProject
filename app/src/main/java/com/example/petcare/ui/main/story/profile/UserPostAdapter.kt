package com.example.petcare.ui.main.story.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.ItemPostProfileBinding
import com.example.petcare.ui.main.story.detail.DetailFragment

class UserPostAdapter: ListAdapter<Story, UserPostAdapter.PostViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val data = getItem(position) ?: return
        holder.bind(data)
        holder.binding.postImg.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(DetailFragment.DATA, data)
            it.findNavController().navigate(R.id.action_profileUserFragment_to_detailFragment, bundle)
        }
    }


    class PostViewHolder(val binding: ItemPostProfileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            Glide.with(itemView.context)
                .load(data.urlImg)
                .centerCrop()
                .into(binding.postImg)
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

        }
    }
}