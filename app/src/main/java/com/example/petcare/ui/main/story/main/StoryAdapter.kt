package com.example.petcare.ui.main.story.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.BuildConfig
import com.example.petcare.R
import com.example.petcare.data.stori.Story
import com.example.petcare.databinding.StoryItemLayoutBinding
import com.example.petcare.helper.capitalizeWords
import com.example.petcare.ui.main.story.comment.CommentFragment
import com.example.petcare.ui.main.story.detail.DetailFragment
import com.example.petcare.utils.DateFormatter
import com.example.petcare.utils.ShareLink
import com.google.firebase.auth.FirebaseAuth
import okhttp3.internal.notify

class StoryAdapter(private val onItemLiked: (Story) -> Unit, private val onItemShared: (Story) -> Unit): ListAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser!!.uid
        val isLiked = data.like.contains(currentUser)
        val ivLike = holder.binding.favorite
        if (isLiked){
            ivLike.setImageDrawable(ContextCompat.getDrawable(ivLike.context, R.drawable.ic_baseline_favorite_24))
        }else{
            ivLike.setImageDrawable(ContextCompat.getDrawable(ivLike.context, R.drawable.ic_baseline_favorite_border_24))
        }
        ivLike.setOnClickListener {
            onItemLiked(data)
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

        holder.binding.share.setOnClickListener {
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
                    if (it.context != null){
                        it.context.startActivity(Intent.createChooser(intentShare, "Share with: "))
                    }

                    //?set onClicked
                    onItemShared(data)

                }catch (e: Exception){
                   Log.e("StoryFragmentAdapter", "onFailure: ${e.message.toString()}")
                }
            }
        }
    }


    class StoryViewHolder(val binding: StoryItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            binding.username.text = data.name
            binding.description.text = data.description
            binding.date.text = DateFormatter.formatterDate(data.createdAt!!)
            if (data.share == 0 || data.share ==1){
                binding.shareCount.text = buildString {
                    append(data.share)
                    append(" share")
                }
            }else{
                binding.shareCount.text = buildString {
                    append(data.share)
                    append(" shares")
                }
            }
            if (data.comment == 0 || data.comment == 1){
                binding.commentCount.text = buildString {
                    append(data.comment)
                    append(" comment")
                }
            }else{
                binding.commentCount.text = buildString {
                    append(data.comment.toString())
                    append(" comments")
                }
            }
            if (data.like.size == 0 || data.like.size == 1){
                binding.likeCount.text = buildString {
                    append(data.like.size.toString())
                    append(" like")
                }
            }else{
                binding.likeCount.text = buildString {
                    append(data.like.size.toString())
                    append(" likes")
                }

            }
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
                binding.photoProfile.setImageResource(R.drawable.ic_launcher_foreground)
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
