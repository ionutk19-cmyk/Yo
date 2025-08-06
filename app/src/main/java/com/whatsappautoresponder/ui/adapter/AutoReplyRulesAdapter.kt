package com.whatsappautoresponder.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whatsappautoresponder.R
import com.whatsappautoresponder.database.AutoReplyRule

class AutoReplyRulesAdapter(
    private val onItemClick: (AutoReplyRule) -> Unit
) : ListAdapter<AutoReplyRule, AutoReplyRulesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rule = getItem(position)
        holder.bind(rule)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1 = itemView.findViewById<TextView>(android.R.id.text1)
        private val text2 = itemView.findViewById<TextView>(android.R.id.text2)

        fun bind(rule: AutoReplyRule) {
            text1.text = if (rule.keyword.isNotEmpty()) "Keyword: ${rule.keyword}" else "Contact: ${rule.contactName}"
            text2.text = "Response: ${rule.response}"
            
            itemView.setOnClickListener {
                onItemClick(rule)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AutoReplyRule>() {
        override fun areItemsTheSame(oldItem: AutoReplyRule, newItem: AutoReplyRule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AutoReplyRule, newItem: AutoReplyRule): Boolean {
            return oldItem == newItem
        }
    }
}