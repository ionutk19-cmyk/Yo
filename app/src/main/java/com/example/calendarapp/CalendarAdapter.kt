package com.example.calendarapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(private val onDayClick: (Calendar) -> Unit) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {
    
    private val days = mutableListOf<CalendarDay>()
    private var selectedPosition = -1
    private val today = Calendar.getInstance()
    
    data class CalendarDay(
        val date: Calendar,
        val isCurrentMonth: Boolean,
        val hasEvents: Boolean = false
    )
    
    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDayNumber: TextView = itemView.findViewById(R.id.tvDayNumber)
        val indicator: View = itemView.findViewById(R.id.indicator)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        val dayNumber = day.date.get(Calendar.DAY_OF_MONTH)
        
        holder.tvDayNumber.text = dayNumber.toString()
        
        // Set text color based on whether it's current month
        if (day.isCurrentMonth) {
            holder.tvDayNumber.setTextColor(holder.itemView.context.getColor(android.R.color.black))
        } else {
            holder.tvDayNumber.setTextColor(holder.itemView.context.getColor(android.R.color.darker_gray))
        }
        
        // Highlight today
        if (isToday(day.date)) {
            holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.today_background))
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.context.getColor(android.R.color.transparent))
        }
        
        // Highlight selected day
        if (position == selectedPosition) {
            holder.tvDayNumber.setTextColor(holder.itemView.context.getColor(android.R.color.white))
            holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.selected_day))
        }
        
        // Show event indicator
        holder.indicator.visibility = if (day.hasEvents) View.VISIBLE else View.GONE
        
        holder.itemView.setOnClickListener {
            if (day.isCurrentMonth) {
                val previousSelected = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onDayClick(day.date)
            }
        }
    }
    
    override fun getItemCount(): Int = days.size
    
    fun updateCalendar(calendar: Calendar) {
        days.clear()
        
        val firstDayOfMonth = calendar.clone() as Calendar
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        
        val lastDayOfMonth = calendar.clone() as Calendar
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
        
        // Add days from previous month to fill first week
        val firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK)
        val daysFromPreviousMonth = firstDayOfWeek - Calendar.SUNDAY
        
        if (daysFromPreviousMonth > 0) {
            val previousMonth = calendar.clone() as Calendar
            previousMonth.add(Calendar.MONTH, -1)
            val lastDayOfPreviousMonth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            
            for (i in daysFromPreviousMonth downTo 1) {
                val day = previousMonth.clone() as Calendar
                day.set(Calendar.DAY_OF_MONTH, lastDayOfPreviousMonth - i + 1)
                days.add(CalendarDay(day, false))
            }
        }
        
        // Add days of current month
        for (day in 1..lastDayOfMonth.get(Calendar.DAY_OF_MONTH)) {
            val currentDay = calendar.clone() as Calendar
            currentDay.set(Calendar.DAY_OF_MONTH, day)
            days.add(CalendarDay(currentDay, true))
        }
        
        // Add days from next month to fill last week
        val lastDayOfWeek = lastDayOfMonth.get(Calendar.DAY_OF_WEEK)
        val daysFromNextMonth = Calendar.SATURDAY - lastDayOfWeek
        
        if (daysFromNextMonth > 0) {
            val nextMonth = calendar.clone() as Calendar
            nextMonth.add(Calendar.MONTH, 1)
            
            for (day in 1..daysFromNextMonth) {
                val nextDay = nextMonth.clone() as Calendar
                nextDay.set(Calendar.DAY_OF_MONTH, day)
                days.add(CalendarDay(nextDay, false))
            }
        }
        
        selectedPosition = -1
        notifyDataSetChanged()
    }
    
    private fun isToday(date: Calendar): Boolean {
        return date.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
               date.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
               date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
    }
}