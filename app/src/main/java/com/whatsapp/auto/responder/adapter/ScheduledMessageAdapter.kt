package com.whatsapp.auto.responder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whatsapp.auto.responder.databinding.ItemScheduledMessageBinding
import com.whatsapp.auto.responder.model.ScheduledMessage
import java.time.format.DateTimeFormatter

class ScheduledMessageAdapter(
    private val onEditClick: (ScheduledMessage) -> Unit,
    private val onDeleteClick: (ScheduledMessage) -> Unit
) : ListAdapter<ScheduledMessage, ScheduledMessageAdapter.ViewHolder>(ScheduledMessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScheduledMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemScheduledMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val timeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
        private val repeatTypeMap = mapOf(
            "none" to "No Repeat",
            "daily" to "Daily",
            "weekly" to "Weekly",
            "monthly" to "Monthly"
        )

        fun bind(scheduledMessage: ScheduledMessage) {
            binding.tvMessage.text = scheduledMessage.message
            binding.tvPhoneNumber.text = scheduledMessage.phoneNumber
            binding.tvScheduledTime.text = scheduledMessage.scheduledTime.format(timeFormatter)
            binding.tvRepeatType.text = repeatTypeMap[scheduledMessage.repeatType] ?: "Unknown"
            
            binding.btnEdit.setOnClickListener {
                onEditClick(scheduledMessage)
            }
            
            binding.btnDelete.setOnClickListener {
                onDeleteClick(scheduledMessage)
            }
        }
    }

    private class ScheduledMessageDiffCallback : DiffUtil.ItemCallback<ScheduledMessage>() {
        override fun areItemsTheSame(oldItem: ScheduledMessage, newItem: ScheduledMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScheduledMessage, newItem: ScheduledMessage): Boolean {
            return oldItem == newItem
        }
    }
}