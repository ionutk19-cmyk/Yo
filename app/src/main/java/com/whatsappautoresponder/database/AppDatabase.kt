package com.whatsappautoresponder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AutoReplyRule::class, ScheduledMessage::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun autoReplyRuleDao(): AutoReplyRuleDao
    abstract fun scheduledMessageDao(): ScheduledMessageDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "whatsapp_auto_responder_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}