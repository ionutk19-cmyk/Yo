package com.whatsappautoresponder.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whatsappautoresponder.database.ScheduledMessage
import java.text.SimpleDateFormat
import java.util.*

class ScheduledMessagesAdapter(
    private val onItemClick: (ScheduledMessage) -> Unit
) : ListAdapter<ScheduledMessage, ScheduledMessagesAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1 = itemView.findViewById<TextView>(android.R.id.text1)
        private val text2 = itemView.findViewById<TextView>(android.R.id.text2)

        fun bind(message: ScheduledMessage) {
            text1.text = "To: ${message.contactName} - ${dateFormat.format(Date(message.scheduledTime))}"
            text2.text = message.message
            
            itemView.setOnClickListener {
                onItemClick(message)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScheduledMessage>() {
        override fun areItemsTheSame(oldItem: ScheduledMessage, newItem: ScheduledMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScheduledMessage, newItem: ScheduledMessage): Boolean {
            return oldItem == newItem
        }
    }
}