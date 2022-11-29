package com.example.petcare.ui.main.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.databinding.ItemScheduleChildBinding
import java.text.SimpleDateFormat
import java.util.*

class ScheduleChildAdapter : RecyclerView.Adapter<ScheduleChildAdapter.ViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<Schedule>) {
        mDiffer.submitList(list)
    }

    inner class ViewHolder(private val binding: ItemScheduleChildBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Schedule) {
            val formatDate = SimpleDateFormat("dd MMMM yyyy | HH:mm", Locale.getDefault())
            val displayDate = formatDate.format(Date(data.time!!))
            val displayName = if (data.name!!.isEmpty()) {
                "No Title"
            } else data.name
            binding.tvActivityName.text = displayName
            binding.tvDateTime.text = displayDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemScheduleChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int = mDiffer.currentList.size

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }
        }
    }
}