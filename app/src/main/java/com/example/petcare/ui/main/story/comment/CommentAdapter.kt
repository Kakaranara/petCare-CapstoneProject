package com.example.petcare.ui.main.story.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.petcare.R
import com.example.petcare.data.stori.Comment
import com.example.petcare.databinding.CommentItemLayoutBinding
import com.example.petcare.utils.DateFormatter

class CommentAdapter(): ListAdapter<Comment, CommentAdapter.CommentViewHolder>(
    DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    class CommentViewHolder(val binding: CommentItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Comment) {
            binding.name.text = data.name
            binding.comment.text = data.comment
            binding.date.text = DateFormatter.formatterDate(data.createdAt!!.toLong())
            Glide.with(itemView.context)
                .load(data.avatarUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground).error(R.drawable.ic_avatar_24))
                .circleCrop()
                .into(binding.photoprofile)
        }

    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.idPost == newItem.idPost
            }

        }
    }
}