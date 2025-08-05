package com.example.calendarapp.ui.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.data.Event
import com.example.calendarapp.databinding.ItemEventBinding
import java.time.format.DateTimeFormatter
import android.view.View

class EventAdapter(
    private val onEventClick: (Event) -> Unit
) : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventViewHolder(binding, onEventClick)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EventViewHolder(
        private val binding: ItemEventBinding,
        private val onEventClick: (Event) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.tvEventTitle.text = event.title
            
            // Format time
            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
            val startTime = event.startDateTime.format(timeFormatter)
            val endTime = event.endDateTime.format(timeFormatter)
            binding.tvEventTime.text = "$startTime - $endTime"
            
            // Show location if available
            if (!event.location.isNullOrBlank()) {
                binding.tvEventLocation.text = event.location
                binding.tvEventLocation.visibility = View.VISIBLE
            } else {
                binding.tvEventLocation.visibility = View.GONE
            }
            
            // Set color indicator
            binding.colorIndicator.setBackgroundColor(event.color)
            
            binding.btnEditEvent.setOnClickListener {
                onEventClick(event)
            }
            
            binding.root.setOnClickListener {
                onEventClick(event)
            }
        }
    }

    private class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}