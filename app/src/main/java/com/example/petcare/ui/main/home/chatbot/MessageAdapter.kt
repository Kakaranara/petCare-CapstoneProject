package com.example.petcare.ui.main.home.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.data.remote.response.Message
import com.example.petcare.helper.Constants.RECEIVE_ID
import com.example.petcare.helper.Constants.SEND_ID

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ListViewHolder>()  {

    private var messagesList = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_message_row, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {

        val currentMessage = messagesList[position]

        when (currentMessage.id) {
            SEND_ID -> {
                viewHolder.tvSender.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                viewHolder.tvBot.visibility = View.GONE
            }
            RECEIVE_ID -> {
                viewHolder.tvBot.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                viewHolder.tvSender.visibility = View.GONE
            }
        }

    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBot: TextView = itemView.findViewById(R.id.tv_bot_message)
        val tvSender: TextView = itemView.findViewById(R.id.tv_message)
    }

    override fun getItemCount() = messagesList.size

    fun insertMessage(message: Message) {
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
    }

    fun resetAll() {
        this.messagesList.clear()
        notifyDataSetChanged()
    }

}