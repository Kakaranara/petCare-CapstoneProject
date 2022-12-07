package com.example.petcare.ui.main.other.petshop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.data.remote.response.Feature

class PetShopAdapter(private val listPetShop: List<Feature>): RecyclerView.Adapter<PetShopAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_petshop_row, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        val currentPetShop: Feature = listPetShop[position]
        viewHolder.tvName.text = currentPetShop.text
        viewHolder.tvAddress.text = currentPetShop.place_name
        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(currentPetShop)
        }
    }

    override fun getItemCount() = listPetShop.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_item_location_name)
        val tvAddress: TextView = itemView.findViewById(R.id.tv_item_location_address)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Feature)
    }
}