package com.whatsappautoresponder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auto_reply_rules")
data class AutoReplyRule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val keyword: String = "",
    val response: String = "",
    val contactName: String = "",
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)