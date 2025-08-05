package com.example.calendarapp.ui.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.R
import com.example.calendarapp.databinding.ItemCalendarDayBinding
import com.example.calendarapp.ui.calendar.model.CalendarDay
import android.view.View

class CalendarAdapter(
    private val onDayClick: (CalendarDay) -> Unit
) : ListAdapter<CalendarDay, CalendarAdapter.CalendarViewHolder>(CalendarDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarDayBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CalendarViewHolder(binding, onDayClick)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CalendarViewHolder(
        private val binding: ItemCalendarDayBinding,
        private val onDayClick: (CalendarDay) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(day: CalendarDay) {
            binding.tvDayNumber.text = day.date.dayOfMonth.toString()
            
            // Set background based on day state
            when {
                day.isSelected -> {
                    binding.tvDayNumber.setBackgroundResource(R.drawable.calendar_day_selected_background)
                    binding.tvDayNumber.setTextColor(binding.root.context.getColor(R.color.white))
                }
                day.isToday -> {
                    binding.tvDayNumber.setBackgroundResource(R.drawable.calendar_day_today_background)
                    binding.tvDayNumber.setTextColor(binding.root.context.getColor(R.color.white))
                }
                else -> {
                    binding.tvDayNumber.setBackgroundResource(R.drawable.calendar_day_background)
                    if (day.isCurrentMonth) {
                        binding.tvDayNumber.setTextColor(binding.root.context.getColor(R.color.calendar_text_primary))
                    } else {
                        binding.tvDayNumber.setTextColor(binding.root.context.getColor(R.color.calendar_text_disabled))
                    }
                }
            }
            
            // Show event indicator if there are events on this day
            // This would be implemented by checking if the day has events
            binding.indicatorEvents.visibility = if (day.hasEvents) View.VISIBLE else View.GONE
            
            binding.root.setOnClickListener {
                onDayClick(day)
            }
        }
    }

    private class CalendarDiffCallback : DiffUtil.ItemCallback<CalendarDay>() {
        override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }
    }
}