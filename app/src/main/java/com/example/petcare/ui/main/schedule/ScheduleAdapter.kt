package com.example.petcare.ui.main.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.data.remote.response.GroupedSchedule
import com.example.petcare.databinding.FragmentScheduleBinding
import com.example.petcare.databinding.ItemScheduleParentBinding

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    private val mDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<GroupedSchedule>) {
        mDiffer.submitList(list)
    }

    inner class ViewHolder(private val binding: ItemScheduleParentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupedSchedule) {
            binding.tvScheduleHeader.text = data.date
            val adapter = ScheduleChildAdapter()
            val manager = LinearLayoutManager(binding.root.context)

            binding.rvChildSchedule.adapter = adapter
            binding.rvChildSchedule.layoutManager = manager

            adapter.submitList(data.schedule ?: listOf())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemScheduleParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int = mDiffer.currentList.size

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GroupedSchedule>() {
            override fun areItemsTheSame(
                oldItem: GroupedSchedule,
                newItem: GroupedSchedule
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: GroupedSchedule,
                newItem: GroupedSchedule
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}