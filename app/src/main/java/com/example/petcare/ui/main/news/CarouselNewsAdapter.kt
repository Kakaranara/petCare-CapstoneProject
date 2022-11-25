package com.example.petcare.ui.main.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.data.remote.response.News
import com.example.petcare.databinding.CarouselItemNewsBinding

class CarouselNewsAdapter(private val listNews: List<News>): RecyclerView.Adapter<CarouselNewsAdapter.CarouselViewHolder>() {

    private lateinit var onItemClickCallback: NewsAdapter.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: NewsAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class CarouselViewHolder(val binding: CarouselItemNewsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = CarouselItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val news = listNews[position]
        holder.binding.apply {
            Glide.with(ivImage).load(news.image).into(ivImage)
            tvSource.text = news.source
            tvTitle.text = news.title

        }
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listNews[holder.adapterPosition].link.toString())
        }
    }

    override fun getItemCount(): Int = 5

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }
}