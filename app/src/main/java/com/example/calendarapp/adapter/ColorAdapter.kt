package com.example.calendarapp.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.R

class ColorAdapter(
    private val colors: List<Int>,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = colors.size

    fun setSelectedColor(color: Int) {
        val newPosition = colors.indexOf(color)
        if (newPosition != -1) {
            val oldPosition = selectedPosition
            selectedPosition = newPosition
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val colorCircle: View = itemView.findViewById(R.id.color_circle)

        fun bind(color: Int, isSelected: Boolean) {
            val drawable = colorCircle.background as GradientDrawable
            drawable.setColor(color)
            
            if (isSelected) {
                drawable.setStroke(4, ContextCompat.getColor(itemView.context, R.color.primary_blue_dark))
            } else {
                drawable.setStroke(2, ContextCompat.getColor(itemView.context, R.color.white))
            }
            
            colorCircle.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)
                onColorSelected(color)
            }
        }
    }
}