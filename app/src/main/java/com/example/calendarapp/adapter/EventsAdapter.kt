package com.example.calendarapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.R
import com.example.calendarapp.data.Event
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter(
    private val onEventClick: (Event) -> Unit,
    private val onEventOptionsClick: (Event, View) -> Unit
) : ListAdapter<Event, EventsAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventTitle: TextView = itemView.findViewById(R.id.event_title)
        private val eventTime: TextView = itemView.findViewById(R.id.event_time)
        private val eventLocation: TextView = itemView.findViewById(R.id.event_location)
        private val eventDescription: TextView = itemView.findViewById(R.id.event_description)
        private val eventColorIndicator: View = itemView.findViewById(R.id.event_color_indicator)
        private val eventOptions: ImageButton = itemView.findViewById(R.id.event_options)

        fun bind(event: Event) {
            eventTitle.text = event.title
            
            // Format time
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            if (event.isAllDay) {
                eventTime.text = "All day"
            } else {
                val startTime = timeFormat.format(event.startTime)
                val endTime = timeFormat.format(event.endTime)
                eventTime.text = "$startTime - $endTime"
            }
            
            // Location
            if (event.location.isNullOrBlank()) {
                eventLocation.visibility = View.GONE
            } else {
                eventLocation.visibility = View.VISIBLE
                eventLocation.text = event.location
            }
            
            // Description
            if (event.description.isNullOrBlank()) {
                eventDescription.visibility = View.GONE
            } else {
                eventDescription.visibility = View.VISIBLE
                eventDescription.text = event.description
            }
            
            // Color indicator
            eventColorIndicator.setBackgroundColor(event.color)
            
            // Click listeners
            itemView.setOnClickListener { onEventClick(event) }
            eventOptions.setOnClickListener { onEventOptionsClick(event, it) }
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