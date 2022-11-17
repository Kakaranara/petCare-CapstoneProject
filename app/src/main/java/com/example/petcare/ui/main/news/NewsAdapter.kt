package com.example.petcare.ui.main.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.data.remote.response.News

class NewsAdapter(private val listNews: List<News>) : RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_row, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        val currentUser: News = listNews[position]
        viewHolder.tvTitle.text = currentUser.title
        viewHolder.tvSource.text = currentUser.source
        Glide.with(viewHolder.imgAvatar.context)
            .load(currentUser.image)
            .into(viewHolder.imgAvatar)
        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listNews[viewHolder.adapterPosition].link.toString())
        }
    }

    override fun getItemCount() = listNews.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSource: TextView = itemView.findViewById(R.id.tv_item_source)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_item_title)
        var imgAvatar: ImageView = itemView.findViewById(R.id.ivImage)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }
}