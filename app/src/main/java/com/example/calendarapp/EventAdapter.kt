package com.example.calendarapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(private val onEventClick: (Event) -> Unit) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    
    private val events = mutableListOf<Event>()
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEventTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
        val tvEventTime: TextView = itemView.findViewById(R.id.tvEventTime)
        val tvEventLocation: TextView = itemView.findViewById(R.id.tvEventLocation)
        val colorIndicator: View = itemView.findViewById(R.id.colorIndicator)
        val btnEditEvent: ImageButton = itemView.findViewById(R.id.btnEditEvent)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        
        holder.tvEventTitle.text = event.title
        
        if (event.isAllDay) {
            holder.tvEventTime.text = "All Day"
        } else {
            val startTime = Date(event.startTime)
            val endTime = Date(event.endTime)
            holder.tvEventTime.text = "${dateFormat.format(startTime)} - ${dateFormat.format(endTime)}"
        }
        
        if (event.location.isNotEmpty()) {
            holder.tvEventLocation.visibility = View.VISIBLE
            holder.tvEventLocation.text = event.location
        } else {
            holder.tvEventLocation.visibility = View.GONE
        }
        
        // Set color indicator based on event type or random color
        val colors = listOf(
            holder.itemView.context.getColor(R.color.event_color_1),
            holder.itemView.context.getColor(R.color.event_color_2),
            holder.itemView.context.getColor(R.color.event_color_3),
            holder.itemView.context.getColor(R.color.event_color_4),
            holder.itemView.context.getColor(R.color.event_color_5)
        )
        val colorIndex = (event.id % colors.size).toInt()
        holder.colorIndicator.setBackgroundColor(colors[colorIndex])
        
        holder.itemView.setOnClickListener {
            onEventClick(event)
        }
        
        holder.btnEditEvent.setOnClickListener {
            onEventClick(event)
        }
    }
    
    override fun getItemCount(): Int = events.size
    
    fun updateEvents(newEvents: List<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }
}